/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReplaceableString
/*     */   implements Replaceable
/*     */ {
/*     */   private StringBuffer buf;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReplaceableString(String str)
/*     */   {
/*  32 */     this.buf = new StringBuffer(str);
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
/*     */   public ReplaceableString(StringBuffer buf)
/*     */   {
/*  45 */     this.buf = buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReplaceableString()
/*     */   {
/*  53 */     this.buf = new StringBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  62 */     return this.buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String substring(int start, int limit)
/*     */   {
/*  70 */     return this.buf.substring(start, limit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/*  79 */     return this.buf.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char charAt(int offset)
/*     */   {
/*  90 */     return this.buf.charAt(offset);
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
/*     */   public int char32At(int offset)
/*     */   {
/* 105 */     return UTF16.charAt(this.buf, offset);
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
/*     */   public void getChars(int srcStart, int srcLimit, char[] dst, int dstStart)
/*     */   {
/* 127 */     if (srcStart != srcLimit) {
/* 128 */       this.buf.getChars(srcStart, srcLimit, dst, dstStart);
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
/*     */ 
/*     */ 
/*     */   public void replace(int start, int limit, String text)
/*     */   {
/* 144 */     this.buf.replace(start, limit, text);
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
/*     */   public void replace(int start, int limit, char[] chars, int charsStart, int charsLen)
/*     */   {
/* 162 */     this.buf.delete(start, limit);
/* 163 */     this.buf.insert(start, chars, charsStart, charsLen);
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
/*     */   public void copy(int start, int limit, int dest)
/*     */   {
/* 182 */     if ((start == limit) && (start >= 0) && (start <= this.buf.length())) {
/* 183 */       return;
/*     */     }
/* 185 */     char[] text = new char[limit - start];
/* 186 */     getChars(start, limit, text, 0);
/* 187 */     replace(dest, dest, text, 0, limit - start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMetaData()
/*     */   {
/* 195 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ReplaceableString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */