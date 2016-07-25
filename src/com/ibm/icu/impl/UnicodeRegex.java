/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.StringTransform;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import com.ibm.icu.util.Freezable;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeRegex
/*     */   implements Cloneable, Freezable<UnicodeRegex>, StringTransform
/*     */ {
/*     */   public String transform(String regex)
/*     */   {
/*  61 */     StringBuilder result = new StringBuilder();
/*  62 */     UnicodeSet temp = new UnicodeSet();
/*  63 */     ParsePosition pos = new ParsePosition(0);
/*  64 */     int state = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */     for (int i = 0; i < regex.length(); i++)
/*     */     {
/*  72 */       char ch = regex.charAt(i);
/*  73 */       switch (state) {
/*     */       case 0: 
/*  75 */         if (ch == '\\') {
/*  76 */           if (UnicodeSet.resemblesPattern(regex, i))
/*     */           {
/*  78 */             i = processSet(regex, i, result, temp, pos);
/*  79 */             continue;
/*     */           }
/*  81 */           state = 1;
/*  82 */         } else if (ch == '[')
/*     */         {
/*  84 */           if (UnicodeSet.resemblesPattern(regex, i))
/*  85 */             i = processSet(regex, i, result, temp, pos); }
/*  86 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case 1: 
/*  92 */         if (ch == 'Q') {
/*  93 */           state = 1;
/*     */         } else {
/*  95 */           state = 0;
/*     */         }
/*  97 */         break;
/*     */       
/*     */       case 2: 
/* 100 */         if (ch == '\\') {
/* 101 */           state = 3;
/*     */         }
/*     */         
/*     */         break;
/*     */       case 3: 
/* 106 */         if (ch == 'E') {
/* 107 */           state = 0;
/*     */         }
/* 109 */         state = 2;
/*     */       }
/*     */       
/* 112 */       result.append(ch);
/*     */     }
/* 114 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String fix(String regex)
/*     */   {
/* 123 */     return STANDARD.transform(regex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Pattern compile(String regex)
/*     */   {
/* 133 */     return Pattern.compile(STANDARD.transform(regex));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Pattern compile(String regex, int options)
/*     */   {
/* 143 */     return Pattern.compile(STANDARD.transform(regex), options);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String compileBnf(String bnfLines)
/*     */   {
/* 153 */     return compileBnf(Arrays.asList(bnfLines.split("\\r\\n?|\\n")));
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
/*     */   public String compileBnf(List<String> lines)
/*     */   {
/* 183 */     Map<String, String> variables = getVariables(lines);
/* 184 */     Set<String> unused = new LinkedHashSet(variables.keySet());
/*     */     Iterator<String> it;
/*     */     String variable;
/* 187 */     String definition; Iterator<String> it2; for (int i = 0; i < 2; i++) {
/* 188 */       for (it = variables.keySet().iterator(); it.hasNext();) {
/* 189 */         variable = (String)it.next();
/* 190 */         definition = (String)variables.get(variable);
/* 191 */         for (it2 = variables.keySet().iterator(); it2.hasNext();) {
/* 192 */           String variable2 = (String)it2.next();
/* 193 */           if (!variable.equals(variable2)) {
/* 194 */             String definition2 = (String)variables.get(variable2);
/* 195 */             String altered2 = definition2.replace(variable, definition);
/* 196 */             if (!altered2.equals(definition2)) {
/* 197 */               unused.remove(variable);
/* 198 */               variables.put(variable2, altered2);
/* 199 */               if (this.log != null)
/*     */                 try {
/* 201 */                   this.log.append(variable2 + "=" + altered2 + ";");
/*     */                 } catch (IOException e) {
/* 203 */                   throw ((IllegalArgumentException)new IllegalArgumentException().initCause(e));
/*     */                 }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 210 */     if (unused.size() != 1) {
/* 211 */       throw new IllegalArgumentException("Not a single root: " + unused);
/*     */     }
/* 213 */     return (String)variables.get(unused.iterator().next());
/*     */   }
/*     */   
/*     */   public String getBnfCommentString() {
/* 217 */     return this.bnfCommentString;
/*     */   }
/*     */   
/*     */   public void setBnfCommentString(String bnfCommentString) {
/* 221 */     this.bnfCommentString = bnfCommentString;
/*     */   }
/*     */   
/*     */   public String getBnfVariableInfix() {
/* 225 */     return this.bnfVariableInfix;
/*     */   }
/*     */   
/*     */   public void setBnfVariableInfix(String bnfVariableInfix) {
/* 229 */     this.bnfVariableInfix = bnfVariableInfix;
/*     */   }
/*     */   
/*     */   public String getBnfLineSeparator() {
/* 233 */     return this.bnfLineSeparator;
/*     */   }
/*     */   
/*     */   public void setBnfLineSeparator(String bnfLineSeparator) {
/* 237 */     this.bnfLineSeparator = bnfLineSeparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> appendLines(List<String> result, String file, String encoding)
/*     */     throws IOException
/*     */   {
/* 249 */     return appendLines(result, new FileInputStream(file), encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> appendLines(List<String> result, InputStream inputStream, String encoding)
/*     */     throws UnsupportedEncodingException, IOException
/*     */   {
/* 262 */     BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, encoding == null ? "UTF-8" : encoding));
/*     */     for (;;) {
/* 264 */       String line = in.readLine();
/* 265 */       if (line == null) break;
/* 266 */       result.add(line);
/*     */     }
/* 268 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeRegex cloneAsThawed()
/*     */   {
/*     */     try
/*     */     {
/* 279 */       return (UnicodeRegex)clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 281 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeRegex freeze()
/*     */   {
/* 290 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFrozen()
/*     */   {
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   private int processSet(String regex, int i, StringBuilder result, UnicodeSet temp, ParsePosition pos)
/*     */   {
/*     */     try
/*     */     {
/* 305 */       pos.setIndex(i);
/* 306 */       UnicodeSet x = temp.clear().applyPattern(regex, pos, null, 0);
/* 307 */       x.complement().complement();
/* 308 */       result.append(x.toPattern(false));
/* 309 */       return pos.getIndex() - 1;
/*     */     }
/*     */     catch (Exception e) {
/* 312 */       throw ((IllegalArgumentException)new IllegalArgumentException("Error in " + regex).initCause(e));
/*     */     }
/*     */   }
/*     */   
/* 316 */   private static UnicodeRegex STANDARD = new UnicodeRegex();
/* 317 */   private String bnfCommentString = "#";
/* 318 */   private String bnfVariableInfix = "=";
/* 319 */   private String bnfLineSeparator = "\n";
/* 320 */   private Appendable log = null;
/*     */   
/* 322 */   private Comparator<Object> LongestFirst = new Comparator() {
/*     */     public int compare(Object obj0, Object obj1) {
/* 324 */       String arg0 = obj0.toString();
/* 325 */       String arg1 = obj1.toString();
/* 326 */       int len0 = arg0.length();
/* 327 */       int len1 = arg1.length();
/* 328 */       if (len0 != len1) return len1 - len0;
/* 329 */       return arg0.compareTo(arg1);
/*     */     }
/*     */   };
/*     */   
/*     */   private Map<String, String> getVariables(List<String> lines) {
/* 334 */     Map<String, String> variables = new TreeMap(this.LongestFirst);
/* 335 */     String variable = null;
/* 336 */     StringBuffer definition = new StringBuffer();
/* 337 */     int count = 0;
/* 338 */     for (Iterator<String> it = lines.iterator(); it.hasNext();) {
/* 339 */       String line = (String)it.next();
/* 340 */       count++;
/*     */       
/* 342 */       if (line.length() != 0) {
/* 343 */         if (line.charAt(0) == 65279) { line = line.substring(1);
/*     */         }
/* 345 */         if (this.bnfCommentString != null) {
/* 346 */           int hashPos = line.indexOf(this.bnfCommentString);
/* 347 */           if (hashPos >= 0) line = line.substring(0, hashPos);
/*     */         }
/* 349 */         String trimline = line.trim();
/* 350 */         if (trimline.length() != 0)
/*     */         {
/*     */ 
/* 353 */           String linePart = line;
/* 354 */           if (linePart.trim().length() != 0) {
/* 355 */             boolean terminated = trimline.endsWith(";");
/* 356 */             if (terminated) {
/* 357 */               linePart = linePart.substring(0, linePart.lastIndexOf(';'));
/*     */             }
/* 359 */             int equalsPos = linePart.indexOf(this.bnfVariableInfix);
/* 360 */             if (equalsPos >= 0) {
/* 361 */               if (variable != null) {
/* 362 */                 throw new IllegalArgumentException("Missing ';' before " + count + ") " + line);
/*     */               }
/* 364 */               variable = linePart.substring(0, equalsPos).trim();
/* 365 */               if (variables.containsKey(variable)) {
/* 366 */                 throw new IllegalArgumentException("Duplicate variable definition in " + line);
/*     */               }
/* 368 */               definition.append(linePart.substring(equalsPos + 1).trim());
/*     */             } else {
/* 370 */               if (variable == null) {
/* 371 */                 throw new IllegalArgumentException("Missing '=' at " + count + ") " + line);
/*     */               }
/* 373 */               definition.append(this.bnfLineSeparator).append(linePart);
/*     */             }
/*     */             
/* 376 */             if (terminated) {
/* 377 */               variables.put(variable, definition.toString());
/* 378 */               variable = null;
/* 379 */               definition.setLength(0);
/*     */             }
/*     */           } } } }
/* 382 */     if (variable != null) {
/* 383 */       throw new IllegalArgumentException("Missing ';' at end");
/*     */     }
/* 385 */     return variables;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UnicodeRegex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */