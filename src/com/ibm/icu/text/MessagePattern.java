/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUConfig;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.util.Freezable;
/*      */ import java.util.ArrayList;
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
/*      */ public final class MessagePattern
/*      */   implements Cloneable, Freezable<MessagePattern>
/*      */ {
/*      */   public static final int ARG_NAME_NOT_NUMBER = -1;
/*      */   public static final int ARG_NAME_NOT_VALID = -2;
/*      */   public static final double NO_NUMERIC_VALUE = -1.23456789E8D;
/*      */   private static final int MAX_PREFIX_LENGTH = 24;
/*      */   private ApostropheMode aposMode;
/*      */   private String msg;
/*      */   
/*      */   public static enum ApostropheMode
/*      */   {
/*  128 */     DOUBLE_OPTIONAL, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  138 */     DOUBLE_REQUIRED;
/*      */     
/*      */ 
/*      */     private ApostropheMode() {}
/*      */   }
/*      */   
/*      */ 
/*      */   public MessagePattern()
/*      */   {
/*  147 */     this.aposMode = defaultAposMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessagePattern(ApostropheMode mode)
/*      */   {
/*  157 */     this.aposMode = mode;
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
/*      */   public MessagePattern(String pattern)
/*      */   {
/*  172 */     this.aposMode = defaultAposMode;
/*  173 */     parse(pattern);
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
/*      */   public MessagePattern parse(String pattern)
/*      */   {
/*  188 */     preParse(pattern);
/*  189 */     parseMessage(0, 0, 0, ArgType.NONE);
/*  190 */     postParse();
/*  191 */     return this;
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
/*      */   public MessagePattern parseChoiceStyle(String pattern)
/*      */   {
/*  206 */     preParse(pattern);
/*  207 */     parseChoiceStyle(0, 0);
/*  208 */     postParse();
/*  209 */     return this;
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
/*      */   public MessagePattern parsePluralStyle(String pattern)
/*      */   {
/*  224 */     preParse(pattern);
/*  225 */     parsePluralOrSelectStyle(ArgType.PLURAL, 0, 0);
/*  226 */     postParse();
/*  227 */     return this;
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
/*      */   public MessagePattern parseSelectStyle(String pattern)
/*      */   {
/*  242 */     preParse(pattern);
/*  243 */     parsePluralOrSelectStyle(ArgType.SELECT, 0, 0);
/*  244 */     postParse();
/*  245 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */   {
/*  256 */     if (isFrozen()) {
/*  257 */       throw new UnsupportedOperationException("Attempt to clear() a frozen MessagePattern instance.");
/*      */     }
/*      */     
/*  260 */     this.msg = null;
/*  261 */     this.hasArgNames = (this.hasArgNumbers = 0);
/*  262 */     this.needsAutoQuoting = false;
/*  263 */     this.parts.clear();
/*  264 */     if (this.numericValues != null) {
/*  265 */       this.numericValues.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearPatternAndSetApostropheMode(ApostropheMode mode)
/*      */   {
/*  277 */     clear();
/*  278 */     this.aposMode = mode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object other)
/*      */   {
/*  289 */     if (this == other) {
/*  290 */       return true;
/*      */     }
/*  292 */     if ((other == null) || (getClass() != other.getClass())) {
/*  293 */       return false;
/*      */     }
/*  295 */     MessagePattern o = (MessagePattern)other;
/*  296 */     return (this.aposMode.equals(o.aposMode)) && (this.msg == null ? o.msg == null : this.msg.equals(o.msg)) && (this.parts.equals(o.parts));
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
/*      */   public int hashCode()
/*      */   {
/*  310 */     return (this.aposMode.hashCode() * 37 + (this.msg != null ? this.msg.hashCode() : 0)) * 37 + this.parts.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ApostropheMode getApostropheMode()
/*      */   {
/*  319 */     return this.aposMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean jdkAposMode()
/*      */   {
/*  327 */     return this.aposMode == ApostropheMode.DOUBLE_REQUIRED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPatternString()
/*      */   {
/*  336 */     return this.msg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasNamedArguments()
/*      */   {
/*  346 */     return this.hasArgNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasNumberedArguments()
/*      */   {
/*  356 */     return this.hasArgNumbers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  366 */     return this.msg;
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
/*      */   public static int validateArgumentName(String name)
/*      */   {
/*  382 */     if (!PatternProps.isIdentifier(name)) {
/*  383 */       return -2;
/*      */     }
/*  385 */     return parseArgNumber(name, 0, name.length());
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
/*      */   public String autoQuoteApostropheDeep()
/*      */   {
/*  418 */     if (!this.needsAutoQuoting) {
/*  419 */       return this.msg;
/*      */     }
/*  421 */     StringBuilder modified = null;
/*      */     
/*  423 */     int count = countParts();
/*  424 */     for (int i = count; i > 0;) {
/*      */       Part part;
/*  426 */       if ((part = getPart(--i)).getType() == MessagePattern.Part.Type.INSERT_CHAR) {
/*  427 */         if (modified == null) {
/*  428 */           modified = new StringBuilder(this.msg.length() + 10).append(this.msg);
/*      */         }
/*  430 */         modified.insert(part.index, (char)part.value);
/*      */       }
/*      */     }
/*  433 */     if (modified == null) {
/*  434 */       return this.msg;
/*      */     }
/*  436 */     return modified.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int countParts()
/*      */   {
/*  448 */     return this.parts.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Part getPart(int i)
/*      */   {
/*  460 */     return (Part)this.parts.get(i);
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
/*      */   public MessagePattern.Part.Type getPartType(int i)
/*      */   {
/*  473 */     return ((Part)this.parts.get(i)).type;
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
/*      */   public int getPatternIndex(int partIndex)
/*      */   {
/*  486 */     return ((Part)this.parts.get(partIndex)).index;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSubstring(Part part)
/*      */   {
/*  498 */     int index = part.index;
/*  499 */     return this.msg.substring(index, index + part.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean partSubstringMatches(Part part, String s)
/*      */   {
/*  511 */     return this.msg.regionMatches(part.index, s, 0, part.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getNumericValue(Part part)
/*      */   {
/*  522 */     MessagePattern.Part.Type type = part.type;
/*  523 */     if (type == MessagePattern.Part.Type.ARG_INT)
/*  524 */       return part.value;
/*  525 */     if (type == MessagePattern.Part.Type.ARG_DOUBLE) {
/*  526 */       return ((Double)this.numericValues.get(part.value)).doubleValue();
/*      */     }
/*  528 */     return -1.23456789E8D;
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
/*      */   public double getPluralOffset(int pluralStart)
/*      */   {
/*  550 */     Part part = (Part)this.parts.get(pluralStart);
/*  551 */     if (part.type.hasNumericValue()) {
/*  552 */       return getNumericValue(part);
/*      */     }
/*  554 */     return 0.0D;
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
/*      */   public int getLimitPartIndex(int start)
/*      */   {
/*  569 */     int limit = ((Part)this.parts.get(start)).limitPartIndex;
/*  570 */     if (limit < start) {
/*  571 */       return start;
/*      */     }
/*  573 */     return limit;
/*      */   }
/*      */   
/*      */   public static final class Part {
/*      */     private static final int MAX_LENGTH = 65535;
/*      */     private static final int MAX_VALUE = 32767;
/*      */     private final Type type;
/*      */     private final int index;
/*      */     private final char length;
/*      */     private short value;
/*      */     private int limitPartIndex;
/*      */     
/*      */     private Part(Type t, int i, int l, int v) {
/*  586 */       this.type = t;
/*  587 */       this.index = i;
/*  588 */       this.length = ((char)l);
/*  589 */       this.value = ((short)v);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Type getType()
/*      */     {
/*  599 */       return this.type;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getIndex()
/*      */     {
/*  609 */       return this.index;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getLength()
/*      */     {
/*  620 */       return this.length;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getLimit()
/*      */     {
/*  631 */       return this.index + this.length;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getValue()
/*      */     {
/*  642 */       return this.value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MessagePattern.ArgType getArgType()
/*      */     {
/*  653 */       Type type = getType();
/*  654 */       if ((type == Type.ARG_START) || (type == Type.ARG_LIMIT)) {
/*  655 */         return MessagePattern.argTypes[this.value];
/*      */       }
/*  657 */       return MessagePattern.ArgType.NONE;
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
/*      */     public static enum Type
/*      */     {
/*  677 */       MSG_START, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  687 */       MSG_LIMIT, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  696 */       SKIP_SYNTAX, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  704 */       INSERT_CHAR, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  713 */       REPLACE_NUMBER, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  721 */       ARG_START, 
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
/*  733 */       ARG_LIMIT, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  739 */       ARG_NUMBER, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  746 */       ARG_NAME, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  753 */       ARG_TYPE, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  760 */       ARG_STYLE, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  767 */       ARG_SELECTOR, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */       ARG_INT, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  784 */       ARG_DOUBLE;
/*      */       
/*      */ 
/*      */ 
/*      */       private Type() {}
/*      */       
/*      */ 
/*      */ 
/*      */       public boolean hasNumericValue()
/*      */       {
/*  794 */         return (this == ARG_INT) || (this == ARG_DOUBLE);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  805 */       String valueString = (this.type == Type.ARG_START) || (this.type == Type.ARG_LIMIT) ? getArgType().name() : Integer.toString(this.value);
/*      */       
/*  807 */       return this.type.name() + "(" + valueString + ")@" + this.index;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object other)
/*      */     {
/*  818 */       if (this == other) {
/*  819 */         return true;
/*      */       }
/*  821 */       if ((other == null) || (getClass() != other.getClass())) {
/*  822 */         return false;
/*      */       }
/*  824 */       Part o = (Part)other;
/*  825 */       return (this.type.equals(o.type)) && (this.index == o.index) && (this.length == o.length) && (this.value == o.value) && (this.limitPartIndex == o.limitPartIndex);
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
/*      */     public int hashCode()
/*      */     {
/*  840 */       return ((this.type.hashCode() * 37 + this.index) * 37 + this.length) * 37 + this.value;
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
/*      */   public static enum ArgType
/*      */   {
/*  870 */     NONE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  877 */     SIMPLE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  884 */     CHOICE, 
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
/*  895 */     PLURAL, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  901 */     SELECT;
/*      */     
/*      */ 
/*      */ 
/*      */     private ArgType() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  912 */     if (isFrozen()) {
/*  913 */       return this;
/*      */     }
/*  915 */     return cloneAsThawed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessagePattern cloneAsThawed()
/*      */   {
/*      */     MessagePattern newMsg;
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  929 */       newMsg = (MessagePattern)super.clone();
/*      */     } catch (CloneNotSupportedException e) {
/*  931 */       throw new RuntimeException(e);
/*      */     }
/*  933 */     newMsg.parts = ((ArrayList)this.parts.clone());
/*  934 */     if (this.numericValues != null) {
/*  935 */       newMsg.numericValues = ((ArrayList)this.numericValues.clone());
/*      */     }
/*  937 */     newMsg.frozen = false;
/*  938 */     return newMsg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessagePattern freeze()
/*      */   {
/*  948 */     this.frozen = true;
/*  949 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrozen()
/*      */   {
/*  959 */     return this.frozen;
/*      */   }
/*      */   
/*      */   private void preParse(String pattern) {
/*  963 */     if (isFrozen()) {
/*  964 */       throw new UnsupportedOperationException("Attempt to parse(\"" + prefix(pattern) + "\") on frozen MessagePattern instance.");
/*      */     }
/*      */     
/*  967 */     this.msg = pattern;
/*  968 */     this.hasArgNames = (this.hasArgNumbers = 0);
/*  969 */     this.needsAutoQuoting = false;
/*  970 */     this.parts.clear();
/*  971 */     if (this.numericValues != null) {
/*  972 */       this.numericValues.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void postParse() {}
/*      */   
/*      */   private int parseMessage(int index, int msgStartLength, int nestingLevel, ArgType parentType)
/*      */   {
/*  981 */     if (nestingLevel > 32767) {
/*  982 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  984 */     int msgStart = this.parts.size();
/*  985 */     addPart(MessagePattern.Part.Type.MSG_START, index, msgStartLength, nestingLevel);
/*  986 */     index += msgStartLength;
/*  987 */     label275: label448: while (index < this.msg.length()) {
/*  988 */       char c = this.msg.charAt(index++);
/*  989 */       if (c == '\'') {
/*  990 */         if (index == this.msg.length())
/*      */         {
/*      */ 
/*  993 */           addPart(MessagePattern.Part.Type.INSERT_CHAR, index, 0, 39);
/*  994 */           this.needsAutoQuoting = true;
/*      */         } else {
/*  996 */           c = this.msg.charAt(index);
/*  997 */           if (c == '\'')
/*      */           {
/*  999 */             addPart(MessagePattern.Part.Type.SKIP_SYNTAX, index++, 1, 0);
/* 1000 */           } else if ((this.aposMode == ApostropheMode.DOUBLE_REQUIRED) || (c == '{') || (c == '}') || ((parentType == ArgType.CHOICE) && (c == '|')) || ((parentType == ArgType.PLURAL) && (c == '#')))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1007 */             addPart(MessagePattern.Part.Type.SKIP_SYNTAX, index - 1, 1, 0);
/*      */             for (;;)
/*      */             {
/* 1010 */               index = this.msg.indexOf('\'', index + 1);
/* 1011 */               if (index < 0) break label275;
/* 1012 */               if ((index + 1 >= this.msg.length()) || (this.msg.charAt(index + 1) != '\'')) {
/*      */                 break;
/*      */               }
/* 1015 */               addPart(MessagePattern.Part.Type.SKIP_SYNTAX, ++index, 1, 0);
/*      */             }
/*      */             
/* 1018 */             addPart(MessagePattern.Part.Type.SKIP_SYNTAX, index++, 1, 0);
/*      */             
/*      */ 
/*      */             break label448;
/*      */             
/* 1023 */             index = this.msg.length();
/*      */             
/* 1025 */             addPart(MessagePattern.Part.Type.INSERT_CHAR, index, 0, 39);
/* 1026 */             this.needsAutoQuoting = true;
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1033 */             addPart(MessagePattern.Part.Type.INSERT_CHAR, index, 0, 39);
/* 1034 */             this.needsAutoQuoting = true;
/*      */           }
/*      */         }
/* 1037 */       } else if ((parentType == ArgType.PLURAL) && (c == '#'))
/*      */       {
/*      */ 
/* 1040 */         addPart(MessagePattern.Part.Type.REPLACE_NUMBER, index - 1, 1, 0);
/* 1041 */       } else if (c == '{') {
/* 1042 */         index = parseArg(index - 1, 1, nestingLevel);
/* 1043 */       } else if (((nestingLevel > 0) && (c == '}')) || ((parentType == ArgType.CHOICE) && (c == '|')))
/*      */       {
/*      */ 
/*      */ 
/* 1047 */         int limitLength = (parentType == ArgType.CHOICE) && (c == '}') ? 0 : 1;
/* 1048 */         addLimitPart(msgStart, MessagePattern.Part.Type.MSG_LIMIT, index - 1, limitLength, nestingLevel);
/* 1049 */         if (parentType == ArgType.CHOICE)
/*      */         {
/* 1051 */           return index - 1;
/*      */         }
/*      */         
/* 1054 */         return index;
/*      */       }
/*      */     }
/*      */     
/* 1058 */     if ((nestingLevel > 0) && (!inTopLevelChoiceMessage(nestingLevel, parentType))) {
/* 1059 */       throw new IllegalArgumentException("Unmatched '{' braces in message \"" + prefix() + "\"");
/*      */     }
/*      */     
/* 1062 */     addLimitPart(msgStart, MessagePattern.Part.Type.MSG_LIMIT, index, 0, nestingLevel);
/* 1063 */     return index;
/*      */   }
/*      */   
/*      */   private int parseArg(int index, int argStartLength, int nestingLevel) {
/* 1067 */     int argStart = this.parts.size();
/* 1068 */     ArgType argType = ArgType.NONE;
/* 1069 */     addPart(MessagePattern.Part.Type.ARG_START, index, argStartLength, argType.ordinal());
/* 1070 */     int nameIndex = index = skipWhiteSpace(index + argStartLength);
/* 1071 */     if (index == this.msg.length()) {
/* 1072 */       throw new IllegalArgumentException("Unmatched '{' braces in message \"" + prefix() + "\"");
/*      */     }
/*      */     
/*      */ 
/* 1076 */     index = skipIdentifier(index);
/* 1077 */     int number = parseArgNumber(nameIndex, index);
/* 1078 */     if (number >= 0) {
/* 1079 */       int length = index - nameIndex;
/* 1080 */       if ((length > 65535) || (number > 32767)) {
/* 1081 */         throw new IndexOutOfBoundsException("Argument number too large: " + prefix(nameIndex));
/*      */       }
/*      */       
/* 1084 */       this.hasArgNumbers = true;
/* 1085 */       addPart(MessagePattern.Part.Type.ARG_NUMBER, nameIndex, length, number);
/* 1086 */     } else if (number == -1) {
/* 1087 */       int length = index - nameIndex;
/* 1088 */       if (length > 65535) {
/* 1089 */         throw new IndexOutOfBoundsException("Argument name too long: " + prefix(nameIndex));
/*      */       }
/*      */       
/* 1092 */       this.hasArgNames = true;
/* 1093 */       addPart(MessagePattern.Part.Type.ARG_NAME, nameIndex, length, 0);
/*      */     } else {
/* 1095 */       throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex));
/*      */     }
/* 1097 */     index = skipWhiteSpace(index);
/* 1098 */     if (index == this.msg.length()) {
/* 1099 */       throw new IllegalArgumentException("Unmatched '{' braces in message \"" + prefix() + "\"");
/*      */     }
/*      */     
/* 1102 */     char c = this.msg.charAt(index);
/* 1103 */     if (c != '}')
/*      */     {
/* 1105 */       if (c != ',') {
/* 1106 */         throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex));
/*      */       }
/*      */       
/* 1109 */       int typeIndex = index = skipWhiteSpace(index + 1);
/* 1110 */       while ((index < this.msg.length()) && (isArgTypeChar(this.msg.charAt(index)))) {
/* 1111 */         index++;
/*      */       }
/* 1113 */       int length = index - typeIndex;
/* 1114 */       index = skipWhiteSpace(index);
/* 1115 */       if (index == this.msg.length()) {
/* 1116 */         throw new IllegalArgumentException("Unmatched '{' braces in message \"" + prefix() + "\"");
/*      */       }
/*      */       
/* 1119 */       if ((length == 0) || (((c = this.msg.charAt(index)) != ',') && (c != '}'))) {
/* 1120 */         throw new IllegalArgumentException("Bad argument syntax: " + prefix(nameIndex));
/*      */       }
/* 1122 */       if (length > 65535) {
/* 1123 */         throw new IndexOutOfBoundsException("Argument type name too long: " + prefix(nameIndex));
/*      */       }
/*      */       
/* 1126 */       argType = ArgType.SIMPLE;
/* 1127 */       if (length == 6)
/*      */       {
/* 1129 */         if (isChoice(typeIndex)) {
/* 1130 */           argType = ArgType.CHOICE;
/* 1131 */         } else if (isPlural(typeIndex)) {
/* 1132 */           argType = ArgType.PLURAL;
/* 1133 */         } else if (isSelect(typeIndex)) {
/* 1134 */           argType = ArgType.SELECT;
/*      */         }
/*      */       }
/*      */       
/* 1138 */       ((Part)this.parts.get(argStart)).value = ((short)argType.ordinal());
/* 1139 */       if (argType == ArgType.SIMPLE) {
/* 1140 */         addPart(MessagePattern.Part.Type.ARG_TYPE, typeIndex, length, 0);
/*      */       }
/*      */       
/* 1143 */       if (c == '}') {
/* 1144 */         if (argType != ArgType.SIMPLE) {
/* 1145 */           throw new IllegalArgumentException("No style field for complex argument: " + prefix(nameIndex));
/*      */         }
/*      */       }
/*      */       else {
/* 1149 */         index++;
/* 1150 */         if (argType == ArgType.SIMPLE) {
/* 1151 */           index = parseSimpleStyle(index);
/* 1152 */         } else if (argType == ArgType.CHOICE) {
/* 1153 */           index = parseChoiceStyle(index, nestingLevel);
/*      */         } else {
/* 1155 */           index = parsePluralOrSelectStyle(argType, index, nestingLevel);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1160 */     addLimitPart(argStart, MessagePattern.Part.Type.ARG_LIMIT, index, 1, argType.ordinal());
/* 1161 */     return index + 1;
/*      */   }
/*      */   
/*      */   private int parseSimpleStyle(int index) {
/* 1165 */     int start = index;
/* 1166 */     int nestedBraces = 0;
/* 1167 */     while (index < this.msg.length()) {
/* 1168 */       char c = this.msg.charAt(index++);
/* 1169 */       if (c == '\'')
/*      */       {
/*      */ 
/* 1172 */         index = this.msg.indexOf('\'', index);
/* 1173 */         if (index < 0) {
/* 1174 */           throw new IllegalArgumentException("Quoted literal argument style text reaches to the end of the message: \"" + prefix(start) + "\"");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1179 */         index++;
/* 1180 */       } else if (c == '{') {
/* 1181 */         nestedBraces++;
/* 1182 */       } else if (c == '}') {
/* 1183 */         if (nestedBraces > 0) {
/* 1184 */           nestedBraces--;
/*      */         } else {
/* 1186 */           index--;int length = index - start;
/* 1187 */           if (length > 65535) {
/* 1188 */             throw new IndexOutOfBoundsException("Argument style text too long: " + prefix(start));
/*      */           }
/*      */           
/* 1191 */           addPart(MessagePattern.Part.Type.ARG_STYLE, start, length, 0);
/* 1192 */           return index;
/*      */         }
/*      */       }
/*      */     }
/* 1196 */     throw new IllegalArgumentException("Unmatched '{' braces in message \"" + prefix() + "\"");
/*      */   }
/*      */   
/*      */   private int parseChoiceStyle(int index, int nestingLevel)
/*      */   {
/* 1201 */     int start = index;
/* 1202 */     index = skipWhiteSpace(index);
/* 1203 */     if ((index == this.msg.length()) || (this.msg.charAt(index) == '}')) {
/* 1204 */       throw new IllegalArgumentException("Missing choice argument pattern in \"" + prefix() + "\"");
/*      */     }
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1210 */       int numberIndex = index;
/* 1211 */       index = skipDouble(index);
/* 1212 */       int length = index - numberIndex;
/* 1213 */       if (length == 0) {
/* 1214 */         throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start));
/*      */       }
/* 1216 */       if (length > 65535) {
/* 1217 */         throw new IndexOutOfBoundsException("Choice number too long: " + prefix(numberIndex));
/*      */       }
/*      */       
/* 1220 */       parseDouble(numberIndex, index, true);
/*      */       
/* 1222 */       index = skipWhiteSpace(index);
/* 1223 */       if (index == this.msg.length()) {
/* 1224 */         throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start));
/*      */       }
/* 1226 */       char c = this.msg.charAt(index);
/* 1227 */       if ((c != '#') && (c != '<') && (c != '≤')) {
/* 1228 */         throw new IllegalArgumentException("Expected choice separator (#<≤) instead of '" + c + "' in choice pattern " + prefix(start));
/*      */       }
/*      */       
/*      */ 
/* 1232 */       addPart(MessagePattern.Part.Type.ARG_SELECTOR, index, 1, 0);
/*      */       
/* 1234 */       index = parseMessage(++index, 0, nestingLevel + 1, ArgType.CHOICE);
/*      */       
/* 1236 */       if (index == this.msg.length()) {
/* 1237 */         return index;
/*      */       }
/* 1239 */       if (this.msg.charAt(index) == '}') {
/* 1240 */         if (!inMessageFormatPattern(nestingLevel)) {
/* 1241 */           throw new IllegalArgumentException("Bad choice pattern syntax: " + prefix(start));
/*      */         }
/*      */         
/* 1244 */         return index;
/*      */       }
/* 1246 */       index = skipWhiteSpace(index + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private int parsePluralOrSelectStyle(ArgType argType, int index, int nestingLevel) {
/* 1251 */     int start = index;
/* 1252 */     boolean isEmpty = true;
/* 1253 */     boolean hasOther = false;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1258 */       index = skipWhiteSpace(index);
/* 1259 */       boolean eos = index == this.msg.length();
/* 1260 */       if ((eos) || (this.msg.charAt(index) == '}')) {
/* 1261 */         if (eos == inMessageFormatPattern(nestingLevel)) {
/* 1262 */           throw new IllegalArgumentException("Bad " + (argType == ArgType.PLURAL ? "plural" : "select") + " pattern syntax: " + prefix(start));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1267 */         if (!hasOther) {
/* 1268 */           throw new IllegalArgumentException("Missing 'other' keyword in " + (argType == ArgType.PLURAL ? "plural" : "select") + " pattern in \"" + prefix() + "\"");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1273 */         return index;
/*      */       }
/* 1275 */       int selectorIndex = index;
/* 1276 */       if ((argType == ArgType.PLURAL) && (this.msg.charAt(selectorIndex) == '='))
/*      */       {
/* 1278 */         index = skipDouble(index + 1);
/* 1279 */         int length = index - selectorIndex;
/* 1280 */         if (length == 1) {
/* 1281 */           throw new IllegalArgumentException("Bad " + (argType == ArgType.PLURAL ? "plural" : "select") + " pattern syntax: " + prefix(start));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1286 */         if (length > 65535) {
/* 1287 */           throw new IndexOutOfBoundsException("Argument selector too long: " + prefix(selectorIndex));
/*      */         }
/*      */         
/* 1290 */         addPart(MessagePattern.Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
/* 1291 */         parseDouble(selectorIndex + 1, index, false);
/*      */       } else {
/* 1293 */         index = skipIdentifier(index);
/* 1294 */         int length = index - selectorIndex;
/* 1295 */         if (length == 0) {
/* 1296 */           throw new IllegalArgumentException("Bad " + (argType == ArgType.PLURAL ? "plural" : "select") + " pattern syntax: " + prefix(start));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1302 */         if ((argType == ArgType.PLURAL) && (length == 6) && (index < this.msg.length()) && (this.msg.regionMatches(selectorIndex, "offset:", 0, 7)))
/*      */         {
/*      */ 
/*      */ 
/* 1306 */           if (!isEmpty) {
/* 1307 */             throw new IllegalArgumentException("Plural argument 'offset:' (if present) must precede key-message pairs: " + prefix(start));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1312 */           int valueIndex = skipWhiteSpace(index + 1);
/* 1313 */           index = skipDouble(valueIndex);
/* 1314 */           if (index == valueIndex) {
/* 1315 */             throw new IllegalArgumentException("Missing value for plural 'offset:' at " + prefix(start));
/*      */           }
/*      */           
/* 1318 */           if (index - valueIndex > 65535) {
/* 1319 */             throw new IndexOutOfBoundsException("Plural offset value too long: " + prefix(valueIndex));
/*      */           }
/*      */           
/* 1322 */           parseDouble(valueIndex, index, false);
/* 1323 */           isEmpty = false;
/* 1324 */           continue;
/*      */         }
/*      */         
/* 1327 */         if (length > 65535) {
/* 1328 */           throw new IndexOutOfBoundsException("Argument selector too long: " + prefix(selectorIndex));
/*      */         }
/*      */         
/* 1331 */         addPart(MessagePattern.Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
/* 1332 */         if (this.msg.regionMatches(selectorIndex, "other", 0, length)) {
/* 1333 */           hasOther = true;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1339 */       index = skipWhiteSpace(index);
/* 1340 */       if ((index == this.msg.length()) || (this.msg.charAt(index) != '{')) {
/* 1341 */         throw new IllegalArgumentException("No message fragment after " + (argType == ArgType.PLURAL ? "plural" : "select") + " selector: " + prefix(selectorIndex));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1346 */       index = parseMessage(index, 1, nestingLevel + 1, argType);
/* 1347 */       isEmpty = false;
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
/*      */ 
/*      */   private static int parseArgNumber(CharSequence s, int start, int limit)
/*      */   {
/* 1363 */     if (start >= limit) {
/* 1364 */       return -2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1369 */     char c = s.charAt(start++);
/* 1370 */     boolean badNumber; if (c == '0') {
/* 1371 */       if (start == limit) {
/* 1372 */         return 0;
/*      */       }
/* 1374 */       int number = 0;
/* 1375 */       badNumber = true;
/*      */     } else { boolean badNumber;
/* 1377 */       if (('1' <= c) && (c <= '9')) {
/* 1378 */         int number = c - '0';
/* 1379 */         badNumber = false;
/*      */       } else {
/* 1381 */         return -1; } }
/*      */     boolean badNumber;
/* 1383 */     int number; while (start < limit) {
/* 1384 */       c = s.charAt(start++);
/* 1385 */       if (('0' <= c) && (c <= '9')) {
/* 1386 */         if (number >= 214748364) {
/* 1387 */           badNumber = true;
/*      */         }
/* 1389 */         number = number * 10 + (c - '0');
/*      */       } else {
/* 1391 */         return -1;
/*      */       }
/*      */     }
/*      */     
/* 1395 */     if (badNumber) {
/* 1396 */       return -2;
/*      */     }
/* 1398 */     return number;
/*      */   }
/*      */   
/*      */   private int parseArgNumber(int start, int limit)
/*      */   {
/* 1403 */     return parseArgNumber(this.msg, start, limit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseDouble(int start, int limit, boolean allowInfinity)
/*      */   {
/* 1413 */     assert (start < limit);
/*      */     
/*      */ 
/*      */ 
/* 1417 */     int value = 0;
/* 1418 */     int isNegative = 0;
/* 1419 */     int index = start;
/* 1420 */     char c = this.msg.charAt(index++);
/* 1421 */     if (c == '-') {
/* 1422 */       isNegative = 1;
/* 1423 */       if (index == limit) {
/*      */         break label263;
/*      */       }
/* 1426 */       c = this.msg.charAt(index++);
/* 1427 */     } else if (c == '+') {
/* 1428 */       if (index == limit) {
/*      */         break label263;
/*      */       }
/* 1431 */       c = this.msg.charAt(index++);
/*      */     }
/* 1433 */     if (c == '∞') {
/* 1434 */       if ((allowInfinity) && (index == limit)) {
/* 1435 */         addArgDoublePart(isNegative != 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, start, limit - start);
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 1444 */       while (('0' <= c) && (c <= '9')) {
/* 1445 */         value = value * 10 + (c - '0');
/* 1446 */         if (value > 32767 + isNegative) {
/*      */           break;
/*      */         }
/* 1449 */         if (index == limit) {
/* 1450 */           addPart(MessagePattern.Part.Type.ARG_INT, start, limit - start, isNegative != 0 ? -value : value);
/* 1451 */           return;
/*      */         }
/* 1453 */         c = this.msg.charAt(index++);
/*      */       }
/*      */       
/* 1456 */       double numericValue = Double.parseDouble(this.msg.substring(start, limit));
/* 1457 */       addArgDoublePart(numericValue, start, limit - start);
/* 1458 */       return; }
/*      */     label263:
/* 1460 */     throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void appendReducedApostrophes(String s, int start, int limit, StringBuilder sb)
/*      */   {
/* 1471 */     int doubleApos = -1;
/*      */     for (;;) {
/* 1473 */       int i = s.indexOf('\'', start);
/* 1474 */       if ((i < 0) || (i >= limit)) {
/* 1475 */         sb.append(s, start, limit);
/* 1476 */         break;
/*      */       }
/* 1478 */       if (i == doubleApos)
/*      */       {
/* 1480 */         sb.append('\'');
/* 1481 */         start++;
/* 1482 */         doubleApos = -1;
/*      */       }
/*      */       else {
/* 1485 */         sb.append(s, start, i);
/* 1486 */         doubleApos = start = i + 1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private int skipWhiteSpace(int index) {
/* 1492 */     return PatternProps.skipWhiteSpace(this.msg, index);
/*      */   }
/*      */   
/*      */   private int skipIdentifier(int index) {
/* 1496 */     return PatternProps.skipIdentifier(this.msg, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int skipDouble(int index)
/*      */   {
/* 1504 */     while (index < this.msg.length()) {
/* 1505 */       char c = this.msg.charAt(index);
/*      */       
/* 1507 */       if (((c < '0') && ("+-.".indexOf(c) < 0)) || ((c > '9') && (c != 'e') && (c != 'E') && (c != '∞'))) {
/*      */         break;
/*      */       }
/* 1510 */       index++;
/*      */     }
/* 1512 */     return index;
/*      */   }
/*      */   
/*      */   private static boolean isArgTypeChar(int c) {
/* 1516 */     return ((97 <= c) && (c <= 122)) || ((65 <= c) && (c <= 90));
/*      */   }
/*      */   
/*      */   private boolean isChoice(int index) {
/*      */     char c;
/* 1521 */     return (((c = this.msg.charAt(index++)) == 'c') || (c == 'C')) && (((c = this.msg.charAt(index++)) == 'h') || (c == 'H')) && (((c = this.msg.charAt(index++)) == 'o') || (c == 'O')) && (((c = this.msg.charAt(index++)) == 'i') || (c == 'I')) && (((c = this.msg.charAt(index++)) == 'c') || (c == 'C')) && (((c = this.msg.charAt(index)) == 'e') || (c == 'E'));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isPlural(int index)
/*      */   {
/*      */     char c;
/*      */     
/*      */ 
/* 1532 */     return (((c = this.msg.charAt(index++)) == 'p') || (c == 'P')) && (((c = this.msg.charAt(index++)) == 'l') || (c == 'L')) && (((c = this.msg.charAt(index++)) == 'u') || (c == 'U')) && (((c = this.msg.charAt(index++)) == 'r') || (c == 'R')) && (((c = this.msg.charAt(index++)) == 'a') || (c == 'A')) && (((c = this.msg.charAt(index)) == 'l') || (c == 'L'));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isSelect(int index)
/*      */   {
/*      */     char c;
/*      */     
/*      */ 
/* 1543 */     return (((c = this.msg.charAt(index++)) == 's') || (c == 'S')) && (((c = this.msg.charAt(index++)) == 'e') || (c == 'E')) && (((c = this.msg.charAt(index++)) == 'l') || (c == 'L')) && (((c = this.msg.charAt(index++)) == 'e') || (c == 'E')) && (((c = this.msg.charAt(index++)) == 'c') || (c == 'C')) && (((c = this.msg.charAt(index)) == 't') || (c == 'T'));
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
/*      */   private boolean inMessageFormatPattern(int nestingLevel)
/*      */   {
/* 1557 */     return (nestingLevel > 0) || (((Part)this.parts.get(0)).type == MessagePattern.Part.Type.MSG_START);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean inTopLevelChoiceMessage(int nestingLevel, ArgType parentType)
/*      */   {
/* 1565 */     return (nestingLevel == 1) && (parentType == ArgType.CHOICE) && (((Part)this.parts.get(0)).type != MessagePattern.Part.Type.MSG_START);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addPart(MessagePattern.Part.Type type, int index, int length, int value)
/*      */   {
/* 1572 */     this.parts.add(new Part(type, index, length, value, null));
/*      */   }
/*      */   
/*      */   private void addLimitPart(int start, MessagePattern.Part.Type type, int index, int length, int value) {
/* 1576 */     ((Part)this.parts.get(start)).limitPartIndex = this.parts.size();
/* 1577 */     addPart(type, index, length, value);
/*      */   }
/*      */   
/*      */   private void addArgDoublePart(double numericValue, int start, int length) { int numericIndex;
/*      */     int numericIndex;
/* 1582 */     if (this.numericValues == null) {
/* 1583 */       this.numericValues = new ArrayList();
/* 1584 */       numericIndex = 0;
/*      */     } else {
/* 1586 */       numericIndex = this.numericValues.size();
/* 1587 */       if (numericIndex > 32767) {
/* 1588 */         throw new IndexOutOfBoundsException("Too many numeric values");
/*      */       }
/*      */     }
/* 1591 */     this.numericValues.add(Double.valueOf(numericValue));
/* 1592 */     addPart(MessagePattern.Part.Type.ARG_DOUBLE, start, length, numericIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String prefix(String s, int start)
/*      */   {
/* 1604 */     int substringLength = s.length() - start;
/* 1605 */     if (substringLength <= 24) {
/* 1606 */       return start == 0 ? s : s.substring(start);
/*      */     }
/* 1608 */     StringBuilder prefix = new StringBuilder(24);
/* 1609 */     prefix.append(s, start, start + 24 - 4);
/* 1610 */     if (Character.isHighSurrogate(prefix.charAt(19)))
/*      */     {
/* 1612 */       prefix.setLength(19);
/*      */     }
/* 1614 */     return " ...";
/*      */   }
/*      */   
/*      */   private static String prefix(String s)
/*      */   {
/* 1619 */     return prefix(s, 0);
/*      */   }
/*      */   
/*      */   private String prefix(int start) {
/* 1623 */     return prefix(this.msg, start);
/*      */   }
/*      */   
/*      */   private String prefix() {
/* 1627 */     return prefix(this.msg, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1632 */   private ArrayList<Part> parts = new ArrayList();
/*      */   
/*      */   private ArrayList<Double> numericValues;
/*      */   private boolean hasArgNames;
/*      */   private boolean hasArgNumbers;
/*      */   private boolean needsAutoQuoting;
/*      */   private boolean frozen;
/* 1639 */   private static final ApostropheMode defaultAposMode = ApostropheMode.valueOf(ICUConfig.get("com.ibm.icu.text.MessagePattern.ApostropheMode", "DOUBLE_OPTIONAL"));
/*      */   
/*      */ 
/*      */ 
/* 1643 */   private static final ArgType[] argTypes = ArgType.values();
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\MessagePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */