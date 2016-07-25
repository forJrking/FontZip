/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class VersionInfo
/*     */   implements Comparable<VersionInfo>
/*     */ {
/*     */   public static VersionInfo getInstance(String version)
/*     */   {
/* 197 */     int length = version.length();
/* 198 */     int[] array = { 0, 0, 0, 0 };
/* 199 */     int count = 0;
/* 200 */     int index = 0;
/*     */     
/* 202 */     while ((count < 4) && (index < length)) {
/* 203 */       char c = version.charAt(index);
/* 204 */       if (c == '.') {
/* 205 */         count++;
/*     */       }
/*     */       else {
/* 208 */         c = (char)(c - '0');
/* 209 */         if ((c < 0) || (c > '\t')) {
/* 210 */           throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */         }
/* 212 */         array[count] *= 10;
/* 213 */         array[count] += c;
/*     */       }
/* 215 */       index++;
/*     */     }
/* 217 */     if (index != length) {
/* 218 */       throw new IllegalArgumentException("Invalid version number: String '" + version + "' exceeds version format");
/*     */     }
/*     */     
/* 221 */     for (int i = 0; i < 4; i++) {
/* 222 */       if ((array[i] < 0) || (array[i] > 255)) {
/* 223 */         throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */       }
/*     */     }
/*     */     
/* 227 */     return getInstance(array[0], array[1], array[2], array[3]);
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
/*     */   public static VersionInfo getInstance(int major, int minor, int milli, int micro)
/*     */   {
/* 244 */     if ((major < 0) || (major > 255) || (minor < 0) || (minor > 255) || (milli < 0) || (milli > 255) || (micro < 0) || (micro > 255))
/*     */     {
/* 246 */       throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */     }
/* 248 */     int version = getInt(major, minor, milli, micro);
/* 249 */     Integer key = new Integer(version);
/* 250 */     VersionInfo result = (VersionInfo)MAP_.get(key);
/* 251 */     if (result == null) {
/* 252 */       result = new VersionInfo(version);
/* 253 */       VersionInfo tmpvi = (VersionInfo)MAP_.putIfAbsent(key, result);
/* 254 */       if (tmpvi != null) {
/* 255 */         result = tmpvi;
/*     */       }
/*     */     }
/* 258 */     return result;
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
/*     */   public static VersionInfo getInstance(int major, int minor, int milli)
/*     */   {
/* 273 */     return getInstance(major, minor, milli, 0);
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
/*     */   public static VersionInfo getInstance(int major, int minor)
/*     */   {
/* 287 */     return getInstance(major, minor, 0, 0);
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
/*     */   public static VersionInfo getInstance(int major)
/*     */   {
/* 300 */     return getInstance(major, 0, 0, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static VersionInfo javaVersion()
/*     */   {
/* 310 */     if (javaVersion == null) {
/* 311 */       String s = System.getProperty("java.version");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 317 */       char[] chars = s.toCharArray();
/* 318 */       int r = 0;int w = 0;int count = 0;
/* 319 */       boolean numeric = false;
/* 320 */       while (r < chars.length) {
/* 321 */         char c = chars[(r++)];
/* 322 */         if ((c < '0') || (c > '9')) {
/* 323 */           if (numeric) {
/* 324 */             if (count == 3) {
/*     */               break;
/*     */             }
/*     */             
/* 328 */             numeric = false;
/* 329 */             chars[(w++)] = '.';
/* 330 */             count++;
/*     */           }
/*     */         } else {
/* 333 */           numeric = true;
/* 334 */           chars[(w++)] = c;
/*     */         }
/*     */       }
/* 337 */       while ((w > 0) && (chars[(w - 1)] == '.')) {
/* 338 */         w--;
/*     */       }
/*     */       
/* 341 */       String vs = new String(chars, 0, w);
/*     */       
/* 343 */       javaVersion = getInstance(vs);
/*     */     }
/* 345 */     return javaVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 356 */     StringBuilder result = new StringBuilder(7);
/* 357 */     result.append(getMajor());
/* 358 */     result.append('.');
/* 359 */     result.append(getMinor());
/* 360 */     result.append('.');
/* 361 */     result.append(getMilli());
/* 362 */     result.append('.');
/* 363 */     result.append(getMicro());
/* 364 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 374 */     return this.m_version_ >> 24 & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 384 */     return this.m_version_ >> 16 & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMilli()
/*     */   {
/* 394 */     return this.m_version_ >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMicro()
/*     */   {
/* 404 */     return this.m_version_ & 0xFF;
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
/* 416 */     return other == this;
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
/*     */   public int compareTo(VersionInfo other)
/*     */   {
/* 432 */     return this.m_version_ - other.m_version_;
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
/* 452 */   private static final ConcurrentHashMap<Integer, VersionInfo> MAP_ = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 469 */     UNICODE_1_0 = getInstance(1, 0, 0, 0);
/* 470 */     UNICODE_1_0_1 = getInstance(1, 0, 1, 0);
/* 471 */     UNICODE_1_1_0 = getInstance(1, 1, 0, 0);
/* 472 */     UNICODE_1_1_5 = getInstance(1, 1, 5, 0);
/* 473 */     UNICODE_2_0 = getInstance(2, 0, 0, 0);
/* 474 */     UNICODE_2_1_2 = getInstance(2, 1, 2, 0);
/* 475 */     UNICODE_2_1_5 = getInstance(2, 1, 5, 0);
/* 476 */     UNICODE_2_1_8 = getInstance(2, 1, 8, 0);
/* 477 */     UNICODE_2_1_9 = getInstance(2, 1, 9, 0);
/* 478 */     UNICODE_3_0 = getInstance(3, 0, 0, 0);
/* 479 */     UNICODE_3_0_1 = getInstance(3, 0, 1, 0);
/* 480 */     UNICODE_3_1_0 = getInstance(3, 1, 0, 0);
/* 481 */     UNICODE_3_1_1 = getInstance(3, 1, 1, 0);
/* 482 */     UNICODE_3_2 = getInstance(3, 2, 0, 0);
/* 483 */     UNICODE_4_0 = getInstance(4, 0, 0, 0);
/* 484 */     UNICODE_4_0_1 = getInstance(4, 0, 1, 0);
/* 485 */     UNICODE_4_1 = getInstance(4, 1, 0, 0);
/* 486 */     UNICODE_5_0 = getInstance(5, 0, 0, 0);
/* 487 */     UNICODE_5_1 = getInstance(5, 1, 0, 0);
/* 488 */     UNICODE_5_2 = getInstance(5, 2, 0, 0);
/* 489 */     UNICODE_6_0 = getInstance(6, 0, 0, 0);
/*     */     
/* 491 */     ICU_VERSION = getInstance(4, 8, 1, 1);
/* 492 */     ICU_DATA_VERSION = getInstance(4, 8, 1, 0);
/* 493 */     UNICODE_VERSION = UNICODE_6_0; }
/*     */   
/* 495 */   public static final VersionInfo UCOL_RUNTIME_VERSION = getInstance(7);
/* 496 */   public static final VersionInfo UCOL_BUILDER_VERSION = getInstance(8);
/* 497 */   public static final VersionInfo UCOL_TAILORINGS_VERSION = getInstance(1);
/*     */   public static final VersionInfo UNICODE_1_0;
/*     */   public static final VersionInfo UNICODE_1_0_1;
/*     */   public static final VersionInfo UNICODE_1_1_0;
/*     */   public static final VersionInfo UNICODE_1_1_5;
/*     */   public static final VersionInfo UNICODE_2_0;
/*     */   public static final VersionInfo UNICODE_2_1_2;
/*     */   public static final VersionInfo UNICODE_2_1_5;
/*     */   
/*     */   private VersionInfo(int compactversion)
/*     */   {
/* 508 */     this.m_version_ = compactversion;
/*     */   }
/*     */   
/*     */ 
/*     */   public static final VersionInfo UNICODE_2_1_8;
/*     */   public static final VersionInfo UNICODE_2_1_9;
/*     */   public static final VersionInfo UNICODE_3_0;
/*     */   public static final VersionInfo UNICODE_3_0_1;
/*     */   public static final VersionInfo UNICODE_3_1_0;
/*     */   public static final VersionInfo UNICODE_3_1_1;
/*     */   public static final VersionInfo UNICODE_3_2;
/*     */   
/* 520 */   private static int getInt(int major, int minor, int milli, int micro) { return major << 24 | minor << 16 | milli << 8 | micro; }
/*     */   
/*     */   public static final VersionInfo UNICODE_4_0;
/*     */   public static final VersionInfo UNICODE_4_0_1;
/*     */   public static final VersionInfo UNICODE_4_1;
/*     */   public static final VersionInfo UNICODE_5_0;
/*     */   public static final VersionInfo UNICODE_5_1;
/*     */   public static final VersionInfo UNICODE_5_2;
/*     */   public static final VersionInfo UNICODE_6_0;
/*     */   public static void main(String[] args) {
/*     */     String icuApiVer;
/*     */     String icuApiVer;
/* 532 */     if (ICU_VERSION.getMinor() % 2 != 0)
/*     */     {
/* 534 */       int major = ICU_VERSION.getMajor();
/* 535 */       int minor = ICU_VERSION.getMinor() + 1;
/* 536 */       if (minor >= 10) {
/* 537 */         minor -= 10;
/* 538 */         major++;
/*     */       }
/* 540 */       icuApiVer = "" + major + "." + minor + "M" + ICU_VERSION.getMilli();
/*     */     } else {
/* 542 */       icuApiVer = ICU_VERSION.getVersionString(2, 2);
/*     */     }
/*     */     
/* 545 */     System.out.println("International Component for Unicode for Java " + icuApiVer);
/*     */     
/* 547 */     System.out.println("");
/* 548 */     System.out.println("Implementation Version: " + ICU_VERSION.getVersionString(2, 4));
/* 549 */     System.out.println("Unicode Data Version:   " + UNICODE_VERSION.getVersionString(2, 4));
/* 550 */     System.out.println("CLDR Data Version:      " + LocaleData.getCLDRVersion().getVersionString(2, 4));
/* 551 */     System.out.println("Time Zone Data Version: " + TimeZone.getTZDataVersion());
/*     */   }
/*     */   
/*     */ 
/*     */   public static final VersionInfo ICU_VERSION;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final String ICU_DATA_VERSION_PATH = "48b";
/*     */   
/*     */   private String getVersionString(int minDigits, int maxDigits)
/*     */   {
/* 563 */     if ((minDigits < 1) || (maxDigits < 1) || (minDigits > 4) || (maxDigits > 4) || (minDigits > maxDigits))
/*     */     {
/* 565 */       throw new IllegalArgumentException("Invalid min/maxDigits range");
/*     */     }
/*     */     
/* 568 */     int[] digits = new int[4];
/* 569 */     digits[0] = getMajor();
/* 570 */     digits[1] = getMinor();
/* 571 */     digits[2] = getMilli();
/* 572 */     digits[3] = getMicro();
/*     */     
/* 574 */     int numDigits = maxDigits;
/* 575 */     while ((numDigits > minDigits) && 
/* 576 */       (digits[(numDigits - 1)] == 0))
/*     */     {
/*     */ 
/* 579 */       numDigits--;
/*     */     }
/*     */     
/* 582 */     StringBuilder verStr = new StringBuilder(7);
/* 583 */     verStr.append(digits[0]);
/* 584 */     for (int i = 1; i < numDigits; i++) {
/* 585 */       verStr.append(".");
/* 586 */       verStr.append(digits[i]);
/*     */     }
/*     */     
/* 589 */     return verStr.toString();
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final VersionInfo ICU_DATA_VERSION;
/*     */   private static VersionInfo javaVersion;
/*     */   private static final VersionInfo UNICODE_VERSION;
/*     */   private int m_version_;
/*     */   private static final int LAST_BYTE_MASK_ = 255;
/*     */   private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\VersionInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */