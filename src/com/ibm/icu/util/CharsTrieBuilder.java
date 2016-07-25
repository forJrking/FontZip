/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharsTrieBuilder
/*     */   extends StringTrieBuilder
/*     */ {
/*     */   public CharsTrieBuilder add(CharSequence s, int value)
/*     */   {
/*  44 */     addImpl(s, value);
/*  45 */     return this;
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
/*     */   public CharsTrie build(StringTrieBuilder.Option buildOption)
/*     */   {
/*  61 */     return new CharsTrie(buildCharSequence(buildOption), 0);
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
/*     */   public CharSequence buildCharSequence(StringTrieBuilder.Option buildOption)
/*     */   {
/*  77 */     buildChars(buildOption);
/*  78 */     return CharBuffer.wrap(this.chars, this.chars.length - this.charsLength, this.charsLength);
/*     */   }
/*     */   
/*     */   private void buildChars(StringTrieBuilder.Option buildOption)
/*     */   {
/*  83 */     if (this.chars == null) {
/*  84 */       this.chars = new char['Ѐ'];
/*     */     }
/*  86 */     buildImpl(buildOption);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharsTrieBuilder clear()
/*     */   {
/*  97 */     clearImpl();
/*  98 */     this.chars = null;
/*  99 */     this.charsLength = 0;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected boolean matchNodesCanHaveValues()
/*     */   {
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getMaxBranchLinearSubNodeLength()
/*     */   {
/* 117 */     return 5;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getMinLinearMatch() {
/* 124 */     return 48;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/* 131 */   protected int getMaxLinearMatchLength() { return 16; }
/*     */   
/*     */   private void ensureCapacity(int length) {
/* 134 */     if (length > this.chars.length) {
/* 135 */       int newCapacity = this.chars.length;
/*     */       do {
/* 137 */         newCapacity *= 2;
/* 138 */       } while (newCapacity <= length);
/* 139 */       char[] newChars = new char[newCapacity];
/* 140 */       System.arraycopy(this.chars, this.chars.length - this.charsLength, newChars, newChars.length - this.charsLength, this.charsLength);
/*     */       
/* 142 */       this.chars = newChars;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int write(int unit)
/*     */   {
/* 152 */     int newLength = this.charsLength + 1;
/* 153 */     ensureCapacity(newLength);
/* 154 */     this.charsLength = newLength;
/* 155 */     this.chars[(this.chars.length - this.charsLength)] = ((char)unit);
/* 156 */     return this.charsLength;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int write(int offset, int length)
/*     */   {
/* 165 */     int newLength = this.charsLength + length;
/* 166 */     ensureCapacity(newLength);
/* 167 */     this.charsLength = newLength;
/* 168 */     int charsOffset = this.chars.length - this.charsLength;
/* 169 */     while (length > 0) {
/* 170 */       this.chars[(charsOffset++)] = this.strings.charAt(offset++);
/* 171 */       length--;
/*     */     }
/* 173 */     return this.charsLength;
/*     */   }
/*     */   
/* 176 */   private int write(char[] s, int length) { int newLength = this.charsLength + length;
/* 177 */     ensureCapacity(newLength);
/* 178 */     this.charsLength = newLength;
/* 179 */     System.arraycopy(s, 0, this.chars, this.chars.length - this.charsLength, length);
/* 180 */     return this.charsLength;
/*     */   }
/*     */   
/*     */ 
/* 184 */   private final char[] intUnits = new char[3];
/*     */   private char[] chars;
/*     */   private int charsLength;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeValueAndFinal(int i, boolean isFinal)
/*     */   {
/* 193 */     if ((0 <= i) && (i <= 16383))
/* 194 */       return write(i | (isFinal ? 32768 : 0));
/*     */     int length;
/*     */     int length;
/* 197 */     if ((i < 0) || (i > 1073676287)) {
/* 198 */       this.intUnits[0] = '翿';
/* 199 */       this.intUnits[1] = ((char)(i >> 16));
/* 200 */       this.intUnits[2] = ((char)i);
/* 201 */       length = 3;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 206 */       this.intUnits[0] = ((char)(16384 + (i >> 16)));
/* 207 */       this.intUnits[1] = ((char)i);
/* 208 */       length = 2;
/*     */     }
/* 210 */     this.intUnits[0] = ((char)(this.intUnits[0] | (isFinal ? 32768 : '\000')));
/* 211 */     return write(this.intUnits, length);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeValueAndType(boolean hasValue, int value, int node)
/*     */   {
/* 220 */     if (!hasValue)
/* 221 */       return write(node);
/*     */     int length;
/*     */     int length;
/* 224 */     if ((value < 0) || (value > 16646143)) {
/* 225 */       this.intUnits[0] = '翀';
/* 226 */       this.intUnits[1] = ((char)(value >> 16));
/* 227 */       this.intUnits[2] = ((char)value);
/* 228 */       length = 3; } else { int length;
/* 229 */       if (value <= 255) {
/* 230 */         this.intUnits[0] = ((char)(value + 1 << 6));
/* 231 */         length = 1;
/*     */       } else {
/* 233 */         this.intUnits[0] = ((char)(16448 + (value >> 10 & 0x7FC0)));
/* 234 */         this.intUnits[1] = ((char)value);
/* 235 */         length = 2;
/*     */       } }
/* 237 */     int tmp115_114 = 0; char[] tmp115_111 = this.intUnits;tmp115_111[tmp115_114] = ((char)(tmp115_111[tmp115_114] | (char)node));
/* 238 */     return write(this.intUnits, length);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeDeltaTo(int jumpTarget)
/*     */   {
/* 247 */     int i = this.charsLength - jumpTarget;
/* 248 */     assert (i >= 0);
/* 249 */     if (i <= 64511)
/* 250 */       return write(i);
/*     */     int length;
/*     */     int length;
/* 253 */     if (i <= 67043327) {
/* 254 */       this.intUnits[0] = ((char)(64512 + (i >> 16)));
/* 255 */       length = 1;
/*     */     } else {
/* 257 */       this.intUnits[0] = 65535;
/* 258 */       this.intUnits[1] = ((char)(i >> 16));
/* 259 */       length = 2;
/*     */     }
/* 261 */     this.intUnits[(length++)] = ((char)i);
/* 262 */     return write(this.intUnits, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CharsTrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */