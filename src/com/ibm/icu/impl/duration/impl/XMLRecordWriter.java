/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLRecordWriter
/*     */   implements RecordWriter
/*     */ {
/*     */   private Writer w;
/*     */   private List<String> nameStack;
/*     */   static final String NULL_NAME = "Null";
/*     */   private static final String INDENT = "    ";
/*     */   
/*     */   public XMLRecordWriter(Writer w)
/*     */   {
/*  22 */     this.w = w;
/*  23 */     this.nameStack = new ArrayList();
/*     */   }
/*     */   
/*     */   public boolean open(String title) {
/*  27 */     newline();
/*  28 */     writeString("<" + title + ">");
/*  29 */     this.nameStack.add(title);
/*  30 */     return true;
/*     */   }
/*     */   
/*     */   public boolean close() {
/*  34 */     int ix = this.nameStack.size() - 1;
/*  35 */     if (ix >= 0) {
/*  36 */       String name = (String)this.nameStack.remove(ix);
/*  37 */       newline();
/*  38 */       writeString("</" + name + ">");
/*  39 */       return true;
/*     */     }
/*  41 */     return false;
/*     */   }
/*     */   
/*     */   public void flush() {
/*     */     try {
/*  46 */       this.w.flush();
/*     */     }
/*     */     catch (IOException e) {}
/*     */   }
/*     */   
/*     */   public void bool(String name, boolean value) {
/*  52 */     internalString(name, String.valueOf(value));
/*     */   }
/*     */   
/*     */   public void boolArray(String name, boolean[] values) {
/*  56 */     if (values != null) {
/*  57 */       String[] stringValues = new String[values.length];
/*  58 */       for (int i = 0; i < values.length; i++) {
/*  59 */         stringValues[i] = String.valueOf(values[i]);
/*     */       }
/*  61 */       stringArray(name, stringValues);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String ctos(char value) {
/*  66 */     if (value == '<') {
/*  67 */       return "&lt;";
/*     */     }
/*  69 */     if (value == '&') {
/*  70 */       return "&amp;";
/*     */     }
/*  72 */     return String.valueOf(value);
/*     */   }
/*     */   
/*     */   public void character(String name, char value) {
/*  76 */     if (value != 65535) {
/*  77 */       internalString(name, ctos(value));
/*     */     }
/*     */   }
/*     */   
/*     */   public void characterArray(String name, char[] values) {
/*  82 */     if (values != null) {
/*  83 */       String[] stringValues = new String[values.length];
/*  84 */       for (int i = 0; i < values.length; i++) {
/*  85 */         char value = values[i];
/*  86 */         if (value == 65535) {
/*  87 */           stringValues[i] = "Null";
/*     */         } else {
/*  89 */           stringValues[i] = ctos(value);
/*     */         }
/*     */       }
/*  92 */       internalStringArray(name, stringValues);
/*     */     }
/*     */   }
/*     */   
/*     */   public void namedIndex(String name, String[] names, int value) {
/*  97 */     if (value >= 0) {
/*  98 */       internalString(name, names[value]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void namedIndexArray(String name, String[] names, byte[] values) {
/* 103 */     if (values != null) {
/* 104 */       String[] stringValues = new String[values.length];
/* 105 */       for (int i = 0; i < values.length; i++) {
/* 106 */         int value = values[i];
/* 107 */         if (value < 0) {
/* 108 */           stringValues[i] = "Null";
/*     */         } else {
/* 110 */           stringValues[i] = names[value];
/*     */         }
/*     */       }
/* 113 */       internalStringArray(name, stringValues);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String normalize(String str) {
/* 118 */     if (str == null) {
/* 119 */       return null;
/*     */     }
/* 121 */     StringBuilder sb = null;
/* 122 */     boolean inWhitespace = false;
/* 123 */     char c = '\000';
/* 124 */     boolean special = false;
/* 125 */     for (int i = 0; i < str.length(); i++) {
/* 126 */       c = str.charAt(i);
/* 127 */       if (UCharacter.isWhitespace(c)) {
/* 128 */         if ((sb == null) && ((inWhitespace) || (c != ' '))) {
/* 129 */           sb = new StringBuilder(str.substring(0, i));
/*     */         }
/* 131 */         if (inWhitespace) {
/*     */           continue;
/*     */         }
/* 134 */         inWhitespace = true;
/* 135 */         special = false;
/* 136 */         c = ' ';
/*     */       } else {
/* 138 */         inWhitespace = false;
/* 139 */         special = (c == '<') || (c == '&');
/* 140 */         if ((special) && (sb == null)) {
/* 141 */           sb = new StringBuilder(str.substring(0, i));
/*     */         }
/*     */       }
/* 144 */       if (sb != null) {
/* 145 */         if (special) {
/* 146 */           sb.append(c == '<' ? "&lt;" : "&amp;");
/*     */         } else {
/* 148 */           sb.append(c);
/*     */         }
/*     */       }
/*     */     }
/* 152 */     if (sb != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 157 */       return sb.toString();
/*     */     }
/* 159 */     return str;
/*     */   }
/*     */   
/*     */   private void internalString(String name, String normalizedValue) {
/* 163 */     if (normalizedValue != null) {
/* 164 */       newline();
/* 165 */       writeString("<" + name + ">" + normalizedValue + "</" + name + ">");
/*     */     }
/*     */   }
/*     */   
/*     */   private void internalStringArray(String name, String[] normalizedValues) {
/* 170 */     if (normalizedValues != null) {
/* 171 */       push(name + "List");
/* 172 */       for (int i = 0; i < normalizedValues.length; i++) {
/* 173 */         String value = normalizedValues[i];
/* 174 */         if (value == null) {
/* 175 */           value = "Null";
/*     */         }
/* 177 */         string(name, value);
/*     */       }
/* 179 */       pop();
/*     */     }
/*     */   }
/*     */   
/*     */   public void string(String name, String value) {
/* 184 */     internalString(name, normalize(value));
/*     */   }
/*     */   
/*     */   public void stringArray(String name, String[] values) {
/* 188 */     if (values != null) {
/* 189 */       push(name + "List");
/* 190 */       for (int i = 0; i < values.length; i++) {
/* 191 */         String value = normalize(values[i]);
/* 192 */         if (value == null) {
/* 193 */           value = "Null";
/*     */         }
/* 195 */         internalString(name, value);
/*     */       }
/* 197 */       pop();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stringTable(String name, String[][] values) {
/* 202 */     if (values != null) {
/* 203 */       push(name + "Table");
/* 204 */       for (int i = 0; i < values.length; i++) {
/* 205 */         String[] rowValues = values[i];
/* 206 */         if (rowValues == null) {
/* 207 */           internalString(name + "List", "Null");
/*     */         } else {
/* 209 */           stringArray(name, rowValues);
/*     */         }
/*     */       }
/* 212 */       pop();
/*     */     }
/*     */   }
/*     */   
/*     */   private void push(String name) {
/* 217 */     newline();
/* 218 */     writeString("<" + name + ">");
/* 219 */     this.nameStack.add(name);
/*     */   }
/*     */   
/*     */   private void pop() {
/* 223 */     int ix = this.nameStack.size() - 1;
/* 224 */     String name = (String)this.nameStack.remove(ix);
/* 225 */     newline();
/* 226 */     writeString("</" + name + ">");
/*     */   }
/*     */   
/*     */   private void newline() {
/* 230 */     writeString("\n");
/* 231 */     for (int i = 0; i < this.nameStack.size(); i++) {
/* 232 */       writeString("    ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeString(String str) {
/* 237 */     if (this.w != null) {
/*     */       try {
/* 239 */         this.w.write(str);
/*     */       }
/*     */       catch (IOException e) {
/* 242 */         System.err.println(e.getMessage());
/* 243 */         this.w = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\XMLRecordWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */