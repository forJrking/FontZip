/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLRecordReader
/*     */   implements RecordReader
/*     */ {
/*     */   private Reader r;
/*     */   private List<String> nameStack;
/*     */   private boolean atTag;
/*     */   private String tag;
/*     */   
/*     */   public XMLRecordReader(Reader r)
/*     */   {
/*  27 */     this.r = r;
/*  28 */     this.nameStack = new ArrayList();
/*     */     
/*     */ 
/*  31 */     if (getTag().startsWith("?xml")) {
/*  32 */       advance();
/*     */     }
/*     */     
/*     */ 
/*  36 */     if (getTag().startsWith("!--")) {
/*  37 */       advance();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean open(String title) {
/*  42 */     if (getTag().equals(title)) {
/*  43 */       this.nameStack.add(title);
/*  44 */       advance();
/*  45 */       return true;
/*     */     }
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public boolean close() {
/*  51 */     int ix = this.nameStack.size() - 1;
/*  52 */     String name = (String)this.nameStack.get(ix);
/*  53 */     if (getTag().equals("/" + name)) {
/*  54 */       this.nameStack.remove(ix);
/*  55 */       advance();
/*  56 */       return true;
/*     */     }
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public boolean bool(String name) {
/*  62 */     String s = string(name);
/*  63 */     if (s != null) {
/*  64 */       return "true".equals(s);
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   public boolean[] boolArray(String name) {
/*  70 */     String[] sa = stringArray(name);
/*  71 */     if (sa != null) {
/*  72 */       boolean[] result = new boolean[sa.length];
/*  73 */       for (int i = 0; i < sa.length; i++) {
/*  74 */         result[i] = "true".equals(sa[i]);
/*     */       }
/*  76 */       return result;
/*     */     }
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public char character(String name) {
/*  82 */     String s = string(name);
/*  83 */     if (s != null) {
/*  84 */       return s.charAt(0);
/*     */     }
/*  86 */     return 65535;
/*     */   }
/*     */   
/*     */   public char[] characterArray(String name) {
/*  90 */     String[] sa = stringArray(name);
/*  91 */     if (sa != null) {
/*  92 */       char[] result = new char[sa.length];
/*  93 */       for (int i = 0; i < sa.length; i++) {
/*  94 */         result[i] = sa[i].charAt(0);
/*     */       }
/*  96 */       return result;
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public byte namedIndex(String name, String[] names) {
/* 102 */     String sa = string(name);
/* 103 */     if (sa != null) {
/* 104 */       for (int i = 0; i < names.length; i++) {
/* 105 */         if (sa.equals(names[i])) {
/* 106 */           return (byte)i;
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return -1;
/*     */   }
/*     */   
/*     */   public byte[] namedIndexArray(String name, String[] names) {
/* 114 */     String[] sa = stringArray(name);
/* 115 */     if (sa != null) {
/* 116 */       byte[] result = new byte[sa.length];
/* 117 */       label77: for (int i = 0; i < sa.length; i++) {
/* 118 */         String s = sa[i];
/* 119 */         for (int j = 0; j < names.length; j++) {
/* 120 */           if (names[j].equals(s)) {
/* 121 */             result[i] = ((byte)j);
/*     */             break label77;
/*     */           }
/*     */         }
/* 125 */         result[i] = -1;
/*     */       }
/* 127 */       return result;
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   public String string(String name) {
/* 133 */     if (match(name)) {
/* 134 */       String result = readData();
/* 135 */       if (match("/" + name)) {
/* 136 */         return result;
/*     */       }
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */   
/*     */   public String[] stringArray(String name) {
/* 143 */     if (match(name + "List")) {
/* 144 */       List<String> list = new ArrayList();
/*     */       String s;
/* 146 */       while (null != (s = string(name))) {
/* 147 */         if ("Null".equals(s)) {
/* 148 */           s = null;
/*     */         }
/* 150 */         list.add(s);
/*     */       }
/* 152 */       if (match("/" + name + "List")) {
/* 153 */         return (String[])list.toArray(new String[list.size()]);
/*     */       }
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public String[][] stringTable(String name) {
/* 160 */     if (match(name + "Table")) {
/* 161 */       List<String[]> list = new ArrayList();
/*     */       String[] sa;
/* 163 */       while (null != (sa = stringArray(name))) {
/* 164 */         list.add(sa);
/*     */       }
/* 166 */       if (match("/" + name + "Table")) {
/* 167 */         return (String[][])list.toArray(new String[list.size()][]);
/*     */       }
/*     */     }
/* 170 */     return (String[][])null;
/*     */   }
/*     */   
/*     */   private boolean match(String target) {
/* 174 */     if (getTag().equals(target))
/*     */     {
/* 176 */       advance();
/* 177 */       return true;
/*     */     }
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   private String getTag() {
/* 183 */     if (this.tag == null) {
/* 184 */       this.tag = readNextTag();
/*     */     }
/* 186 */     return this.tag;
/*     */   }
/*     */   
/*     */   private void advance() {
/* 190 */     this.tag = null;
/*     */   }
/*     */   
/*     */   private String readData() {
/* 194 */     StringBuilder sb = new StringBuilder();
/* 195 */     boolean inWhitespace = false;
/*     */     for (;;)
/*     */     {
/* 198 */       int c = readChar();
/* 199 */       if ((c == -1) || (c == 60)) {
/* 200 */         this.atTag = (c == 60);
/* 201 */         break;
/*     */       }
/* 203 */       if (c == 38) {
/* 204 */         c = readChar();
/* 205 */         if (c == 35) {
/* 206 */           StringBuilder numBuf = new StringBuilder();
/* 207 */           int radix = 10;
/* 208 */           c = readChar();
/* 209 */           if (c == 120) {
/* 210 */             radix = 16;
/* 211 */             c = readChar();
/*     */           }
/* 213 */           while ((c != 59) && (c != -1)) {
/* 214 */             numBuf.append((char)c);
/* 215 */             c = readChar();
/*     */           }
/*     */           try {
/* 218 */             int num = Integer.parseInt(numBuf.toString(), radix);
/* 219 */             c = (char)num;
/*     */           } catch (NumberFormatException ex) {
/* 221 */             System.err.println("numbuf: " + numBuf.toString() + " radix: " + radix);
/*     */             
/* 223 */             throw ex;
/*     */           }
/*     */         } else {
/* 226 */           StringBuilder charBuf = new StringBuilder();
/* 227 */           while ((c != 59) && (c != -1)) {
/* 228 */             charBuf.append((char)c);
/* 229 */             c = readChar();
/*     */           }
/* 231 */           String charName = charBuf.toString();
/* 232 */           if (charName.equals("lt")) {
/* 233 */             c = 60;
/* 234 */           } else if (charName.equals("gt")) {
/* 235 */             c = 62;
/* 236 */           } else if (charName.equals("quot")) {
/* 237 */             c = 34;
/* 238 */           } else if (charName.equals("apos")) {
/* 239 */             c = 39;
/* 240 */           } else if (charName.equals("amp")) {
/* 241 */             c = 38;
/*     */           } else {
/* 243 */             System.err.println("unrecognized character entity: '" + charName + "'");
/*     */             
/* 245 */             continue;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 250 */       if (UCharacter.isWhitespace(c)) {
/* 251 */         if (inWhitespace) {
/*     */           continue;
/*     */         }
/* 254 */         c = 32;
/* 255 */         inWhitespace = true;
/*     */       } else {
/* 257 */         inWhitespace = false;
/*     */       }
/* 259 */       sb.append((char)c);
/*     */     }
/*     */     
/* 262 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String readNextTag() {
/* 266 */     int c = 0;
/* 267 */     while (!this.atTag) {
/* 268 */       c = readChar();
/* 269 */       if ((c == 60) || (c == -1)) {
/* 270 */         if (c == 60) {
/* 271 */           this.atTag = true;
/*     */         }
/*     */         
/*     */       }
/* 275 */       else if (!UCharacter.isWhitespace(c)) {
/* 276 */         System.err.println("Unexpected non-whitespace character " + Integer.toHexString(c));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 282 */     if (this.atTag) {
/* 283 */       this.atTag = false;
/* 284 */       StringBuilder sb = new StringBuilder();
/*     */       for (;;) {
/* 286 */         c = readChar();
/* 287 */         if ((c == 62) || (c == -1)) {
/*     */           break;
/*     */         }
/* 290 */         sb.append((char)c);
/*     */       }
/*     */       
/* 293 */       return sb.toString();
/*     */     }
/* 295 */     return null;
/*     */   }
/*     */   
/*     */   int readChar() {
/*     */     try {
/* 300 */       return this.r.read();
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 304 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\XMLRecordReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */