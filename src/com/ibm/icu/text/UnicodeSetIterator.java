/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeSetIterator
/*     */ {
/*  46 */   public static int IS_STRING = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int codepoint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int codepointEnd;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String string;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private UnicodeSet set;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeSetIterator(UnicodeSet set)
/*     */   {
/*  80 */     reset(set);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeSetIterator()
/*     */   {
/*  90 */     reset(new UnicodeSet());
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
/*     */   public boolean next()
/*     */   {
/* 113 */     if (this.nextElement <= this.endElement) {
/* 114 */       this.codepoint = (this.codepointEnd = this.nextElement++);
/* 115 */       return true;
/*     */     }
/* 117 */     if (this.range < this.endRange) {
/* 118 */       loadRange(++this.range);
/* 119 */       this.codepoint = (this.codepointEnd = this.nextElement++);
/* 120 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 125 */     if (this.stringIterator == null) {
/* 126 */       return false;
/*     */     }
/* 128 */     this.codepoint = IS_STRING;
/* 129 */     this.string = ((String)this.stringIterator.next());
/* 130 */     if (!this.stringIterator.hasNext()) {
/* 131 */       this.stringIterator = null;
/*     */     }
/* 133 */     return true;
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
/*     */   public boolean nextRange()
/*     */   {
/* 157 */     if (this.nextElement <= this.endElement) {
/* 158 */       this.codepointEnd = this.endElement;
/* 159 */       this.codepoint = this.nextElement;
/* 160 */       this.nextElement = (this.endElement + 1);
/* 161 */       return true;
/*     */     }
/* 163 */     if (this.range < this.endRange) {
/* 164 */       loadRange(++this.range);
/* 165 */       this.codepointEnd = this.endElement;
/* 166 */       this.codepoint = this.nextElement;
/* 167 */       this.nextElement = (this.endElement + 1);
/* 168 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 173 */     if (this.stringIterator == null) {
/* 174 */       return false;
/*     */     }
/* 176 */     this.codepoint = IS_STRING;
/* 177 */     this.string = ((String)this.stringIterator.next());
/* 178 */     if (!this.stringIterator.hasNext()) {
/* 179 */       this.stringIterator = null;
/*     */     }
/* 181 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset(UnicodeSet uset)
/*     */   {
/* 192 */     this.set = uset;
/* 193 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 201 */     this.endRange = (this.set.getRangeCount() - 1);
/* 202 */     this.range = 0;
/* 203 */     this.endElement = -1;
/* 204 */     this.nextElement = 0;
/* 205 */     if (this.endRange >= 0) {
/* 206 */       loadRange(this.range);
/*     */     }
/* 208 */     this.stringIterator = null;
/* 209 */     if (this.set.strings != null) {
/* 210 */       this.stringIterator = this.set.strings.iterator();
/* 211 */       if (!this.stringIterator.hasNext()) {
/* 212 */         this.stringIterator = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString()
/*     */   {
/* 222 */     if (this.codepoint != IS_STRING) {
/* 223 */       return UTF16.valueOf(this.codepoint);
/*     */     }
/* 225 */     return this.string;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 231 */   private int endRange = 0;
/* 232 */   private int range = 0;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UnicodeSet getSet()
/*     */   {
/* 239 */     return this.set;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int endElement;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int nextElement;
/* 252 */   private Iterator<String> stringIterator = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void loadRange(int aRange)
/*     */   {
/* 263 */     this.nextElement = this.set.getRangeStart(aRange);
/* 264 */     this.endElement = this.set.getRangeEnd(aRange);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeSetIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */