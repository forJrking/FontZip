/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UCharacterName
/*      */ {
/*      */   public static final UCharacterName INSTANCE;
/*      */   public static final int LINES_PER_GROUP_ = 32;
/*      */   
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*   46 */       INSTANCE = new UCharacterName();
/*      */     }
/*      */     catch (IOException e) {
/*   49 */       throw new MissingResourceException("Could not construct UCharacterName. Missing unames.icu", "", "");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   62 */   public int m_groupcount_ = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName(int ch, int choice)
/*      */   {
/*   80 */     if ((ch < 0) || (ch > 1114111) || (choice > 4))
/*      */     {
/*   82 */       return null;
/*      */     }
/*      */     
/*   85 */     String result = null;
/*      */     
/*   87 */     result = getAlgName(ch, choice);
/*      */     
/*      */ 
/*   90 */     if ((result == null) || (result.length() == 0)) {
/*   91 */       if (choice == 2) {
/*   92 */         result = getExtendedName(ch);
/*      */       } else {
/*   94 */         result = getGroupName(ch, choice);
/*      */       }
/*      */     }
/*      */     
/*   98 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCharFromName(int choice, String name)
/*      */   {
/*  111 */     if ((choice >= 4) || (name == null) || (name.length() == 0))
/*      */     {
/*  113 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  117 */     int result = getExtendedChar(name.toLowerCase(), choice);
/*  118 */     if (result >= -1) {
/*  119 */       return result;
/*      */     }
/*      */     
/*  122 */     String upperCaseName = name.toUpperCase();
/*      */     
/*      */ 
/*      */ 
/*  126 */     if ((choice == 0) || (choice == 2))
/*      */     {
/*      */ 
/*  129 */       int count = 0;
/*  130 */       if (this.m_algorithm_ != null) {
/*  131 */         count = this.m_algorithm_.length;
/*      */       }
/*  133 */       for (count--; count >= 0; count--) {
/*  134 */         result = this.m_algorithm_[count].getChar(upperCaseName);
/*  135 */         if (result >= 0) {
/*  136 */           return result;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  141 */     if (choice == 2) {
/*  142 */       result = getGroupChar(upperCaseName, 0);
/*      */       
/*  144 */       if (result == -1) {
/*  145 */         result = getGroupChar(upperCaseName, 1);
/*      */       }
/*      */       
/*  148 */       if (result == -1) {
/*  149 */         result = getGroupChar(upperCaseName, 3);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  154 */       result = getGroupChar(upperCaseName, choice);
/*      */     }
/*  156 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGroupLengths(int index, char[] offsets, char[] lengths)
/*      */   {
/*  179 */     char length = 65535;
/*  180 */     byte b = 0;
/*  181 */     byte n = 0;
/*      */     
/*  183 */     index *= this.m_groupsize_;
/*  184 */     int stringoffset = UCharacterUtility.toInt(this.m_groupinfo_[(index + 1)], this.m_groupinfo_[(index + 2)]);
/*      */     
/*      */ 
/*      */ 
/*  188 */     offsets[0] = '\000';
/*      */     
/*      */ 
/*      */ 
/*  192 */     for (int i = 0; i < 32; stringoffset++) {
/*  193 */       b = this.m_groupstring_[stringoffset];
/*  194 */       int shift = 4;
/*      */       
/*  196 */       while (shift >= 0)
/*      */       {
/*  198 */         n = (byte)(b >> shift & 0xF);
/*  199 */         if ((length == 65535) && (n > 11)) {
/*  200 */           length = (char)(n - 12 << 4);
/*      */         }
/*      */         else {
/*  203 */           if (length != 65535) {
/*  204 */             lengths[i] = ((char)((length | n) + '\f'));
/*      */           }
/*      */           else {
/*  207 */             lengths[i] = ((char)n);
/*      */           }
/*      */           
/*  210 */           if (i < 32) {
/*  211 */             offsets[(i + 1)] = ((char)(offsets[i] + lengths[i]));
/*      */           }
/*      */           
/*  214 */           length = 65535;
/*  215 */           i++;
/*      */         }
/*      */         
/*  218 */         shift -= 4;
/*      */       }
/*      */     }
/*  221 */     return stringoffset;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getGroupName(int index, int length, int choice)
/*      */   {
/*  240 */     if ((choice != 0) && (choice != 2))
/*      */     {
/*      */ 
/*  243 */       if ((59 >= this.m_tokentable_.length) || (this.m_tokentable_[59] == 65535))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  248 */         int fieldIndex = choice == 4 ? 2 : choice;
/*      */         do {
/*  250 */           int oldindex = index;
/*  251 */           index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, length, (byte)59);
/*      */           
/*  253 */           length -= index - oldindex;
/*  254 */           fieldIndex--; } while (fieldIndex > 0);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  260 */         length = 0;
/*      */       }
/*      */     }
/*      */     
/*  264 */     synchronized (this.m_utilStringBuffer_) {
/*  265 */       this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*      */       
/*      */ 
/*  268 */       for (int i = 0; i < length;) {
/*  269 */         byte b = this.m_groupstring_[(index + i)];
/*  270 */         i++;
/*      */         
/*  272 */         if (b >= this.m_tokentable_.length) {
/*  273 */           if (b == 59) {
/*      */             break;
/*      */           }
/*  276 */           this.m_utilStringBuffer_.append(b);
/*      */         }
/*      */         else {
/*  279 */           char token = this.m_tokentable_[(b & 0xFF)];
/*  280 */           if (token == 65534)
/*      */           {
/*  282 */             token = this.m_tokentable_[(b << 8 | this.m_groupstring_[(index + i)] & 0xFF)];
/*      */             
/*  284 */             i++;
/*      */           }
/*  286 */           if (token == 65535) {
/*  287 */             if (b == 59)
/*      */             {
/*      */ 
/*      */ 
/*  291 */               if ((this.m_utilStringBuffer_.length() != 0) || (choice != 2))
/*      */               {
/*      */                 break;
/*      */               }
/*      */               
/*      */             }
/*      */             else {
/*  298 */               this.m_utilStringBuffer_.append((char)(b & 0xFF));
/*      */             }
/*      */           } else {
/*  301 */             UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  307 */       if (this.m_utilStringBuffer_.length() > 0) {
/*  308 */         return this.m_utilStringBuffer_.toString();
/*      */       }
/*      */     }
/*  311 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getExtendedName(int ch)
/*      */   {
/*  319 */     String result = getName(ch, 0);
/*  320 */     if (result == null) {
/*  321 */       if (getType(ch) == 15) {
/*  322 */         result = getName(ch, 1);
/*      */       }
/*      */       
/*  325 */       if (result == null) {
/*  326 */         result = getExtendedOr10Name(ch);
/*      */       }
/*      */     }
/*  329 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGroup(int codepoint)
/*      */   {
/*  339 */     int endGroup = this.m_groupcount_;
/*  340 */     int msb = getCodepointMSB(codepoint);
/*  341 */     int result = 0;
/*      */     
/*      */ 
/*      */ 
/*  345 */     while (result < endGroup - 1) {
/*  346 */       int gindex = result + endGroup >> 1;
/*  347 */       if (msb < getGroupMSB(gindex)) {
/*  348 */         endGroup = gindex;
/*      */       }
/*      */       else {
/*  351 */         result = gindex;
/*      */       }
/*      */     }
/*  354 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getExtendedOr10Name(int ch)
/*      */   {
/*  365 */     String result = null;
/*  366 */     if (getType(ch) == 15) {
/*  367 */       result = getName(ch, 1);
/*      */     }
/*      */     
/*  370 */     if (result == null) {
/*  371 */       int type = getType(ch);
/*      */       
/*      */ 
/*  374 */       if (type >= TYPE_NAMES_.length) {
/*  375 */         result = "unknown";
/*      */       }
/*      */       else {
/*  378 */         result = TYPE_NAMES_[type];
/*      */       }
/*  380 */       synchronized (this.m_utilStringBuffer_) {
/*  381 */         this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*  382 */         this.m_utilStringBuffer_.append('<');
/*  383 */         this.m_utilStringBuffer_.append(result);
/*  384 */         this.m_utilStringBuffer_.append('-');
/*  385 */         String chStr = Integer.toHexString(ch).toUpperCase();
/*  386 */         int zeros = 4 - chStr.length();
/*  387 */         while (zeros > 0) {
/*  388 */           this.m_utilStringBuffer_.append('0');
/*  389 */           zeros--;
/*      */         }
/*  391 */         this.m_utilStringBuffer_.append(chStr);
/*  392 */         this.m_utilStringBuffer_.append('>');
/*  393 */         result = this.m_utilStringBuffer_.toString();
/*      */       }
/*      */     }
/*  396 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getGroupMSB(int gindex)
/*      */   {
/*  406 */     if (gindex >= this.m_groupcount_) {
/*  407 */       return -1;
/*      */     }
/*  409 */     return this.m_groupinfo_[(gindex * this.m_groupsize_)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getCodepointMSB(int codepoint)
/*      */   {
/*  419 */     return codepoint >> 5;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getGroupLimit(int msb)
/*      */   {
/*  429 */     return (msb << 5) + 32;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getGroupMin(int msb)
/*      */   {
/*  439 */     return msb << 5;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getGroupOffset(int codepoint)
/*      */   {
/*  449 */     return codepoint & 0x1F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getGroupMinFromCodepoint(int codepoint)
/*      */   {
/*  460 */     return codepoint & 0xFFFFFFE0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getAlgorithmLength()
/*      */   {
/*  470 */     return this.m_algorithm_.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getAlgorithmStart(int index)
/*      */   {
/*  480 */     return this.m_algorithm_[index].m_rangestart_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getAlgorithmEnd(int index)
/*      */   {
/*  490 */     return this.m_algorithm_[index].m_rangeend_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAlgorithmName(int index, int codepoint)
/*      */   {
/*  501 */     String result = null;
/*  502 */     synchronized (this.m_utilStringBuffer_) {
/*  503 */       this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*  504 */       this.m_algorithm_[index].appendName(codepoint, this.m_utilStringBuffer_);
/*  505 */       result = this.m_utilStringBuffer_.toString();
/*      */     }
/*  507 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized String getGroupName(int ch, int choice)
/*      */   {
/*  518 */     int msb = getCodepointMSB(ch);
/*  519 */     int group = getGroup(ch);
/*      */     
/*      */ 
/*  522 */     if (msb == this.m_groupinfo_[(group * this.m_groupsize_)]) {
/*  523 */       int index = getGroupLengths(group, this.m_groupoffsets_, this.m_grouplengths_);
/*      */       
/*  525 */       int offset = ch & 0x1F;
/*  526 */       return getGroupName(index + this.m_groupoffsets_[offset], this.m_grouplengths_[offset], choice);
/*      */     }
/*      */     
/*      */ 
/*  530 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCharNameLength()
/*      */   {
/*  542 */     if (initNameSetsLengths()) {
/*  543 */       return this.m_maxNameLength_;
/*      */     }
/*      */     
/*  546 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxISOCommentLength()
/*      */   {
/*  558 */     if (initNameSetsLengths()) {
/*  559 */       return this.m_maxISOCommentLength_;
/*      */     }
/*      */     
/*  562 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getCharNameCharacters(UnicodeSet set)
/*      */   {
/*  574 */     convert(this.m_nameSet_, set);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getISOCommentCharacters(UnicodeSet set)
/*      */   {
/*  585 */     convert(this.m_ISOCommentSet_, set);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final class AlgorithmName
/*      */   {
/*      */     static final int TYPE_0_ = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     static final int TYPE_1_ = 1;
/*      */     
/*      */ 
/*      */ 
/*      */     private int m_rangestart_;
/*      */     
/*      */ 
/*      */ 
/*      */     private int m_rangeend_;
/*      */     
/*      */ 
/*      */ 
/*      */     private byte m_type_;
/*      */     
/*      */ 
/*      */     private byte m_variant_;
/*      */     
/*      */ 
/*      */     private char[] m_factor_;
/*      */     
/*      */ 
/*      */     private String m_prefix_;
/*      */     
/*      */ 
/*      */     private byte[] m_factorstring_;
/*      */     
/*      */ 
/*      */ 
/*      */     boolean setInfo(int rangestart, int rangeend, byte type, byte variant)
/*      */     {
/*  627 */       if ((rangestart >= 0) && (rangestart <= rangeend) && (rangeend <= 1114111) && ((type == 0) || (type == 1)))
/*      */       {
/*      */ 
/*  630 */         this.m_rangestart_ = rangestart;
/*  631 */         this.m_rangeend_ = rangeend;
/*  632 */         this.m_type_ = type;
/*  633 */         this.m_variant_ = variant;
/*  634 */         return true;
/*      */       }
/*  636 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean setFactor(char[] factor)
/*      */     {
/*  646 */       if (factor.length == this.m_variant_) {
/*  647 */         this.m_factor_ = factor;
/*  648 */         return true;
/*      */       }
/*  650 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean setPrefix(String prefix)
/*      */     {
/*  660 */       if ((prefix != null) && (prefix.length() > 0)) {
/*  661 */         this.m_prefix_ = prefix;
/*  662 */         return true;
/*      */       }
/*  664 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean setFactorString(byte[] string)
/*      */     {
/*  676 */       this.m_factorstring_ = string;
/*  677 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean contains(int ch)
/*      */     {
/*  686 */       return (this.m_rangestart_ <= ch) && (ch <= this.m_rangeend_);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     void appendName(int ch, StringBuffer str)
/*      */     {
/*  698 */       str.append(this.m_prefix_);
/*  699 */       switch (this.m_type_)
/*      */       {
/*      */ 
/*      */       case 0: 
/*  703 */         str.append(Utility.hex(ch, this.m_variant_));
/*  704 */         break;
/*      */       
/*      */       case 1: 
/*  707 */         int offset = ch - this.m_rangestart_;
/*  708 */         int[] indexes = this.m_utilIntBuffer_;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  714 */         synchronized (this.m_utilIntBuffer_) {
/*  715 */           for (int i = this.m_variant_ - 1; i > 0; i--)
/*      */           {
/*  717 */             int factor = this.m_factor_[i] & 0xFF;
/*  718 */             indexes[i] = (offset % factor);
/*  719 */             offset /= factor;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  725 */           indexes[0] = offset;
/*      */           
/*      */ 
/*  728 */           str.append(getFactorString(indexes, this.m_variant_));
/*      */         }
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int getChar(String name)
/*      */     {
/*  740 */       int prefixlen = this.m_prefix_.length();
/*  741 */       if ((name.length() < prefixlen) || (!this.m_prefix_.equals(name.substring(0, prefixlen))))
/*      */       {
/*  743 */         return -1;
/*      */       }
/*      */       
/*  746 */       switch (this.m_type_)
/*      */       {
/*      */       case 0: 
/*      */         try
/*      */         {
/*  751 */           int result = Integer.parseInt(name.substring(prefixlen), 16);
/*      */           
/*      */ 
/*  754 */           if ((this.m_rangestart_ <= result) && (result <= this.m_rangeend_)) {
/*  755 */             return result;
/*      */           }
/*      */         }
/*      */         catch (NumberFormatException e)
/*      */         {
/*  760 */           return -1;
/*      */         }
/*      */       
/*      */ 
/*      */ 
/*      */       case 1: 
/*  766 */         for (int ch = this.m_rangestart_; ch <= this.m_rangeend_; ch++)
/*      */         {
/*  768 */           int offset = ch - this.m_rangestart_;
/*  769 */           int[] indexes = this.m_utilIntBuffer_;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */           synchronized (this.m_utilIntBuffer_) {
/*  776 */             for (int i = this.m_variant_ - 1; i > 0; i--)
/*      */             {
/*  778 */               int factor = this.m_factor_[i] & 0xFF;
/*  779 */               indexes[i] = (offset % factor);
/*  780 */               offset /= factor;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  786 */             indexes[0] = offset;
/*      */             
/*      */ 
/*  789 */             if (compareFactorString(indexes, this.m_variant_, name, prefixlen))
/*      */             {
/*  791 */               return ch;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  797 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int add(int[] set, int maxlength)
/*      */     {
/*  811 */       int length = UCharacterName.add(set, this.m_prefix_);
/*  812 */       switch (this.m_type_)
/*      */       {
/*      */ 
/*      */       case 0: 
/*  816 */         length += this.m_variant_;
/*      */         
/*      */ 
/*      */ 
/*  820 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       case 1: 
/*  826 */         for (int i = this.m_variant_ - 1; i > 0; i--)
/*      */         {
/*  828 */           int maxfactorlength = 0;
/*  829 */           int count = 0;
/*  830 */           for (int factor = this.m_factor_[i]; factor > 0; factor--) {
/*  831 */             synchronized (this.m_utilStringBuffer_) {
/*  832 */               this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*      */               
/*  834 */               count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
/*      */               
/*      */ 
/*      */ 
/*  838 */               UCharacterName.add(set, this.m_utilStringBuffer_);
/*  839 */               if (this.m_utilStringBuffer_.length() > maxfactorlength)
/*      */               {
/*      */ 
/*  842 */                 maxfactorlength = this.m_utilStringBuffer_.length();
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  847 */           length += maxfactorlength;
/*      */         }
/*      */       }
/*      */       
/*  851 */       if (length > maxlength) {
/*  852 */         return length;
/*      */       }
/*  854 */       return maxlength;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  872 */     private StringBuffer m_utilStringBuffer_ = new StringBuffer();
/*      */     
/*      */ 
/*      */ 
/*  876 */     private int[] m_utilIntBuffer_ = new int['Ā'];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String getFactorString(int[] index, int length)
/*      */     {
/*  889 */       int size = this.m_factor_.length;
/*  890 */       if ((index == null) || (length != size)) {
/*  891 */         return null;
/*      */       }
/*      */       
/*  894 */       synchronized (this.m_utilStringBuffer_) {
/*  895 */         this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*  896 */         int count = 0;
/*      */         
/*  898 */         size--;
/*  899 */         for (int i = 0; i <= size; i++) {
/*  900 */           int factor = this.m_factor_[i];
/*  901 */           count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
/*      */           
/*  903 */           count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
/*      */           
/*      */ 
/*  906 */           if (i != size) {
/*  907 */             count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i] - 1);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  912 */         return this.m_utilStringBuffer_.toString();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean compareFactorString(int[] index, int length, String str, int offset)
/*      */     {
/*  928 */       int size = this.m_factor_.length;
/*  929 */       if ((index == null) || (length != size)) {
/*  930 */         return false;
/*      */       }
/*  932 */       int count = 0;
/*  933 */       int strcount = offset;
/*      */       
/*  935 */       size--;
/*  936 */       for (int i = 0; i <= size; i++)
/*      */       {
/*  938 */         int factor = this.m_factor_[i];
/*  939 */         count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
/*      */         
/*  941 */         strcount = UCharacterUtility.compareNullTermByteSubString(str, this.m_factorstring_, strcount, count);
/*      */         
/*  943 */         if (strcount < 0) {
/*  944 */           return false;
/*      */         }
/*      */         
/*  947 */         if (i != size) {
/*  948 */           count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i]);
/*      */         }
/*      */       }
/*      */       
/*  952 */       if (strcount != str.length()) {
/*  953 */         return false;
/*      */       }
/*  955 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  964 */   int m_groupsize_ = 0;
/*      */   
/*      */   private char[] m_tokentable_;
/*      */   
/*      */   private byte[] m_tokenstring_;
/*      */   
/*      */   private char[] m_groupinfo_;
/*      */   private byte[] m_groupstring_;
/*      */   private AlgorithmName[] m_algorithm_;
/*      */   
/*      */   boolean setToken(char[] token, byte[] tokenstring)
/*      */   {
/*  976 */     if ((token != null) && (tokenstring != null) && (token.length > 0) && (tokenstring.length > 0))
/*      */     {
/*  978 */       this.m_tokentable_ = token;
/*  979 */       this.m_tokenstring_ = tokenstring;
/*  980 */       return true;
/*      */     }
/*  982 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean setAlgorithm(AlgorithmName[] alg)
/*      */   {
/*  992 */     if ((alg != null) && (alg.length != 0)) {
/*  993 */       this.m_algorithm_ = alg;
/*  994 */       return true;
/*      */     }
/*  996 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean setGroupCountSize(int count, int size)
/*      */   {
/* 1007 */     if ((count <= 0) || (size <= 0)) {
/* 1008 */       return false;
/*      */     }
/* 1010 */     this.m_groupcount_ = count;
/* 1011 */     this.m_groupsize_ = size;
/* 1012 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean setGroup(char[] group, byte[] groupstring)
/*      */   {
/* 1023 */     if ((group != null) && (groupstring != null) && (group.length > 0) && (groupstring.length > 0))
/*      */     {
/* 1025 */       this.m_groupinfo_ = group;
/* 1026 */       this.m_groupstring_ = groupstring;
/* 1027 */       return true;
/*      */     }
/* 1029 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1046 */   private char[] m_groupoffsets_ = new char[33];
/* 1047 */   private char[] m_grouplengths_ = new char[33];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String NAME_FILE_NAME_ = "data/icudt48b/unames.icu";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int GROUP_SHIFT_ = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int GROUP_MASK_ = 31;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int NAME_BUFFER_SIZE_ = 100000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int OFFSET_HIGH_OFFSET_ = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int OFFSET_LOW_OFFSET_ = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int SINGLE_NIBBLE_MAX_ = 11;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1094 */   private int[] m_nameSet_ = new int[8];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1099 */   private int[] m_ISOCommentSet_ = new int[8];
/*      */   
/*      */ 
/*      */ 
/* 1103 */   private StringBuffer m_utilStringBuffer_ = new StringBuffer();
/*      */   
/*      */ 
/*      */ 
/* 1107 */   private int[] m_utilIntBuffer_ = new int[2];
/*      */   
/*      */ 
/*      */ 
/*      */   private int m_maxISOCommentLength_;
/*      */   
/*      */ 
/*      */ 
/*      */   private int m_maxNameLength_;
/*      */   
/*      */ 
/*      */ 
/* 1119 */   private static final String[] TYPE_NAMES_ = { "unassigned", "uppercase letter", "lowercase letter", "titlecase letter", "modifier letter", "other letter", "non spacing mark", "enclosing mark", "combining spacing mark", "decimal digit number", "letter number", "other number", "space separator", "line separator", "paragraph separator", "control", "format", "private use area", "surrogate", "dash punctuation", "start punctuation", "end punctuation", "connector punctuation", "other punctuation", "math symbol", "currency symbol", "modifier symbol", "other symbol", "initial punctuation", "final punctuation", "noncharacter", "lead surrogate", "trail surrogate" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String UNKNOWN_TYPE_NAME_ = "unknown";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int NON_CHARACTER_ = 30;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int LEAD_SURROGATE_ = 31;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int TRAIL_SURROGATE_ = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int EXTENDED_CATEGORY_ = 33;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UCharacterName()
/*      */     throws IOException
/*      */   {
/* 1185 */     InputStream is = ICUData.getRequiredStream("data/icudt48b/unames.icu");
/* 1186 */     BufferedInputStream b = new BufferedInputStream(is, 100000);
/* 1187 */     UCharacterNameReader reader = new UCharacterNameReader(b);
/* 1188 */     reader.read(this);
/* 1189 */     b.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getAlgName(int ch, int choice)
/*      */   {
/* 1203 */     if ((choice == 0) || (choice == 2))
/*      */     {
/*      */ 
/*      */ 
/* 1207 */       synchronized (this.m_utilStringBuffer_) {
/* 1208 */         this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*      */         
/* 1210 */         for (int index = this.m_algorithm_.length - 1; index >= 0; index--)
/*      */         {
/* 1212 */           if (this.m_algorithm_[index].contains(ch)) {
/* 1213 */             this.m_algorithm_[index].appendName(ch, this.m_utilStringBuffer_);
/* 1214 */             return this.m_utilStringBuffer_.toString();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1219 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized int getGroupChar(String name, int choice)
/*      */   {
/* 1230 */     for (int i = 0; i < this.m_groupcount_; i++)
/*      */     {
/*      */ 
/* 1233 */       int startgpstrindex = getGroupLengths(i, this.m_groupoffsets_, this.m_grouplengths_);
/*      */       
/*      */ 
/*      */ 
/* 1237 */       int result = getGroupChar(startgpstrindex, this.m_grouplengths_, name, choice);
/*      */       
/* 1239 */       if (result != -1) {
/* 1240 */         return this.m_groupinfo_[(i * this.m_groupsize_)] << '\005' | result;
/*      */       }
/*      */     }
/*      */     
/* 1244 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getGroupChar(int index, char[] length, String name, int choice)
/*      */   {
/* 1260 */     byte b = 0;
/*      */     
/*      */ 
/* 1263 */     int namelen = name.length();
/*      */     
/*      */ 
/*      */ 
/* 1267 */     for (int result = 0; result <= 32; result++) {
/* 1268 */       int nindex = 0;
/* 1269 */       int len = length[result];
/*      */       
/* 1271 */       if ((choice != 0) && (choice != 2))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1278 */         int fieldIndex = choice == 4 ? 2 : choice;
/*      */         do {
/* 1280 */           int oldindex = index;
/* 1281 */           index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, len, (byte)59);
/*      */           
/* 1283 */           len -= index - oldindex;
/* 1284 */           fieldIndex--; } while (fieldIndex > 0);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1289 */       for (int count = 0; (count < len) && (nindex != -1) && (nindex < namelen);)
/*      */       {
/* 1291 */         b = this.m_groupstring_[(index + count)];
/* 1292 */         count++;
/*      */         
/* 1294 */         if (b >= this.m_tokentable_.length) {
/* 1295 */           if (name.charAt(nindex++) != (b & 0xFF)) {
/* 1296 */             nindex = -1;
/*      */           }
/*      */         }
/*      */         else {
/* 1300 */           char token = this.m_tokentable_[(b & 0xFF)];
/* 1301 */           if (token == 65534)
/*      */           {
/* 1303 */             token = this.m_tokentable_[(b << 8 | this.m_groupstring_[(index + count)] & 0xFF)];
/*      */             
/* 1305 */             count++;
/*      */           }
/* 1307 */           if (token == 65535) {
/* 1308 */             if (name.charAt(nindex++) != (b & 0xFF)) {
/* 1309 */               nindex = -1;
/*      */             }
/*      */             
/*      */           }
/*      */           else {
/* 1314 */             nindex = UCharacterUtility.compareNullTermByteSubString(name, this.m_tokenstring_, nindex, token);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1320 */       if ((namelen == nindex) && ((count == len) || (this.m_groupstring_[(index + count)] == 59)))
/*      */       {
/* 1322 */         return result;
/*      */       }
/*      */       
/* 1325 */       index += len;
/*      */     }
/* 1327 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getType(int ch)
/*      */   {
/* 1337 */     if (UCharacterUtility.isNonCharacter(ch))
/*      */     {
/* 1339 */       return 30;
/*      */     }
/* 1341 */     int result = UCharacter.getType(ch);
/* 1342 */     if (result == 18) {
/* 1343 */       if (ch <= 56319) {
/* 1344 */         result = 31;
/*      */       }
/*      */       else {
/* 1347 */         result = 32;
/*      */       }
/*      */     }
/* 1350 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getExtendedChar(String name, int choice)
/*      */   {
/* 1362 */     if (name.charAt(0) == '<') {
/* 1363 */       if (choice == 2) {
/* 1364 */         int endIndex = name.length() - 1;
/* 1365 */         if (name.charAt(endIndex) == '>') {
/* 1366 */           int startIndex = name.lastIndexOf('-');
/* 1367 */           if (startIndex >= 0) {
/* 1368 */             startIndex++;
/* 1369 */             int result = -1;
/*      */             try {
/* 1371 */               result = Integer.parseInt(name.substring(startIndex, endIndex), 16);
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException e)
/*      */             {
/* 1376 */               return -1;
/*      */             }
/*      */             
/*      */ 
/* 1380 */             String type = name.substring(1, startIndex - 1);
/* 1381 */             int length = TYPE_NAMES_.length;
/* 1382 */             for (int i = 0; i < length; i++) {
/* 1383 */               if (type.compareTo(TYPE_NAMES_[i]) == 0) {
/* 1384 */                 if (getType(result) != i) break;
/* 1385 */                 return result;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1393 */       return -1;
/*      */     }
/* 1395 */     return -2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void add(int[] set, char ch)
/*      */   {
/* 1408 */     set[(ch >>> '\005')] |= '\001' << (ch & 0x1F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean contains(int[] set, char ch)
/*      */   {
/* 1420 */     return (set[(ch >>> '\005')] & '\001' << (ch & 0x1F)) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int add(int[] set, String str)
/*      */   {
/* 1431 */     int result = str.length();
/*      */     
/* 1433 */     for (int i = result - 1; i >= 0; i--) {
/* 1434 */       add(set, str.charAt(i));
/*      */     }
/* 1436 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int add(int[] set, StringBuffer str)
/*      */   {
/* 1447 */     int result = str.length();
/*      */     
/* 1449 */     for (int i = result - 1; i >= 0; i--) {
/* 1450 */       add(set, str.charAt(i));
/*      */     }
/* 1452 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int addAlgorithmName(int maxlength)
/*      */   {
/* 1464 */     int result = 0;
/* 1465 */     for (int i = this.m_algorithm_.length - 1; i >= 0; i--) {
/* 1466 */       result = this.m_algorithm_[i].add(this.m_nameSet_, maxlength);
/* 1467 */       if (result > maxlength) {
/* 1468 */         maxlength = result;
/*      */       }
/*      */     }
/* 1471 */     return maxlength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int addExtendedName(int maxlength)
/*      */   {
/* 1482 */     for (int i = TYPE_NAMES_.length - 1; i >= 0; i--)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1488 */       int length = 9 + add(this.m_nameSet_, TYPE_NAMES_[i]);
/* 1489 */       if (length > maxlength) {
/* 1490 */         maxlength = length;
/*      */       }
/*      */     }
/* 1493 */     return maxlength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] addGroupName(int offset, int length, byte[] tokenlength, int[] set)
/*      */   {
/* 1509 */     int resultnlength = 0;
/* 1510 */     int resultplength = 0;
/* 1511 */     while (resultplength < length) {
/* 1512 */       char b = (char)(this.m_groupstring_[(offset + resultplength)] & 0xFF);
/* 1513 */       resultplength++;
/* 1514 */       if (b == ';') {
/*      */         break;
/*      */       }
/*      */       
/* 1518 */       if (b >= this.m_tokentable_.length) {
/* 1519 */         add(set, b);
/* 1520 */         resultnlength++;
/*      */       }
/*      */       else {
/* 1523 */         char token = this.m_tokentable_[(b & 0xFF)];
/* 1524 */         if (token == 65534)
/*      */         {
/* 1526 */           b = (char)(b << '\b' | this.m_groupstring_[(offset + resultplength)] & 0xFF);
/*      */           
/* 1528 */           token = this.m_tokentable_[b];
/* 1529 */           resultplength++;
/*      */         }
/* 1531 */         if (token == 65535) {
/* 1532 */           add(set, b);
/* 1533 */           resultnlength++;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1538 */           byte tlength = tokenlength[b];
/* 1539 */           if (tlength == 0) {
/* 1540 */             synchronized (this.m_utilStringBuffer_) {
/* 1541 */               this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/*      */               
/* 1543 */               UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
/*      */               
/*      */ 
/* 1546 */               tlength = (byte)add(set, this.m_utilStringBuffer_);
/*      */             }
/* 1548 */             tokenlength[b] = tlength;
/*      */           }
/* 1550 */           resultnlength += tlength;
/*      */         }
/*      */       }
/*      */     }
/* 1554 */     this.m_utilIntBuffer_[0] = resultnlength;
/* 1555 */     this.m_utilIntBuffer_[1] = resultplength;
/* 1556 */     return this.m_utilIntBuffer_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void addGroupName(int maxlength)
/*      */   {
/* 1568 */     int maxisolength = 0;
/* 1569 */     char[] offsets = new char[34];
/* 1570 */     char[] lengths = new char[34];
/* 1571 */     byte[] tokenlengths = new byte[this.m_tokentable_.length];
/*      */     
/*      */ 
/*      */ 
/* 1575 */     for (int i = 0; i < this.m_groupcount_; i++) {
/* 1576 */       int offset = getGroupLengths(i, offsets, lengths);
/*      */       
/*      */ 
/*      */ 
/* 1580 */       for (int linenumber = 0; linenumber < 32; 
/* 1581 */           linenumber++) {
/* 1582 */         int lineoffset = offset + offsets[linenumber];
/* 1583 */         int length = lengths[linenumber];
/* 1584 */         if (length != 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1589 */           int[] parsed = addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
/*      */           
/* 1591 */           if (parsed[0] > maxlength)
/*      */           {
/* 1593 */             maxlength = parsed[0];
/*      */           }
/* 1595 */           lineoffset += parsed[1];
/* 1596 */           if (parsed[1] < length)
/*      */           {
/*      */ 
/*      */ 
/* 1600 */             length -= parsed[1];
/*      */             
/* 1602 */             parsed = addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
/*      */             
/* 1604 */             if (parsed[0] > maxlength)
/*      */             {
/* 1606 */               maxlength = parsed[0];
/*      */             }
/* 1608 */             lineoffset += parsed[1];
/* 1609 */             if (parsed[1] < length)
/*      */             {
/*      */ 
/*      */ 
/* 1613 */               length -= parsed[1];
/*      */               
/* 1615 */               parsed = addGroupName(lineoffset, length, tokenlengths, this.m_ISOCommentSet_);
/*      */               
/* 1617 */               if (parsed[1] > maxisolength)
/* 1618 */                 maxisolength = length;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1624 */     this.m_maxISOCommentLength_ = maxisolength;
/* 1625 */     this.m_maxNameLength_ = maxlength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean initNameSetsLengths()
/*      */   {
/* 1634 */     if (this.m_maxNameLength_ > 0) {
/* 1635 */       return true;
/*      */     }
/*      */     
/* 1638 */     String extra = "0123456789ABCDEF<>-";
/*      */     
/*      */ 
/* 1641 */     for (int i = extra.length() - 1; i >= 0; i--) {
/* 1642 */       add(this.m_nameSet_, extra.charAt(i));
/*      */     }
/*      */     
/*      */ 
/* 1646 */     this.m_maxNameLength_ = addAlgorithmName(0);
/*      */     
/* 1648 */     this.m_maxNameLength_ = addExtendedName(this.m_maxNameLength_);
/*      */     
/* 1650 */     addGroupName(this.m_maxNameLength_);
/* 1651 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void convert(int[] set, UnicodeSet uset)
/*      */   {
/* 1662 */     uset.clear();
/* 1663 */     if (!initNameSetsLengths()) {
/* 1664 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1668 */     for (char c = 'ÿ'; c > 0; c = (char)(c - '\001')) {
/* 1669 */       if (contains(set, c)) {
/* 1670 */         uset.add(c);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCharacterName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */