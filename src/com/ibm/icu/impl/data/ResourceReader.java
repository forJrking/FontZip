/*     */ package com.ibm.icu.impl.data;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceReader
/*     */ {
/*     */   private BufferedReader reader;
/*     */   private String resourceName;
/*     */   private String encoding;
/*     */   private Class<?> root;
/*     */   private int lineNo;
/*     */   
/*     */   public ResourceReader(String resourceName, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  56 */     this(ICUData.class, "data/" + resourceName, encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceReader(String resourceName)
/*     */   {
/*  66 */     this(ICUData.class, "data/" + resourceName);
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
/*     */   public ResourceReader(Class<?> rootClass, String resourceName, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  81 */     this.root = rootClass;
/*  82 */     this.resourceName = resourceName;
/*  83 */     this.encoding = encoding;
/*  84 */     this.lineNo = -1;
/*  85 */     _reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceReader(InputStream is, String resourceName, String encoding)
/*     */   {
/*  95 */     this.root = null;
/*  96 */     this.resourceName = resourceName;
/*  97 */     this.encoding = encoding;
/*     */     
/*  99 */     this.lineNo = -1;
/*     */     try {
/* 101 */       InputStreamReader isr = encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, encoding);
/*     */       
/*     */ 
/*     */ 
/* 105 */       this.reader = new BufferedReader(isr);
/* 106 */       this.lineNo = 0;
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceReader(InputStream is, String resourceName)
/*     */   {
/* 119 */     this(is, resourceName, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceReader(Class<?> rootClass, String resourceName)
/*     */   {
/* 129 */     this.root = rootClass;
/* 130 */     this.resourceName = resourceName;
/* 131 */     this.encoding = null;
/* 132 */     this.lineNo = -1;
/*     */     try {
/* 134 */       _reset();
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public String readLine()
/*     */     throws IOException
/*     */   {
/* 143 */     if (this.lineNo == 0)
/*     */     {
/* 145 */       this.lineNo += 1;
/* 146 */       String line = this.reader.readLine();
/* 147 */       if ((line.charAt(0) == 65519) || (line.charAt(0) == 65279))
/*     */       {
/* 149 */         line = line.substring(1);
/*     */       }
/* 151 */       return line;
/*     */     }
/* 153 */     this.lineNo += 1;
/* 154 */     return this.reader.readLine();
/*     */   }
/*     */   
/*     */   public String readLineSkippingComments(boolean trim)
/*     */     throws IOException
/*     */   {
/*     */     String line;
/*     */     int pos;
/*     */     do
/*     */     {
/* 164 */       line = readLine();
/* 165 */       if (line == null) {
/* 166 */         return line;
/*     */       }
/*     */       
/* 169 */       pos = PatternProps.skipWhiteSpace(line, 0);
/*     */     }
/* 171 */     while ((pos == line.length()) || (line.charAt(pos) == '#'));
/*     */     
/*     */ 
/*     */ 
/* 175 */     if (trim) line = line.substring(pos);
/* 176 */     return line;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String readLineSkippingComments()
/*     */     throws IOException
/*     */   {
/* 186 */     return readLineSkippingComments(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 196 */     return this.lineNo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String describePosition()
/*     */   {
/* 204 */     return this.resourceName + ':' + this.lineNo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */     try
/*     */     {
/* 217 */       _reset();
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void _reset()
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 232 */     if (this.lineNo == 0) {
/* 233 */       return;
/*     */     }
/* 235 */     InputStream is = ICUData.getStream(this.root, this.resourceName);
/* 236 */     if (is == null) {
/* 237 */       throw new IllegalArgumentException("Can't open " + this.resourceName);
/*     */     }
/*     */     
/* 240 */     InputStreamReader isr = this.encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, this.encoding);
/*     */     
/*     */ 
/* 243 */     this.reader = new BufferedReader(isr);
/* 244 */     this.lineNo = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\ResourceReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */