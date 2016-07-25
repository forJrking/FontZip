/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringPrepParseException
/*     */   extends ParseException
/*     */ {
/*     */   static final long serialVersionUID = 7160264827701651255L;
/*     */   public static final int INVALID_CHAR_FOUND = 0;
/*     */   public static final int ILLEGAL_CHAR_FOUND = 1;
/*     */   public static final int PROHIBITED_ERROR = 2;
/*     */   public static final int UNASSIGNED_ERROR = 3;
/*     */   public static final int CHECK_BIDI_ERROR = 4;
/*     */   public static final int STD3_ASCII_RULES_ERROR = 5;
/*     */   public static final int ACE_PREFIX_ERROR = 6;
/*     */   public static final int VERIFICATION_ERROR = 7;
/*     */   public static final int LABEL_TOO_LONG_ERROR = 8;
/*     */   public static final int BUFFER_OVERFLOW_ERROR = 9;
/*     */   public static final int ZERO_LENGTH_LABEL = 10;
/*     */   public static final int DOMAIN_NAME_TOO_LONG_ERROR = 11;
/*     */   private int error;
/*     */   private int line;
/*     */   
/*     */   public StringPrepParseException(String message, int error)
/*     */   {
/*  82 */     super(message, -1);
/*  83 */     this.error = error;
/*  84 */     this.line = 0;
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
/*     */   public StringPrepParseException(String message, int error, String rules, int pos)
/*     */   {
/*  98 */     super(message, -1);
/*  99 */     this.error = error;
/* 100 */     setContext(rules, pos);
/* 101 */     this.line = 0;
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
/*     */   public StringPrepParseException(String message, int error, String rules, int pos, int lineNumber)
/*     */   {
/* 118 */     super(message, -1);
/* 119 */     this.error = error;
/* 120 */     setContext(rules, pos);
/* 121 */     this.line = lineNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 133 */     if (!(other instanceof StringPrepParseException)) {
/* 134 */       return false;
/*     */     }
/* 136 */     return ((StringPrepParseException)other).error == this.error;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 146 */     StringBuilder buf = new StringBuilder();
/* 147 */     buf.append(super.getMessage());
/* 148 */     buf.append(". line:  ");
/* 149 */     buf.append(this.line);
/* 150 */     buf.append(". preContext:  ");
/* 151 */     buf.append(this.preContext);
/* 152 */     buf.append(". postContext: ");
/* 153 */     buf.append(this.postContext);
/* 154 */     buf.append("\n");
/* 155 */     return buf.toString();
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
/* 174 */   private StringBuffer preContext = new StringBuffer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */   private StringBuffer postContext = new StringBuffer();
/*     */   private static final int PARSE_CONTEXT_LEN = 16;
/*     */   
/*     */   private void setPreContext(String str, int pos)
/*     */   {
/* 185 */     setPreContext(str.toCharArray(), pos);
/*     */   }
/*     */   
/*     */   private void setPreContext(char[] str, int pos) {
/* 189 */     int start = pos <= 16 ? 0 : pos - 15;
/* 190 */     int len = start <= 16 ? start : 16;
/* 191 */     this.preContext.append(str, start, len);
/*     */   }
/*     */   
/*     */   private void setPostContext(String str, int pos)
/*     */   {
/* 196 */     setPostContext(str.toCharArray(), pos);
/*     */   }
/*     */   
/*     */   private void setPostContext(char[] str, int pos) {
/* 200 */     int start = pos;
/* 201 */     int len = str.length - start;
/* 202 */     this.postContext.append(str, start, len);
/*     */   }
/*     */   
/*     */   private void setContext(String str, int pos)
/*     */   {
/* 207 */     setPreContext(str, pos);
/* 208 */     setPostContext(str, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getError()
/*     */   {
/* 218 */     return this.error;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringPrepParseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */