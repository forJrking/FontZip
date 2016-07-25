/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharsetMatch
/*     */   implements Comparable<CharsetMatch>
/*     */ {
/*     */   public static final int ENCODING_SCHEME = 1;
/*     */   public static final int BOM = 2;
/*     */   public static final int DECLARED_ENCODING = 4;
/*     */   public static final int LANG_STATISTICS = 8;
/*     */   private int fConfidence;
/*     */   private CharsetRecognizer fRecognizer;
/*     */   
/*     */   public Reader getReader()
/*     */   {
/*  46 */     InputStream inputStream = this.fInputStream;
/*     */     
/*  48 */     if (inputStream == null) {
/*  49 */       inputStream = new ByteArrayInputStream(this.fRawInput, 0, this.fRawLength);
/*     */     }
/*     */     try
/*     */     {
/*  53 */       inputStream.reset();
/*  54 */       return new InputStreamReader(inputStream, getName());
/*     */     } catch (IOException e) {}
/*  56 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString()
/*     */     throws IOException
/*     */   {
/*  69 */     return getString(-1);
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
/*     */   public String getString(int maxLength)
/*     */     throws IOException
/*     */   {
/*  88 */     String result = null;
/*  89 */     if (this.fInputStream != null) {
/*  90 */       StringBuilder sb = new StringBuilder();
/*  91 */       char[] buffer = new char['Ѐ'];
/*  92 */       Reader reader = getReader();
/*  93 */       int max = maxLength < 0 ? Integer.MAX_VALUE : maxLength;
/*  94 */       int bytesRead = 0;
/*     */       
/*  96 */       while ((bytesRead = reader.read(buffer, 0, Math.min(max, 1024))) >= 0) {
/*  97 */         sb.append(buffer, 0, bytesRead);
/*  98 */         max -= bytesRead;
/*     */       }
/*     */       
/* 101 */       reader.close();
/*     */       
/* 103 */       return sb.toString();
/*     */     }
/* 105 */     result = new String(this.fRawInput, getName());
/*     */     
/* 107 */     return result;
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
/*     */   public int getConfidence()
/*     */   {
/* 122 */     return this.fConfidence;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMatchType()
/*     */   {
/* 175 */     return 0;
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
/*     */   public String getName()
/*     */   {
/* 194 */     return this.fRecognizer.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLanguage()
/*     */   {
/* 205 */     return this.fRecognizer.getLanguage();
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
/*     */   public int compareTo(CharsetMatch other)
/*     */   {
/* 222 */     int compareResult = 0;
/* 223 */     if (this.fConfidence > other.fConfidence) {
/* 224 */       compareResult = 1;
/* 225 */     } else if (this.fConfidence < other.fConfidence) {
/* 226 */       compareResult = -1;
/*     */     }
/* 228 */     return compareResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf)
/*     */   {
/* 235 */     this.fRecognizer = rec;
/* 236 */     this.fConfidence = conf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 241 */     if (det.fInputStream == null)
/*     */     {
/*     */ 
/* 244 */       this.fRawInput = det.fRawInput;
/* 245 */       this.fRawLength = det.fRawLength;
/*     */     }
/* 247 */     this.fInputStream = det.fInputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 256 */   private byte[] fRawInput = null;
/*     */   
/*     */   private int fRawLength;
/*     */   
/* 260 */   private InputStream fInputStream = null;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetMatch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */