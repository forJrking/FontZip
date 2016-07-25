/*     */ package com.ibm.icu.text;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public final class StringCharacterIterator
/*     */   implements CharacterIterator
/*     */ {
/*     */   private String text;
/*     */   private int begin;
/*     */   private int end;
/*     */   private int pos;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public StringCharacterIterator(String text)
/*     */   {
/*  42 */     this(text, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public StringCharacterIterator(String text, int pos)
/*     */   {
/*  54 */     this(text, 0, text.length(), pos);
/*     */   }
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
/*     */   public StringCharacterIterator(String text, int begin, int end, int pos)
/*     */   {
/*  68 */     if (text == null) {
/*  69 */       throw new NullPointerException();
/*     */     }
/*  71 */     this.text = text;
/*     */     
/*  73 */     if ((begin < 0) || (begin > end) || (end > text.length())) {
/*  74 */       throw new IllegalArgumentException("Invalid substring range");
/*     */     }
/*     */     
/*  77 */     if ((pos < begin) || (pos > end)) {
/*  78 */       throw new IllegalArgumentException("Invalid position");
/*     */     }
/*     */     
/*  81 */     this.begin = begin;
/*  82 */     this.end = end;
/*  83 */     this.pos = pos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void setText(String text)
/*     */   {
/*  96 */     if (text == null) {
/*  97 */       throw new NullPointerException();
/*     */     }
/*  99 */     this.text = text;
/* 100 */     this.begin = 0;
/* 101 */     this.end = text.length();
/* 102 */     this.pos = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char first()
/*     */   {
/* 112 */     this.pos = this.begin;
/* 113 */     return current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char last()
/*     */   {
/* 123 */     if (this.end != this.begin) {
/* 124 */       this.pos = (this.end - 1);
/*     */     } else {
/* 126 */       this.pos = this.end;
/*     */     }
/* 128 */     return current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char setIndex(int p)
/*     */   {
/* 138 */     if ((p < this.begin) || (p > this.end)) {
/* 139 */       throw new IllegalArgumentException("Invalid index");
/*     */     }
/* 141 */     this.pos = p;
/* 142 */     return current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char current()
/*     */   {
/* 152 */     if ((this.pos >= this.begin) && (this.pos < this.end)) {
/* 153 */       return this.text.charAt(this.pos);
/*     */     }
/*     */     
/* 156 */     return 65535;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char next()
/*     */   {
/* 167 */     if (this.pos < this.end - 1) {
/* 168 */       this.pos += 1;
/* 169 */       return this.text.charAt(this.pos);
/*     */     }
/*     */     
/* 172 */     this.pos = this.end;
/* 173 */     return 65535;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char previous()
/*     */   {
/* 184 */     if (this.pos > this.begin) {
/* 185 */       this.pos -= 1;
/* 186 */       return this.text.charAt(this.pos);
/*     */     }
/*     */     
/* 189 */     return 65535;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getBeginIndex()
/*     */   {
/* 200 */     return this.begin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getEndIndex()
/*     */   {
/* 210 */     return this.end;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getIndex()
/*     */   {
/* 220 */     return this.pos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public boolean equals(Object obj)
/*     */   {
/* 232 */     if (this == obj) {
/* 233 */       return true;
/*     */     }
/* 235 */     if (!(obj instanceof StringCharacterIterator)) {
/* 236 */       return false;
/*     */     }
/*     */     
/* 239 */     StringCharacterIterator that = (StringCharacterIterator)obj;
/*     */     
/* 241 */     if (hashCode() != that.hashCode()) {
/* 242 */       return false;
/*     */     }
/* 244 */     if (!this.text.equals(that.text)) {
/* 245 */       return false;
/*     */     }
/* 247 */     if ((this.pos != that.pos) || (this.begin != that.begin) || (this.end != that.end)) {
/* 248 */       return false;
/*     */     }
/* 250 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int hashCode()
/*     */   {
/* 260 */     return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 271 */       return (StringCharacterIterator)super.clone();
/*     */ 
/*     */     }
/*     */     catch (CloneNotSupportedException e)
/*     */     {
/* 276 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */