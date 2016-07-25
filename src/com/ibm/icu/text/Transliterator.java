/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.util.CaseInsensitiveString;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Transliterator
/*      */   implements StringTransform
/*      */ {
/*      */   public static final int FORWARD = 0;
/*      */   public static final int REVERSE = 1;
/*      */   private String ID;
/*      */   private UnicodeSet filter;
/*      */   
/*      */   public static abstract interface Factory
/*      */   {
/*      */     public abstract Transliterator getInstance(String paramString);
/*      */   }
/*      */   
/*      */   public static class Position
/*      */   {
/*      */     public int contextStart;
/*      */     public int contextLimit;
/*      */     public int start;
/*      */     public int limit;
/*      */     
/*      */     public Position()
/*      */     {
/*  313 */       this(0, 0, 0, 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Position(int contextStart, int contextLimit, int start)
/*      */     {
/*  323 */       this(contextStart, contextLimit, start, contextLimit);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Position(int contextStart, int contextLimit, int start, int limit)
/*      */     {
/*  333 */       this.contextStart = contextStart;
/*  334 */       this.contextLimit = contextLimit;
/*  335 */       this.start = start;
/*  336 */       this.limit = limit;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Position(Position pos)
/*      */     {
/*  344 */       set(pos);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void set(Position pos)
/*      */     {
/*  352 */       this.contextStart = pos.contextStart;
/*  353 */       this.contextLimit = pos.contextLimit;
/*  354 */       this.start = pos.start;
/*  355 */       this.limit = pos.limit;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/*  363 */       if ((obj instanceof Position)) {
/*  364 */         Position pos = (Position)obj;
/*  365 */         return (this.contextStart == pos.contextStart) && (this.contextLimit == pos.contextLimit) && (this.start == pos.start) && (this.limit == pos.limit);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  370 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  378 */       return "[cs=" + this.contextStart + ", s=" + this.start + ", l=" + this.limit + ", cl=" + this.contextLimit + "]";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final void validate(int length)
/*      */     {
/*  393 */       if ((this.contextStart < 0) || (this.start < this.contextStart) || (this.limit < this.start) || (this.contextLimit < this.limit) || (length < this.contextLimit))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  398 */         throw new IllegalArgumentException("Invalid Position {cs=" + this.contextStart + ", s=" + this.start + ", l=" + this.limit + ", cl=" + this.contextLimit + "}, len=" + length);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  421 */   private int maximumContextLength = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Transliterator(String ID, UnicodeFilter filter)
/*      */   {
/*  489 */     if (ID == null) {
/*  490 */       throw new NullPointerException();
/*      */     }
/*  492 */     this.ID = ID;
/*  493 */     setFilter(filter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int transliterate(Replaceable text, int start, int limit)
/*      */   {
/*  512 */     if ((start < 0) || (limit < start) || (text.length() < limit))
/*      */     {
/*      */ 
/*  515 */       return -1;
/*      */     }
/*      */     
/*  518 */     Position pos = new Position(start, limit, start);
/*  519 */     filteredTransliterate(text, pos, false, true);
/*  520 */     return pos.limit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void transliterate(Replaceable text)
/*      */   {
/*  529 */     transliterate(text, 0, text.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String transliterate(String text)
/*      */   {
/*  540 */     ReplaceableString result = new ReplaceableString(text);
/*  541 */     transliterate(result);
/*  542 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void transliterate(Replaceable text, Position index, String insertion)
/*      */   {
/*  595 */     index.validate(text.length());
/*      */     
/*      */ 
/*  598 */     if (insertion != null) {
/*  599 */       text.replace(index.limit, index.limit, insertion);
/*  600 */       index.limit += insertion.length();
/*  601 */       index.contextLimit += insertion.length();
/*      */     }
/*      */     
/*  604 */     if ((index.limit > 0) && (UTF16.isLeadSurrogate(text.charAt(index.limit - 1))))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  610 */       return;
/*      */     }
/*      */     
/*  613 */     filteredTransliterate(text, index, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void transliterate(Replaceable text, Position index, int insertion)
/*      */   {
/*  641 */     transliterate(text, index, UTF16.valueOf(insertion));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void transliterate(Replaceable text, Position index)
/*      */   {
/*  657 */     transliterate(text, index, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void finishTransliteration(Replaceable text, Position index)
/*      */   {
/*  673 */     index.validate(text.length());
/*  674 */     filteredTransliterate(text, index, false, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void filteredTransliterate(Replaceable text, Position index, boolean incremental, boolean rollback)
/*      */   {
/*  792 */     if ((this.filter == null) && (!rollback)) {
/*  793 */       handleTransliterate(text, index, incremental);
/*  794 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  821 */     int globalLimit = index.limit;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  833 */     StringBuffer log = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  840 */       if (this.filter != null)
/*      */       {
/*      */         int c;
/*      */         
/*      */ 
/*      */ 
/*  846 */         while ((index.start < globalLimit) && (!this.filter.contains(c = text.char32At(index.start))))
/*      */         {
/*  848 */           index.start += UTF16.getCharCount(c);
/*      */         }
/*      */         
/*      */ 
/*  852 */         index.limit = index.start;
/*  853 */         int c; while ((index.limit < globalLimit) && (this.filter.contains(c = text.char32At(index.limit))))
/*      */         {
/*  855 */           index.limit += UTF16.getCharCount(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  862 */       if (index.start == index.limit) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  870 */       boolean isIncrementalRun = index.limit < globalLimit ? false : incremental;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  903 */       if ((rollback) && (isIncrementalRun))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  911 */         int runStart = index.start;
/*  912 */         int runLimit = index.limit;
/*  913 */         int runLength = runLimit - runStart;
/*      */         
/*      */ 
/*  916 */         int rollbackOrigin = text.length();
/*  917 */         text.copy(runStart, runLimit, rollbackOrigin);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  924 */         int passStart = runStart;
/*  925 */         int rollbackStart = rollbackOrigin;
/*      */         
/*      */ 
/*      */ 
/*  929 */         int passLimit = index.start;
/*      */         
/*      */ 
/*      */ 
/*  933 */         int uncommittedLength = 0;
/*      */         
/*      */ 
/*  936 */         int totalDelta = 0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         for (;;)
/*      */         {
/*  943 */           int charLength = UTF16.getCharCount(text.char32At(passLimit));
/*      */           
/*  945 */           passLimit += charLength;
/*  946 */           if (passLimit > runLimit) {
/*      */             break;
/*      */           }
/*  949 */           uncommittedLength += charLength;
/*      */           
/*  951 */           index.limit = passLimit;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  963 */           handleTransliterate(text, index, true);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  970 */           int delta = index.limit - passLimit;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  975 */           if (index.start != index.limit)
/*      */           {
/*      */ 
/*  978 */             int rs = rollbackStart + delta - (index.limit - passStart);
/*      */             
/*      */ 
/*  981 */             text.replace(passStart, index.limit, "");
/*      */             
/*      */ 
/*  984 */             text.copy(rs, rs + uncommittedLength, passStart);
/*      */             
/*      */ 
/*  987 */             index.start = passStart;
/*  988 */             index.limit = passLimit;
/*  989 */             index.contextLimit -= delta;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1001 */             passStart = passLimit = index.start;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1007 */             rollbackStart += delta + uncommittedLength;
/* 1008 */             uncommittedLength = 0;
/*      */             
/*      */ 
/* 1011 */             runLimit += delta;
/* 1012 */             totalDelta += delta;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1023 */         rollbackOrigin += totalDelta;
/* 1024 */         globalLimit += totalDelta;
/*      */         
/*      */ 
/* 1027 */         text.replace(rollbackOrigin, rollbackOrigin + runLength, "");
/*      */         
/*      */ 
/* 1030 */         index.start = passStart;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1041 */         int limit = index.limit;
/* 1042 */         handleTransliterate(text, index, isIncrementalRun);
/* 1043 */         int delta = index.limit - limit;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1057 */         if ((!isIncrementalRun) && (index.start != index.limit)) {
/* 1058 */           throw new RuntimeException("ERROR: Incomplete non-incremental transliteration by " + getID());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1064 */         globalLimit += delta;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1071 */       if ((this.filter == null) || (isIncrementalRun)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1081 */     index.limit = globalLimit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void filteredTransliterate(Replaceable text, Position index, boolean incremental)
/*      */   {
/* 1103 */     filteredTransliterate(text, index, incremental, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getMaximumContextLength()
/*      */   {
/* 1119 */     return this.maximumContextLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setMaximumContextLength(int a)
/*      */   {
/* 1128 */     if (a < 0) {
/* 1129 */       throw new IllegalArgumentException("Invalid context length " + a);
/*      */     }
/* 1131 */     this.maximumContextLength = a;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getID()
/*      */   {
/* 1143 */     return this.ID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void setID(String id)
/*      */   {
/* 1152 */     this.ID = id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String getDisplayName(String ID)
/*      */   {
/* 1163 */     return getDisplayName(ID, ULocale.getDefault(ULocale.Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(String id, Locale inLocale)
/*      */   {
/* 1186 */     return getDisplayName(id, ULocale.forLocale(inLocale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(String id, ULocale inLocale)
/*      */   {
/* 1216 */     ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/translit", inLocale);
/*      */     
/*      */ 
/*      */ 
/* 1220 */     String[] stv = TransliteratorIDParser.IDtoSTV(id);
/* 1221 */     if (stv == null)
/*      */     {
/* 1223 */       return "";
/*      */     }
/* 1225 */     String ID = stv[0] + '-' + stv[1];
/* 1226 */     if ((stv[2] != null) && (stv[2].length() > 0)) {
/* 1227 */       ID = ID + '/' + stv[2];
/*      */     }
/*      */     
/*      */ 
/* 1231 */     String n = (String)displayNameCache.get(new CaseInsensitiveString(ID));
/* 1232 */     if (n != null) {
/* 1233 */       return n;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1239 */       return bundle.getString("%Translit%%" + ID);
/*      */     }
/*      */     catch (MissingResourceException e)
/*      */     {
/*      */       try
/*      */       {
/* 1245 */         MessageFormat format = new MessageFormat(bundle.getString("TransliteratorNamePattern"));
/*      */         
/*      */ 
/* 1248 */         Object[] args = { new Integer(2), stv[0], stv[1] };
/*      */         
/*      */ 
/* 1251 */         for (int j = 1; j <= 2; j++) {
/*      */           try {
/* 1253 */             args[j] = bundle.getString("%Translit%" + (String)args[j]);
/*      */           }
/*      */           catch (MissingResourceException e) {}
/*      */         }
/*      */         
/*      */ 
/* 1259 */         return stv[2].length() > 0 ? format.format(args) + '/' + stv[2] : format.format(args);
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (MissingResourceException e2)
/*      */       {
/*      */ 
/*      */ 
/* 1267 */         throw new RuntimeException();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final UnicodeFilter getFilter()
/*      */   {
/* 1276 */     return this.filter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFilter(UnicodeFilter filter)
/*      */   {
/* 1289 */     if (filter == null) {
/* 1290 */       this.filter = null;
/*      */     } else {
/*      */       try
/*      */       {
/* 1294 */         this.filter = new UnicodeSet((UnicodeSet)filter).freeze();
/*      */       } catch (Exception e) {
/* 1296 */         this.filter = new UnicodeSet();
/* 1297 */         filter.addMatchSetTo(this.filter);
/* 1298 */         this.filter.freeze();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Transliterator getInstance(String ID)
/*      */   {
/* 1314 */     return getInstance(ID, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Transliterator getInstance(String ID, int dir)
/*      */   {
/* 1334 */     StringBuffer canonID = new StringBuffer();
/* 1335 */     List<TransliteratorIDParser.SingleID> list = new ArrayList();
/* 1336 */     UnicodeSet[] globalFilter = new UnicodeSet[1];
/* 1337 */     if (!TransliteratorIDParser.parseCompoundID(ID, dir, canonID, list, globalFilter)) {
/* 1338 */       throw new IllegalArgumentException("Invalid ID " + ID);
/*      */     }
/*      */     
/* 1341 */     List<Transliterator> translits = TransliteratorIDParser.instantiateList(list);
/*      */     
/*      */ 
/* 1344 */     Transliterator t = null;
/* 1345 */     if ((list.size() > 1) || (canonID.indexOf(";") >= 0))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1351 */       t = new CompoundTransliterator(translits);
/*      */     }
/*      */     else {
/* 1354 */       t = (Transliterator)translits.get(0);
/*      */     }
/*      */     
/* 1357 */     t.setID(canonID.toString());
/* 1358 */     if (globalFilter[0] != null) {
/* 1359 */       t.setFilter(globalFilter[0]);
/*      */     }
/* 1361 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Transliterator getBasicInstance(String id, String canonID)
/*      */   {
/* 1375 */     StringBuffer s = new StringBuffer();
/* 1376 */     Transliterator t = registry.get(id, s);
/* 1377 */     if (s.length() != 0)
/*      */     {
/*      */ 
/* 1380 */       t = getInstance(s.toString(), 0);
/*      */     }
/* 1382 */     if ((t != null) && (canonID != null)) {
/* 1383 */       t.setID(canonID);
/*      */     }
/* 1385 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Transliterator createFromRules(String ID, String rules, int dir)
/*      */   {
/* 1398 */     Transliterator t = null;
/*      */     
/* 1400 */     TransliteratorParser parser = new TransliteratorParser();
/* 1401 */     parser.parse(rules, dir);
/*      */     
/*      */ 
/* 1404 */     if ((parser.idBlockVector.size() == 0) && (parser.dataVector.size() == 0)) {
/* 1405 */       t = new NullTransliterator();
/*      */     }
/* 1407 */     else if ((parser.idBlockVector.size() == 0) && (parser.dataVector.size() == 1)) {
/* 1408 */       t = new RuleBasedTransliterator(ID, (RuleBasedTransliterator.Data)parser.dataVector.get(0), parser.compoundFilter);
/*      */     }
/* 1410 */     else if ((parser.idBlockVector.size() == 1) && (parser.dataVector.size() == 0))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1415 */       if (parser.compoundFilter != null) {
/* 1416 */         t = getInstance(parser.compoundFilter.toPattern(false) + ";" + (String)parser.idBlockVector.get(0));
/*      */       }
/*      */       else {
/* 1419 */         t = getInstance((String)parser.idBlockVector.get(0));
/*      */       }
/*      */       
/* 1422 */       if (t != null) {
/* 1423 */         t.setID(ID);
/*      */       }
/*      */     }
/*      */     else {
/* 1427 */       List<Transliterator> transliterators = new ArrayList();
/* 1428 */       int passNumber = 1;
/*      */       
/* 1430 */       int limit = Math.max(parser.idBlockVector.size(), parser.dataVector.size());
/* 1431 */       for (int i = 0; i < limit; i++) {
/* 1432 */         if (i < parser.idBlockVector.size()) {
/* 1433 */           String idBlock = (String)parser.idBlockVector.get(i);
/* 1434 */           if (idBlock.length() > 0) {
/* 1435 */             Transliterator temp = getInstance(idBlock);
/* 1436 */             if (!(temp instanceof NullTransliterator))
/* 1437 */               transliterators.add(getInstance(idBlock));
/*      */           }
/*      */         }
/* 1440 */         if (i < parser.dataVector.size()) {
/* 1441 */           RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)parser.dataVector.get(i);
/* 1442 */           transliterators.add(new RuleBasedTransliterator("%Pass" + passNumber++, data, null));
/*      */         }
/*      */       }
/*      */       
/* 1446 */       t = new CompoundTransliterator(transliterators, passNumber - 1);
/* 1447 */       t.setID(ID);
/* 1448 */       if (parser.compoundFilter != null) {
/* 1449 */         t.setFilter(parser.compoundFilter);
/*      */       }
/*      */     }
/*      */     
/* 1453 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toRules(boolean escapeUnprintable)
/*      */   {
/* 1464 */     return baseToRules(escapeUnprintable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String baseToRules(boolean escapeUnprintable)
/*      */   {
/* 1481 */     if (escapeUnprintable) {
/* 1482 */       StringBuffer rulesSource = new StringBuffer();
/* 1483 */       String id = getID();
/* 1484 */       for (int i = 0; i < id.length();) {
/* 1485 */         int c = UTF16.charAt(id, i);
/* 1486 */         if (!Utility.escapeUnprintable(rulesSource, c)) {
/* 1487 */           UTF16.append(rulesSource, c);
/*      */         }
/* 1489 */         i += UTF16.getCharCount(c);
/*      */       }
/* 1491 */       rulesSource.insert(0, "::");
/* 1492 */       rulesSource.append(';');
/* 1493 */       return rulesSource.toString();
/*      */     }
/* 1495 */     return "::" + getID() + ';';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Transliterator[] getElements()
/*      */   {
/*      */     Transliterator[] result;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1514 */     if ((this instanceof CompoundTransliterator)) {
/* 1515 */       CompoundTransliterator cpd = (CompoundTransliterator)this;
/* 1516 */       Transliterator[] result = new Transliterator[cpd.getCount()];
/* 1517 */       for (int i = 0; i < result.length; i++) {
/* 1518 */         result[i] = cpd.getTransliterator(i);
/*      */       }
/*      */     } else {
/* 1521 */       result = new Transliterator[] { this };
/*      */     }
/* 1523 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet getSourceSet()
/*      */   {
/* 1540 */     UnicodeSet result = new UnicodeSet();
/* 1541 */     addSourceTargetSet(getFilterAsUnicodeSet(UnicodeSet.ALL_CODE_POINTS), result, new UnicodeSet());
/* 1542 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected UnicodeSet handleGetSourceSet()
/*      */   {
/* 1559 */     return new UnicodeSet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet getTargetSet()
/*      */   {
/* 1584 */     UnicodeSet result = new UnicodeSet();
/* 1585 */     addSourceTargetSet(getFilterAsUnicodeSet(UnicodeSet.ALL_CODE_POINTS), new UnicodeSet(), result);
/* 1586 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*      */   {
/* 1622 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/* 1623 */     UnicodeSet temp = new UnicodeSet(handleGetSourceSet()).retainAll(myFilter);
/*      */     
/* 1625 */     sourceSet.addAll(temp);
/*      */     
/* 1627 */     for (String s : temp) {
/* 1628 */       String t = transliterate(s);
/* 1629 */       if (!s.equals(t)) {
/* 1630 */         targetSet.addAll(t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet getFilterAsUnicodeSet(UnicodeSet externalFilter)
/*      */   {
/* 1643 */     if (this.filter == null) {
/* 1644 */       return externalFilter;
/*      */     }
/* 1646 */     UnicodeSet filterSet = new UnicodeSet(externalFilter);
/*      */     
/*      */     UnicodeSet temp;
/*      */     try
/*      */     {
/* 1651 */       temp = this.filter;
/*      */     } catch (ClassCastException e) {
/* 1653 */       this.filter.addMatchSetTo(temp = new UnicodeSet());
/*      */     }
/* 1655 */     return filterSet.retainAll(temp).freeze();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Transliterator getInverse()
/*      */   {
/* 1677 */     return getInstance(this.ID, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void registerClass(String ID, Class<? extends Transliterator> transClass, String displayName)
/*      */   {
/* 1694 */     registry.put(ID, transClass, true);
/* 1695 */     if (displayName != null) {
/* 1696 */       displayNameCache.put(new CaseInsensitiveString(ID), displayName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void registerFactory(String ID, Factory factory)
/*      */   {
/* 1708 */     registry.put(ID, factory, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void registerInstance(Transliterator trans)
/*      */   {
/* 1717 */     registry.put(trans.getID(), trans, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void registerInstance(Transliterator trans, boolean visible)
/*      */   {
/* 1726 */     registry.put(trans.getID(), trans, visible);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void registerAlias(String aliasID, String realID)
/*      */   {
/* 1738 */     registry.put(aliasID, realID, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void registerSpecialInverse(String target, String inverseTarget, boolean bidirectional)
/*      */   {
/* 1776 */     TransliteratorIDParser.registerSpecialInverse(target, inverseTarget, bidirectional);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void unregister(String ID)
/*      */   {
/* 1788 */     displayNameCache.remove(new CaseInsensitiveString(ID));
/* 1789 */     registry.remove(ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Enumeration<String> getAvailableIDs()
/*      */   {
/* 1805 */     return registry.getAvailableIDs();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Enumeration<String> getAvailableSources()
/*      */   {
/* 1816 */     return registry.getAvailableSources();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Enumeration<String> getAvailableTargets(String source)
/*      */   {
/* 1827 */     return registry.getAvailableTargets(source);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Enumeration<String> getAvailableVariants(String source, String target)
/*      */   {
/* 1837 */     return registry.getAvailableVariants(source, target);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1842 */   private static TransliteratorRegistry registry = new TransliteratorRegistry();
/*      */   
/*      */ 
/* 1845 */   private static Map<CaseInsensitiveString, String> displayNameCache = Collections.synchronizedMap(new HashMap());
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String RB_DISPLAY_NAME_PREFIX = "%Translit%%";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String RB_SCRIPT_DISPLAY_NAME_PREFIX = "%Translit%";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String RB_DISPLAY_NAME_PATTERN = "TransliteratorNamePattern";
/*      */   
/*      */ 
/*      */   static final char ID_DELIM = ';';
/*      */   
/*      */ 
/*      */   static final char ID_SEP = '-';
/*      */   
/*      */ 
/*      */   static final char VARIANT_SEP = '/';
/*      */   
/*      */ 
/*      */   static final boolean DEBUG = false;
/*      */   
/*      */ 
/*      */   private static final String INDEX = "index";
/*      */   
/*      */ 
/*      */   private static final String RB_RULE_BASED_IDS = "RuleBasedTransliteratorIDs";
/*      */   
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/* 1881 */     UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/translit", "index");
/* 1882 */     UResourceBundle transIDs = bundle.get("RuleBasedTransliteratorIDs");
/*      */     
/*      */ 
/* 1885 */     int maxRows = transIDs.getSize();
/* 1886 */     for (int row = 0; row < maxRows; row++) {
/* 1887 */       UResourceBundle colBund = transIDs.get(row);
/* 1888 */       String ID = colBund.getKey();
/* 1889 */       UResourceBundle res = colBund.get(0);
/* 1890 */       String type = res.getKey();
/* 1891 */       if ((type.equals("file")) || (type.equals("internal")))
/*      */       {
/*      */ 
/* 1894 */         String resString = res.getString("resource");
/*      */         
/* 1896 */         String direction = res.getString("direction");
/* 1897 */         int dir; switch (direction.charAt(0)) {
/*      */         case 'F': 
/* 1899 */           dir = 0;
/* 1900 */           break;
/*      */         case 'R': 
/* 1902 */           dir = 1;
/* 1903 */           break;
/*      */         default: 
/* 1905 */           throw new RuntimeException("Can't parse direction: " + direction);
/*      */         }
/* 1907 */         registry.put(ID, resString, "UTF-16", dir, !type.equals("internal"));
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 1912 */       else if (type.equals("alias"))
/*      */       {
/* 1914 */         String resString = res.getString();
/* 1915 */         registry.put(ID, resString, true);
/*      */       }
/*      */       else {
/* 1918 */         throw new RuntimeException("Unknow type: " + type);
/*      */       }
/*      */     }
/*      */     
/* 1922 */     registerSpecialInverse(NullTransliterator.SHORT_ID, NullTransliterator.SHORT_ID, false);
/*      */     
/*      */ 
/* 1925 */     registerClass(NullTransliterator._ID, NullTransliterator.class, null);
/*      */     
/* 1927 */     RemoveTransliterator.register();
/* 1928 */     EscapeTransliterator.register();
/* 1929 */     UnescapeTransliterator.register();
/* 1930 */     LowercaseTransliterator.register();
/* 1931 */     UppercaseTransliterator.register();
/* 1932 */     TitlecaseTransliterator.register();
/* 1933 */     CaseFoldTransliterator.register();
/* 1934 */     UnicodeNameTransliterator.register();
/* 1935 */     NameUnicodeTransliterator.register();
/* 1936 */     NormalizationTransliterator.register();
/* 1937 */     BreakTransliterator.register();
/* 1938 */     AnyTransliterator.register();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String transform(String source)
/*      */   {
/* 1975 */     return transliterate(source);
/*      */   }
/*      */   
/*      */   protected abstract void handleTransliterate(Replaceable paramReplaceable, Position paramPosition, boolean paramBoolean);
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static void registerAny() {}
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Transliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */