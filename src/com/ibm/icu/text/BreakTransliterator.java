/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class BreakTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   private BreakIterator bi;
/*     */   private String insertion;
/*  22 */   private int[] boundaries = new int[50];
/*  23 */   private int boundaryCount = 0;
/*     */   static final int LETTER_OR_MARK_MASK = 510;
/*     */   
/*  26 */   public BreakTransliterator(String ID, UnicodeFilter filter, BreakIterator bi, String insertion) { super(ID, filter);
/*  27 */     this.bi = bi;
/*  28 */     this.insertion = insertion;
/*     */   }
/*     */   
/*     */   public BreakTransliterator(String ID, UnicodeFilter filter) {
/*  32 */     this(ID, filter, null, " ");
/*     */   }
/*     */   
/*     */ 
/*     */   public String getInsertion()
/*     */   {
/*  38 */     return this.insertion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setInsertion(String insertion)
/*     */   {
/*  45 */     this.insertion = insertion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BreakIterator getBreakIterator()
/*     */   {
/*  52 */     if (this.bi == null) this.bi = BreakIterator.getWordInstance(new ULocale("th_TH"));
/*  53 */     return this.bi;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBreakIterator(BreakIterator bi)
/*     */   {
/*  59 */     this.bi = bi;
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
/*     */   protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental)
/*     */   {
/*  74 */     this.boundaryCount = 0;
/*  75 */     int boundary = 0;
/*  76 */     getBreakIterator();
/*  77 */     this.bi.setText(new ReplaceableCharacterIterator(text, pos.start, pos.limit, pos.start));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     for (boundary = this.bi.first(); (boundary != -1) && (boundary < pos.limit); boundary = this.bi.next())
/*  90 */       if (boundary != 0)
/*     */       {
/*     */ 
/*  93 */         int cp = UTF16.charAt(text, boundary - 1);
/*  94 */         int type = UCharacter.getType(cp);
/*     */         
/*  96 */         if ((1 << type & 0x1FE) != 0)
/*     */         {
/*  98 */           cp = UTF16.charAt(text, boundary);
/*  99 */           type = UCharacter.getType(cp);
/*     */           
/* 101 */           if ((1 << type & 0x1FE) != 0)
/*     */           {
/* 103 */             if (this.boundaryCount >= this.boundaries.length) {
/* 104 */               int[] temp = new int[this.boundaries.length * 2];
/* 105 */               System.arraycopy(this.boundaries, 0, temp, 0, this.boundaries.length);
/* 106 */               this.boundaries = temp;
/*     */             }
/*     */             
/* 109 */             this.boundaries[(this.boundaryCount++)] = boundary;
/*     */           }
/*     */         }
/*     */       }
/* 113 */     int delta = 0;
/* 114 */     int lastBoundary = 0;
/*     */     
/* 116 */     if (this.boundaryCount != 0) {
/* 117 */       delta = this.boundaryCount * this.insertion.length();
/* 118 */       lastBoundary = this.boundaries[(this.boundaryCount - 1)];
/*     */       
/*     */ 
/*     */ 
/* 122 */       while (this.boundaryCount > 0) {
/* 123 */         boundary = this.boundaries[(--this.boundaryCount)];
/* 124 */         text.replace(boundary, boundary, this.insertion);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 129 */     pos.contextLimit += delta;
/* 130 */     pos.limit += delta;
/* 131 */     pos.start = (incremental ? lastBoundary + delta : pos.limit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void register()
/*     */   {
/* 141 */     Transliterator trans = new BreakTransliterator("Any-BreakInternal", null);
/* 142 */     Transliterator.registerInstance(trans, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ReplaceableCharacterIterator
/*     */     implements CharacterIterator
/*     */   {
/*     */     private Replaceable text;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int begin;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int end;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int pos;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReplaceableCharacterIterator(Replaceable text, int begin, int end, int pos)
/*     */     {
/* 190 */       if (text == null) {
/* 191 */         throw new NullPointerException();
/*     */       }
/* 193 */       this.text = text;
/*     */       
/* 195 */       if ((begin < 0) || (begin > end) || (end > text.length())) {
/* 196 */         throw new IllegalArgumentException("Invalid substring range");
/*     */       }
/*     */       
/* 199 */       if ((pos < begin) || (pos > end)) {
/* 200 */         throw new IllegalArgumentException("Invalid position");
/*     */       }
/*     */       
/* 203 */       this.begin = begin;
/* 204 */       this.end = end;
/* 205 */       this.pos = pos;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setText(Replaceable text)
/*     */     {
/* 217 */       if (text == null) {
/* 218 */         throw new NullPointerException();
/*     */       }
/* 220 */       this.text = text;
/* 221 */       this.begin = 0;
/* 222 */       this.end = text.length();
/* 223 */       this.pos = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char first()
/*     */     {
/* 232 */       this.pos = this.begin;
/* 233 */       return current();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char last()
/*     */     {
/* 242 */       if (this.end != this.begin) {
/* 243 */         this.pos = (this.end - 1);
/*     */       } else {
/* 245 */         this.pos = this.end;
/*     */       }
/* 247 */       return current();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char setIndex(int p)
/*     */     {
/* 256 */       if ((p < this.begin) || (p > this.end)) {
/* 257 */         throw new IllegalArgumentException("Invalid index");
/*     */       }
/* 259 */       this.pos = p;
/* 260 */       return current();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char current()
/*     */     {
/* 269 */       if ((this.pos >= this.begin) && (this.pos < this.end)) {
/* 270 */         return this.text.charAt(this.pos);
/*     */       }
/*     */       
/* 273 */       return 65535;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char next()
/*     */     {
/* 283 */       if (this.pos < this.end - 1) {
/* 284 */         this.pos += 1;
/* 285 */         return this.text.charAt(this.pos);
/*     */       }
/*     */       
/* 288 */       this.pos = this.end;
/* 289 */       return 65535;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char previous()
/*     */     {
/* 299 */       if (this.pos > this.begin) {
/* 300 */         this.pos -= 1;
/* 301 */         return this.text.charAt(this.pos);
/*     */       }
/*     */       
/* 304 */       return 65535;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getBeginIndex()
/*     */     {
/* 314 */       return this.begin;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getEndIndex()
/*     */     {
/* 323 */       return this.end;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getIndex()
/*     */     {
/* 332 */       return this.pos;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 343 */       if (this == obj) {
/* 344 */         return true;
/*     */       }
/* 346 */       if (!(obj instanceof ReplaceableCharacterIterator)) {
/* 347 */         return false;
/*     */       }
/*     */       
/* 350 */       ReplaceableCharacterIterator that = (ReplaceableCharacterIterator)obj;
/*     */       
/* 352 */       if (hashCode() != that.hashCode()) {
/* 353 */         return false;
/*     */       }
/* 355 */       if (!this.text.equals(that.text)) {
/* 356 */         return false;
/*     */       }
/* 358 */       if ((this.pos != that.pos) || (this.begin != that.begin) || (this.end != that.end)) {
/* 359 */         return false;
/*     */       }
/* 361 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 370 */       return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object clone()
/*     */     {
/*     */       try
/*     */       {
/* 380 */         return (ReplaceableCharacterIterator)super.clone();
/*     */ 
/*     */       }
/*     */       catch (CloneNotSupportedException e)
/*     */       {
/* 385 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 395 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/*     */     
/*     */ 
/* 398 */     if (myFilter.size() != 0) {
/* 399 */       targetSet.addAll(this.insertion);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BreakTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */