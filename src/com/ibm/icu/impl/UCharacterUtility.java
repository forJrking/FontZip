/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UCharacterUtility
/*     */ {
/*     */   private static final int NON_CHARACTER_SUFFIX_MIN_3_0_ = 65534;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int NON_CHARACTER_MIN_3_1_ = 64976;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int NON_CHARACTER_MAX_3_1_ = 65007;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isNonCharacter(int ch)
/*     */   {
/*  27 */     if ((ch & 0xFFFE) == 65534)
/*     */     {
/*  29 */       return true;
/*     */     }
/*     */     
/*  32 */     return (ch >= 64976) && (ch <= 65007);
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
/*     */   static int toInt(char msc, char lsc)
/*     */   {
/*  45 */     return msc << '\020' | lsc;
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
/*     */   static int getNullTermByteSubString(StringBuffer str, byte[] array, int index)
/*     */   {
/*  62 */     byte b = 1;
/*     */     
/*  64 */     while (b != 0)
/*     */     {
/*  66 */       b = array[index];
/*  67 */       if (b != 0) {
/*  68 */         str.append((char)(b & 0xFF));
/*     */       }
/*  70 */       index++;
/*     */     }
/*  72 */     return index;
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
/*     */   static int compareNullTermByteSubString(String str, byte[] array, int strindex, int aindex)
/*     */   {
/*  90 */     byte b = 1;
/*  91 */     int length = str.length();
/*     */     
/*  93 */     while (b != 0)
/*     */     {
/*  95 */       b = array[aindex];
/*  96 */       aindex++;
/*  97 */       if (b == 0) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 102 */       if ((strindex == length) || (str.charAt(strindex) != (char)(b & 0xFF)))
/*     */       {
/* 104 */         return -1;
/*     */       }
/* 106 */       strindex++;
/*     */     }
/* 108 */     return strindex;
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
/*     */   static int skipNullTermByteSubString(byte[] array, int index, int skipcount)
/*     */   {
/* 125 */     for (int i = 0; i < skipcount; i++)
/*     */     {
/* 127 */       byte b = 1;
/* 128 */       while (b != 0)
/*     */       {
/* 130 */         b = array[index];
/* 131 */         index++;
/*     */       }
/*     */     }
/* 134 */     return index;
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
/*     */   static int skipByteSubString(byte[] array, int index, int length, byte skipend)
/*     */   {
/* 154 */     for (int result = 0; result < length; result++)
/*     */     {
/* 156 */       byte b = array[(index + result)];
/* 157 */       if (b == skipend)
/*     */       {
/* 159 */         result++;
/* 160 */         break;
/*     */       }
/*     */     }
/*     */     
/* 164 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCharacterUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */