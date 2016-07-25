/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.VersionInfo;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ICUDebug
/*     */ {
/*     */   private static String params;
/*     */   private static boolean debug;
/*     */   private static boolean help;
/*     */   public static final String javaVersionString;
/*     */   public static final boolean isJDK14OrHigher;
/*     */   public static final VersionInfo javaVersion;
/*     */   
/*     */   public static VersionInfo getInstanceLenient(String s)
/*     */   {
/*  36 */     int[] ver = new int[4];
/*  37 */     boolean numeric = false;
/*  38 */     int i = 0;int vidx = 0;
/*  39 */     while (i < s.length()) {
/*  40 */       char c = s.charAt(i++);
/*  41 */       if ((c < '0') || (c > '9')) {
/*  42 */         if (numeric) {
/*  43 */           if (vidx == 3) {
/*     */             break;
/*     */           }
/*     */           
/*  47 */           numeric = false;
/*  48 */           vidx++;
/*     */         }
/*     */       }
/*  51 */       else if (numeric) {
/*  52 */         ver[vidx] = (ver[vidx] * 10 + (c - '0'));
/*  53 */         if (ver[vidx] > 255)
/*     */         {
/*     */ 
/*     */ 
/*  57 */           ver[vidx] = 0;
/*  58 */           break;
/*     */         }
/*     */       } else {
/*  61 */         numeric = true;
/*  62 */         ver[vidx] = (c - '0');
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  67 */     return VersionInfo.getInstance(ver[0], ver[1], ver[2], ver[3]);
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  15 */       params = System.getProperty("ICUDebug");
/*     */     }
/*     */     catch (SecurityException e) {}
/*     */     
/*     */ 
/*  20 */     debug = params != null;
/*  21 */     help = (debug) && ((params.equals("")) || (params.indexOf("help") != -1));
/*     */     
/*     */ 
/*  24 */     if (debug) {
/*  25 */       System.out.println("\nICUDebug=" + params);
/*     */     }
/*     */     
/*     */ 
/*  29 */     javaVersionString = System.getProperty("java.version", "0");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */     javaVersion = getInstanceLenient(javaVersionString);
/*     */     
/*  73 */     VersionInfo java14Version = VersionInfo.getInstance("1.4.0");
/*     */     
/*  75 */     isJDK14OrHigher = javaVersion.compareTo(java14Version) >= 0;
/*     */   }
/*     */   
/*     */   public static boolean enabled() {
/*  79 */     return debug;
/*     */   }
/*     */   
/*     */   public static boolean enabled(String arg) {
/*  83 */     if (debug) {
/*  84 */       boolean result = params.indexOf(arg) != -1;
/*  85 */       if (help) System.out.println("\nICUDebug.enabled(" + arg + ") = " + result);
/*  86 */       return result;
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public static String value(String arg) {
/*  92 */     String result = "false";
/*  93 */     if (debug) {
/*  94 */       int index = params.indexOf(arg);
/*  95 */       if (index != -1) {
/*  96 */         index += arg.length();
/*  97 */         if ((params.length() > index) && (params.charAt(index) == '=')) {
/*  98 */           index++;
/*  99 */           int limit = params.indexOf(",", index);
/* 100 */           result = params.substring(index, limit == -1 ? params.length() : limit);
/*     */         } else {
/* 102 */           result = "true";
/*     */         }
/*     */       }
/*     */       
/* 106 */       if (help) System.out.println("\nICUDebug.value(" + arg + ") = " + result);
/*     */     }
/* 108 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUDebug.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */