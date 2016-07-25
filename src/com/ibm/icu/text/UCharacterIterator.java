/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.CharacterIteratorWrapper;
/*     */ import com.ibm.icu.impl.ReplaceableUCharacterIterator;
/*     */ import com.ibm.icu.impl.UCharArrayIterator;
/*     */ import com.ibm.icu.impl.UCharacterIteratorWrapper;
/*     */ import com.ibm.icu.impl.UCharacterProperty;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UCharacterIterator
/*     */   implements Cloneable, UForwardCharacterIterator
/*     */ {
/*     */   public static final UCharacterIterator getInstance(Replaceable source)
/*     */   {
/*  54 */     return new ReplaceableUCharacterIterator(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final UCharacterIterator getInstance(String source)
/*     */   {
/*  66 */     return new ReplaceableUCharacterIterator(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final UCharacterIterator getInstance(char[] source)
/*     */   {
/*  78 */     return getInstance(source, 0, source.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final UCharacterIterator getInstance(char[] source, int start, int limit)
/*     */   {
/*  90 */     return new UCharArrayIterator(source, start, limit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final UCharacterIterator getInstance(StringBuffer source)
/*     */   {
/* 101 */     return new ReplaceableUCharacterIterator(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final UCharacterIterator getInstance(CharacterIterator source)
/*     */   {
/* 113 */     return new CharacterIteratorWrapper(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharacterIterator getCharacterIterator()
/*     */   {
/* 125 */     return new UCharacterIteratorWrapper(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int current();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int currentCodePoint()
/*     */   {
/* 146 */     int ch = current();
/* 147 */     if (UTF16.isLeadSurrogate((char)ch))
/*     */     {
/*     */ 
/* 150 */       next();
/*     */       
/*     */ 
/*     */ 
/* 154 */       int ch2 = current();
/*     */       
/*     */ 
/* 157 */       previous();
/*     */       
/* 159 */       if (UTF16.isTrailSurrogate((char)ch2))
/*     */       {
/*     */ 
/* 162 */         return UCharacterProperty.getRawSupplementary((char)ch, (char)ch2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 167 */     return ch;
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
/*     */   public abstract int getLength();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getIndex();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int next();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int nextCodePoint()
/*     */   {
/* 209 */     int ch1 = next();
/* 210 */     if (UTF16.isLeadSurrogate((char)ch1)) {
/* 211 */       int ch2 = next();
/* 212 */       if (UTF16.isTrailSurrogate((char)ch2)) {
/* 213 */         return UCharacterProperty.getRawSupplementary((char)ch1, (char)ch2);
/*     */       }
/* 215 */       if (ch2 != -1)
/*     */       {
/* 217 */         previous();
/*     */       }
/*     */     }
/* 220 */     return ch1;
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
/*     */   public abstract int previous();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int previousCodePoint()
/*     */   {
/* 247 */     int ch1 = previous();
/* 248 */     if (UTF16.isTrailSurrogate((char)ch1)) {
/* 249 */       int ch2 = previous();
/* 250 */       if (UTF16.isLeadSurrogate((char)ch2)) {
/* 251 */         return UCharacterProperty.getRawSupplementary((char)ch2, (char)ch1);
/*     */       }
/* 253 */       if (ch2 != -1)
/*     */       {
/* 255 */         next();
/*     */       }
/*     */     }
/* 258 */     return ch1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setIndex(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToLimit()
/*     */   {
/* 275 */     setIndex(getLength());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToStart()
/*     */   {
/* 283 */     setIndex(0);
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
/*     */   public abstract int getText(char[] paramArrayOfChar, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getText(char[] fillIn)
/*     */   {
/* 334 */     return getText(fillIn, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getText()
/*     */   {
/* 343 */     char[] text = new char[getLength()];
/* 344 */     getText(text);
/* 345 */     return new String(text);
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
/*     */   public int moveIndex(int delta)
/*     */   {
/* 365 */     int x = Math.max(0, Math.min(getIndex() + delta, getLength()));
/* 366 */     setIndex(x);
/* 367 */     return x;
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
/*     */   public int moveCodePointIndex(int delta)
/*     */   {
/* 386 */     if (delta > 0) {
/* 387 */       while ((delta > 0) && (nextCodePoint() != -1)) delta--;
/*     */     }
/* 389 */     while ((delta < 0) && (previousCodePoint() != -1)) { delta++;
/*     */     }
/* 391 */     if (delta != 0) {
/* 392 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 395 */     return getIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 405 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */