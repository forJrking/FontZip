/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.Format;
/*     */ import java.text.ParsePosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectFormat
/*     */   extends Format
/*     */ {
/*     */   private static final long serialVersionUID = 2993154333257524984L;
/* 158 */   private String pattern = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient MessagePattern msgPattern;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelectFormat(String pattern)
/*     */   {
/* 171 */     applyPattern(pattern);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void reset()
/*     */   {
/* 178 */     this.pattern = null;
/* 179 */     if (this.msgPattern != null) {
/* 180 */       this.msgPattern.clear();
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
/*     */   public void applyPattern(String pattern)
/*     */   {
/* 193 */     this.pattern = pattern;
/* 194 */     if (this.msgPattern == null) {
/* 195 */       this.msgPattern = new MessagePattern();
/*     */     }
/*     */     try {
/* 198 */       this.msgPattern.parseSelectStyle(pattern);
/*     */     } catch (RuntimeException e) {
/* 200 */       reset();
/* 201 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toPattern()
/*     */   {
/* 212 */     return this.pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findSubMessage(MessagePattern pattern, int partIndex, String keyword)
/*     */   {
/* 223 */     int count = pattern.countParts();
/* 224 */     int msgStart = 0;
/*     */     do
/*     */     {
/* 227 */       MessagePattern.Part part = pattern.getPart(partIndex++);
/* 228 */       MessagePattern.Part.Type type = part.getType();
/* 229 */       if (type == MessagePattern.Part.Type.ARG_LIMIT) {
/*     */         break;
/*     */       }
/* 232 */       assert (type == MessagePattern.Part.Type.ARG_SELECTOR);
/*     */       
/* 234 */       if (pattern.partSubstringMatches(part, keyword))
/*     */       {
/* 236 */         return partIndex; }
/* 237 */       if ((msgStart == 0) && (pattern.partSubstringMatches(part, "other"))) {
/* 238 */         msgStart = partIndex;
/*     */       }
/* 240 */       partIndex = pattern.getLimitPartIndex(partIndex);
/* 241 */       partIndex++; } while (partIndex < count);
/* 242 */     return msgStart;
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
/*     */   public final String format(String keyword)
/*     */   {
/* 255 */     if (!PatternProps.isIdentifier(keyword)) {
/* 256 */       throw new IllegalArgumentException("Invalid formatting argument.");
/*     */     }
/*     */     
/* 259 */     if ((this.msgPattern == null) || (this.msgPattern.countParts() == 0)) {
/* 260 */       throw new IllegalStateException("Invalid format error.");
/*     */     }
/*     */     
/*     */ 
/* 264 */     int msgStart = findSubMessage(this.msgPattern, 0, keyword);
/* 265 */     if (!this.msgPattern.jdkAposMode()) {
/* 266 */       int msgLimit = this.msgPattern.getLimitPartIndex(msgStart);
/* 267 */       return this.msgPattern.getPatternString().substring(this.msgPattern.getPart(msgStart).getLimit(), this.msgPattern.getPatternIndex(msgLimit));
/*     */     }
/*     */     
/*     */ 
/* 271 */     StringBuilder result = null;
/* 272 */     int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
/* 273 */     int i = msgStart;
/* 274 */     for (;;) { MessagePattern.Part part = this.msgPattern.getPart(++i);
/* 275 */       MessagePattern.Part.Type type = part.getType();
/* 276 */       int index = part.getIndex();
/* 277 */       if (type == MessagePattern.Part.Type.MSG_LIMIT) {
/* 278 */         if (result == null) {
/* 279 */           return this.pattern.substring(prevIndex, index);
/*     */         }
/* 281 */         return result.append(this.pattern, prevIndex, index).toString();
/*     */       }
/* 283 */       if (type == MessagePattern.Part.Type.SKIP_SYNTAX) {
/* 284 */         if (result == null) {
/* 285 */           result = new StringBuilder();
/*     */         }
/* 287 */         result.append(this.pattern, prevIndex, index);
/* 288 */         prevIndex = part.getLimit();
/* 289 */       } else if (type == MessagePattern.Part.Type.ARG_START) {
/* 290 */         if (result == null) {
/* 291 */           result = new StringBuilder();
/*     */         }
/* 293 */         result.append(this.pattern, prevIndex, index);
/* 294 */         prevIndex = index;
/* 295 */         i = this.msgPattern.getLimitPartIndex(i);
/* 296 */         index = this.msgPattern.getPart(i).getLimit();
/* 297 */         MessagePattern.appendReducedApostrophes(this.pattern, prevIndex, index, result);
/* 298 */         prevIndex = index;
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuffer format(Object keyword, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 318 */     if ((keyword instanceof String)) {
/* 319 */       toAppendTo.append(format((String)keyword));
/*     */     } else {
/* 321 */       throw new IllegalArgumentException("'" + keyword + "' is not a String");
/*     */     }
/* 323 */     return toAppendTo;
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
/*     */   public Object parseObject(String source, ParsePosition pos)
/*     */   {
/* 337 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 346 */     if (this == obj) {
/* 347 */       return true;
/*     */     }
/* 349 */     if ((obj == null) || (getClass() != obj.getClass())) {
/* 350 */       return false;
/*     */     }
/* 352 */     SelectFormat sf = (SelectFormat)obj;
/* 353 */     return this.msgPattern == null ? false : sf.msgPattern == null ? true : this.msgPattern.equals(sf.msgPattern);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 362 */     if (this.pattern != null) {
/* 363 */       return this.pattern.hashCode();
/*     */     }
/* 365 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 374 */     return "pattern='" + this.pattern + "'";
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 379 */     in.defaultReadObject();
/* 380 */     if (this.pattern != null) {
/* 381 */       applyPattern(this.pattern);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SelectFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */