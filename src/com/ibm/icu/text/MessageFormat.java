/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.text.AttributedString;
/*      */ import java.text.CharacterIterator;
/*      */ import java.text.ChoiceFormat;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.Format;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
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
/*      */ public class MessageFormat
/*      */   extends UFormat
/*      */ {
/*      */   static final long serialVersionUID = 7136212545847378652L;
/*      */   private transient ULocale ulocale;
/*      */   private transient MessagePattern msgPattern;
/*      */   private transient Map<Integer, Format> cachedFormatters;
/*      */   private transient Set<Integer> customFormatArgStarts;
/*      */   private transient Format stockDateFormatter;
/*      */   private transient Format stockNumberFormatter;
/*      */   private transient PluralSelectorProvider pluralProvider;
/*      */   
/*      */   public MessageFormat(String pattern)
/*      */   {
/*  342 */     this.ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
/*  343 */     applyPattern(pattern);
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
/*      */   public MessageFormat(String pattern, Locale locale)
/*      */   {
/*  357 */     this(pattern, ULocale.forLocale(locale));
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
/*      */   public MessageFormat(String pattern, ULocale locale)
/*      */   {
/*  371 */     this.ulocale = locale;
/*  372 */     applyPattern(pattern);
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
/*      */   public void setLocale(Locale locale)
/*      */   {
/*  385 */     setLocale(ULocale.forLocale(locale));
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
/*      */   public void setLocale(ULocale locale)
/*      */   {
/*  401 */     String existingPattern = toPattern();
/*  402 */     this.ulocale = locale;
/*      */     
/*      */ 
/*  405 */     this.stockNumberFormatter = (this.stockDateFormatter = null);
/*  406 */     this.pluralProvider = null;
/*  407 */     applyPattern(existingPattern);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  417 */     return this.ulocale.toLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale getULocale()
/*      */   {
/*  427 */     return this.ulocale;
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
/*      */   public void applyPattern(String pttrn)
/*      */   {
/*      */     try
/*      */     {
/*  442 */       if (this.msgPattern == null) {
/*  443 */         this.msgPattern = new MessagePattern(pttrn);
/*      */       } else {
/*  445 */         this.msgPattern.parse(pttrn);
/*      */       }
/*      */       
/*  448 */       cacheExplicitFormats();
/*      */     } catch (RuntimeException e) {
/*  450 */       resetPattern();
/*  451 */       throw e;
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
/*      */   public void applyPattern(String pattern, MessagePattern.ApostropheMode aposMode)
/*      */   {
/*  472 */     if (this.msgPattern == null) {
/*  473 */       this.msgPattern = new MessagePattern(aposMode);
/*  474 */     } else if (aposMode != this.msgPattern.getApostropheMode()) {
/*  475 */       this.msgPattern.clearPatternAndSetApostropheMode(aposMode);
/*      */     }
/*  477 */     applyPattern(pattern);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessagePattern.ApostropheMode getApostropheMode()
/*      */   {
/*  487 */     if (this.msgPattern == null) {
/*  488 */       this.msgPattern = new MessagePattern();
/*      */     }
/*  490 */     return this.msgPattern.getApostropheMode();
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
/*      */   public String toPattern()
/*      */   {
/*  507 */     if (this.customFormatArgStarts != null) {
/*  508 */       throw new IllegalStateException("toPattern() is not supported after custom Format objects have been set via setFormat() or similar APIs");
/*      */     }
/*      */     
/*      */ 
/*  512 */     if (this.msgPattern == null) {
/*  513 */       return "";
/*      */     }
/*  515 */     String originalPattern = this.msgPattern.getPatternString();
/*  516 */     return originalPattern == null ? "" : originalPattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextTopLevelArgStart(int partIndex)
/*      */   {
/*  525 */     if (partIndex != 0) {
/*  526 */       partIndex = this.msgPattern.getLimitPartIndex(partIndex);
/*      */     }
/*      */     for (;;) {
/*  529 */       MessagePattern.Part.Type type = this.msgPattern.getPartType(++partIndex);
/*  530 */       if (type == MessagePattern.Part.Type.ARG_START) {
/*  531 */         return partIndex;
/*      */       }
/*  533 */       if (type == MessagePattern.Part.Type.MSG_LIMIT) {
/*  534 */         return -1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean argNameMatches(int partIndex, String argName, int argNumber) {
/*  540 */     MessagePattern.Part part = this.msgPattern.getPart(partIndex);
/*  541 */     return part.getValue() == argNumber ? true : part.getType() == MessagePattern.Part.Type.ARG_NAME ? this.msgPattern.partSubstringMatches(part, argName) : false;
/*      */   }
/*      */   
/*      */ 
/*      */   private String getArgName(int partIndex)
/*      */   {
/*  547 */     MessagePattern.Part part = this.msgPattern.getPart(partIndex);
/*  548 */     if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
/*  549 */       return this.msgPattern.getSubstring(part);
/*      */     }
/*  551 */     return Integer.toString(part.getValue());
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
/*      */   public void setFormatsByArgumentIndex(Format[] newFormats)
/*      */   {
/*  583 */     if (this.msgPattern.hasNamedArguments()) {
/*  584 */       throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
/*      */     }
/*      */     
/*      */ 
/*  588 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  589 */       int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
/*  590 */       if (argNumber < newFormats.length) {
/*  591 */         setCustomArgStartFormat(partIndex, newFormats[argNumber]);
/*      */       }
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
/*      */   public void setFormatsByArgumentName(Map<String, Format> newFormats)
/*      */   {
/*  618 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  619 */       String key = getArgName(partIndex + 1);
/*  620 */       if (newFormats.containsKey(key)) {
/*  621 */         setCustomArgStartFormat(partIndex, (Format)newFormats.get(key));
/*      */       }
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
/*      */   public void setFormats(Format[] newFormats)
/*      */   {
/*  650 */     int formatNumber = 0;
/*  651 */     int partIndex = 0;
/*      */     
/*  653 */     while ((formatNumber < newFormats.length) && ((partIndex = nextTopLevelArgStart(partIndex)) >= 0)) {
/*  654 */       setCustomArgStartFormat(partIndex, newFormats[formatNumber]);
/*  655 */       formatNumber++;
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
/*      */   public void setFormatByArgumentIndex(int argumentIndex, Format newFormat)
/*      */   {
/*  682 */     if (this.msgPattern.hasNamedArguments()) {
/*  683 */       throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
/*      */     }
/*      */     
/*      */ 
/*  687 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  688 */       if (this.msgPattern.getPart(partIndex + 1).getValue() == argumentIndex) {
/*  689 */         setCustomArgStartFormat(partIndex, newFormat);
/*      */       }
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
/*      */   public void setFormatByArgumentName(String argumentName, Format newFormat)
/*      */   {
/*  714 */     int argNumber = MessagePattern.validateArgumentName(argumentName);
/*  715 */     if (argNumber < -1) {
/*  716 */       return;
/*      */     }
/*  718 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  719 */       if (argNameMatches(partIndex + 1, argumentName, argNumber)) {
/*  720 */         setCustomArgStartFormat(partIndex, newFormat);
/*      */       }
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
/*      */   public void setFormat(int formatElementIndex, Format newFormat)
/*      */   {
/*  744 */     int formatNumber = 0;
/*  745 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  746 */       if (formatNumber == formatElementIndex) {
/*  747 */         setCustomArgStartFormat(partIndex, newFormat);
/*  748 */         return;
/*      */       }
/*  750 */       formatNumber++;
/*      */     }
/*  752 */     throw new ArrayIndexOutOfBoundsException(formatElementIndex);
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
/*      */   public Format[] getFormatsByArgumentIndex()
/*      */   {
/*  780 */     if (this.msgPattern.hasNamedArguments()) {
/*  781 */       throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
/*      */     }
/*      */     
/*      */ 
/*  785 */     ArrayList<Format> list = new ArrayList();
/*  786 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  787 */       int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
/*  788 */       while (argNumber >= list.size()) {
/*  789 */         list.add(null);
/*      */       }
/*  791 */       list.set(argNumber, this.cachedFormatters == null ? null : (Format)this.cachedFormatters.get(Integer.valueOf(partIndex)));
/*      */     }
/*  793 */     return (Format[])list.toArray(new Format[list.size()]);
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
/*      */   public Format[] getFormats()
/*      */   {
/*  818 */     ArrayList<Format> list = new ArrayList();
/*  819 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  820 */       list.add(this.cachedFormatters == null ? null : (Format)this.cachedFormatters.get(Integer.valueOf(partIndex)));
/*      */     }
/*  822 */     return (Format[])list.toArray(new Format[list.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getArgumentNames()
/*      */   {
/*  833 */     Set<String> result = new HashSet();
/*  834 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  835 */       result.add(getArgName(partIndex + 1));
/*      */     }
/*  837 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Format getFormatByArgumentName(String argumentName)
/*      */   {
/*  849 */     if (this.cachedFormatters == null) {
/*  850 */       return null;
/*      */     }
/*  852 */     int argNumber = MessagePattern.validateArgumentName(argumentName);
/*  853 */     if (argNumber < -1) {
/*  854 */       return null;
/*      */     }
/*  856 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/*  857 */       if (argNameMatches(partIndex + 1, argumentName, argNumber)) {
/*  858 */         return (Format)this.cachedFormatters.get(Integer.valueOf(partIndex));
/*      */       }
/*      */     }
/*  861 */     return null;
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
/*      */   public final StringBuffer format(Object[] arguments, StringBuffer result, FieldPosition pos)
/*      */   {
/*  937 */     format(arguments, null, new AppendableWrapper(result), pos);
/*  938 */     return result;
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
/*      */   public final StringBuffer format(Map<String, Object> arguments, StringBuffer result, FieldPosition pos)
/*      */   {
/*  971 */     format(null, arguments, new AppendableWrapper(result), pos);
/*  972 */     return result;
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
/*      */   public static String format(String pattern, Object... arguments)
/*      */   {
/*  992 */     MessageFormat temp = new MessageFormat(pattern);
/*  993 */     return temp.format(arguments);
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
/*      */   public static String format(String pattern, Map<String, Object> arguments)
/*      */   {
/* 1010 */     MessageFormat temp = new MessageFormat(pattern);
/* 1011 */     return temp.format(arguments);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean usesNamedArguments()
/*      */   {
/* 1022 */     return this.msgPattern.hasNamedArguments();
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
/*      */   public final StringBuffer format(Object arguments, StringBuffer result, FieldPosition pos)
/*      */   {
/* 1053 */     format(arguments, new AppendableWrapper(result), pos);
/* 1054 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object arguments)
/*      */   {
/* 1094 */     if (arguments == null) {
/* 1095 */       throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
/*      */     }
/*      */     
/* 1098 */     StringBuilder result = new StringBuilder();
/* 1099 */     AppendableWrapper wrapper = new AppendableWrapper(result);
/* 1100 */     wrapper.useAttributes();
/* 1101 */     format(arguments, wrapper, null);
/* 1102 */     AttributedString as = new AttributedString(result.toString());
/* 1103 */     for (AttributeAndPosition a : wrapper.attributes) {
/* 1104 */       as.addAttribute(a.key, a.value, a.start, a.limit);
/*      */     }
/* 1106 */     return as.getIterator();
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
/*      */ 
/*      */   public Object[] parse(String source, ParsePosition pos)
/*      */   {
/* 1140 */     if (this.msgPattern.hasNamedArguments()) {
/* 1141 */       throw new IllegalArgumentException("This method is not available in MessageFormat objects that use named argument.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1147 */     int maxArgId = -1;
/* 1148 */     for (int partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/* 1149 */       int argNumber = this.msgPattern.getPart(partIndex + 1).getValue();
/* 1150 */       if (argNumber > maxArgId) {
/* 1151 */         maxArgId = argNumber;
/*      */       }
/*      */     }
/* 1154 */     Object[] resultArray = new Object[maxArgId + 1];
/*      */     
/* 1156 */     int backupStartPos = pos.getIndex();
/* 1157 */     parse(0, source, pos, resultArray, null);
/* 1158 */     if (pos.getIndex() == backupStartPos) {
/* 1159 */       return null;
/*      */     }
/*      */     
/* 1162 */     return resultArray;
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
/*      */   public Map<String, Object> parseToMap(String source, ParsePosition pos)
/*      */   {
/* 1179 */     Map<String, Object> result = new HashMap();
/* 1180 */     int backupStartPos = pos.getIndex();
/* 1181 */     parse(0, source, pos, null, result);
/* 1182 */     if (pos.getIndex() == backupStartPos) {
/* 1183 */       return null;
/*      */     }
/* 1185 */     return result;
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
/*      */   public Object[] parse(String source)
/*      */     throws ParseException
/*      */   {
/* 1203 */     ParsePosition pos = new ParsePosition(0);
/* 1204 */     Object[] result = parse(source, pos);
/* 1205 */     if (pos.getIndex() == 0) {
/* 1206 */       throw new ParseException("MessageFormat parse error!", pos.getErrorIndex());
/*      */     }
/*      */     
/* 1209 */     return result;
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
/*      */   private void parse(int msgStart, String source, ParsePosition pos, Object[] args, Map<String, Object> argsMap)
/*      */   {
/* 1229 */     if (source == null) {
/* 1230 */       return;
/*      */     }
/* 1232 */     String msgString = this.msgPattern.getPatternString();
/* 1233 */     int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
/* 1234 */     int sourceOffset = pos.getIndex();
/* 1235 */     ParsePosition tempStatus = new ParsePosition(0);
/*      */     
/* 1237 */     for (int i = msgStart + 1;; i++) {
/* 1238 */       MessagePattern.Part part = this.msgPattern.getPart(i);
/* 1239 */       MessagePattern.Part.Type type = part.getType();
/* 1240 */       int index = part.getIndex();
/*      */       
/* 1242 */       int len = index - prevIndex;
/* 1243 */       if ((len == 0) || (msgString.regionMatches(prevIndex, source, sourceOffset, len))) {
/* 1244 */         sourceOffset += len;
/* 1245 */         prevIndex += len;
/*      */       } else {
/* 1247 */         pos.setErrorIndex(sourceOffset);
/* 1248 */         return;
/*      */       }
/* 1250 */       if (type == MessagePattern.Part.Type.MSG_LIMIT)
/*      */       {
/* 1252 */         pos.setIndex(sourceOffset);
/* 1253 */         return;
/*      */       }
/* 1255 */       if ((type == MessagePattern.Part.Type.SKIP_SYNTAX) || (type == MessagePattern.Part.Type.INSERT_CHAR)) {
/* 1256 */         prevIndex = part.getLimit();
/*      */       }
/*      */       else
/*      */       {
/* 1260 */         assert (type == MessagePattern.Part.Type.ARG_START) : ("Unexpected Part " + part + " in parsed message.");
/* 1261 */         int argLimit = this.msgPattern.getLimitPartIndex(i);
/*      */         
/* 1263 */         MessagePattern.ArgType argType = part.getArgType();
/* 1264 */         part = this.msgPattern.getPart(++i);
/*      */         
/* 1266 */         Object argId = null;
/* 1267 */         int argNumber = 0;
/* 1268 */         String key = null;
/* 1269 */         if (args != null) {
/* 1270 */           argNumber = part.getValue();
/* 1271 */           argId = new Integer(argNumber);
/*      */         } else {
/* 1273 */           if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
/* 1274 */             key = this.msgPattern.getSubstring(part);
/*      */           } else {
/* 1276 */             key = Integer.toString(part.getValue());
/*      */           }
/* 1278 */           argId = key;
/*      */         }
/*      */         
/* 1281 */         i++;
/* 1282 */         Format formatter = null;
/* 1283 */         boolean haveArgResult = false;
/* 1284 */         Object argResult = null;
/* 1285 */         if ((this.cachedFormatters != null) && ((formatter = (Format)this.cachedFormatters.get(Integer.valueOf(i - 2))) != null))
/*      */         {
/* 1287 */           tempStatus.setIndex(sourceOffset);
/* 1288 */           argResult = formatter.parseObject(source, tempStatus);
/* 1289 */           if (tempStatus.getIndex() == sourceOffset) {
/* 1290 */             pos.setErrorIndex(sourceOffset);
/* 1291 */             return;
/*      */           }
/* 1293 */           haveArgResult = true;
/* 1294 */           sourceOffset = tempStatus.getIndex();
/* 1295 */         } else if ((argType == MessagePattern.ArgType.NONE) || ((this.cachedFormatters != null) && (this.cachedFormatters.containsKey(Integer.valueOf(i - 2)))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1302 */           String stringAfterArgument = getLiteralStringUntilNextArgument(argLimit);
/*      */           int next;
/* 1304 */           int next; if (stringAfterArgument.length() != 0) {
/* 1305 */             next = source.indexOf(stringAfterArgument, sourceOffset);
/*      */           } else {
/* 1307 */             next = source.length();
/*      */           }
/* 1309 */           if (next < 0) {
/* 1310 */             pos.setErrorIndex(sourceOffset);
/* 1311 */             return;
/*      */           }
/* 1313 */           String strValue = source.substring(sourceOffset, next);
/* 1314 */           if (!strValue.equals("{" + argId.toString() + "}")) {
/* 1315 */             haveArgResult = true;
/* 1316 */             argResult = strValue;
/*      */           }
/* 1318 */           sourceOffset = next;
/*      */         }
/* 1320 */         else if (argType == MessagePattern.ArgType.CHOICE) {
/* 1321 */           tempStatus.setIndex(sourceOffset);
/* 1322 */           double choiceResult = parseChoiceArgument(this.msgPattern, i, source, tempStatus);
/* 1323 */           if (tempStatus.getIndex() == sourceOffset) {
/* 1324 */             pos.setErrorIndex(sourceOffset);
/* 1325 */             return;
/*      */           }
/* 1327 */           argResult = Double.valueOf(choiceResult);
/* 1328 */           haveArgResult = true;
/* 1329 */           sourceOffset = tempStatus.getIndex();
/* 1330 */         } else { if ((argType == MessagePattern.ArgType.PLURAL) || (argType == MessagePattern.ArgType.SELECT))
/*      */           {
/* 1332 */             throw new UnsupportedOperationException(argType == MessagePattern.ArgType.PLURAL ? "Parsing of PluralFormat is not supported." : "Parsing of SelectFormat is not supported.");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1337 */           throw new IllegalStateException("unexpected argType " + argType);
/*      */         }
/* 1339 */         if (haveArgResult) {
/* 1340 */           if (args != null) {
/* 1341 */             args[argNumber] = argResult;
/* 1342 */           } else if (argsMap != null) {
/* 1343 */             argsMap.put(key, argResult);
/*      */           }
/*      */         }
/* 1346 */         prevIndex = this.msgPattern.getPart(argLimit).getLimit();
/* 1347 */         i = argLimit;
/*      */       }
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
/*      */   public Map<String, Object> parseToMap(String source)
/*      */     throws ParseException
/*      */   {
/* 1366 */     ParsePosition pos = new ParsePosition(0);
/* 1367 */     Map<String, Object> result = new HashMap();
/* 1368 */     parse(0, source, pos, null, result);
/* 1369 */     if (pos.getIndex() == 0) {
/* 1370 */       throw new ParseException("MessageFormat parse error!", pos.getErrorIndex());
/*      */     }
/*      */     
/* 1373 */     return result;
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
/*      */   public Object parseObject(String source, ParsePosition pos)
/*      */   {
/* 1404 */     if (!this.msgPattern.hasNamedArguments()) {
/* 1405 */       return parse(source, pos);
/*      */     }
/* 1407 */     return parseToMap(source, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1417 */     MessageFormat other = (MessageFormat)super.clone();
/*      */     
/* 1419 */     if (this.customFormatArgStarts != null) {
/* 1420 */       other.customFormatArgStarts = new HashSet();
/* 1421 */       for (Integer key : this.customFormatArgStarts) {
/* 1422 */         other.customFormatArgStarts.add(key);
/*      */       }
/*      */     } else {
/* 1425 */       other.customFormatArgStarts = null;
/*      */     }
/*      */     
/* 1428 */     if (this.cachedFormatters != null) {
/* 1429 */       other.cachedFormatters = new HashMap();
/* 1430 */       Iterator<Map.Entry<Integer, Format>> it = this.cachedFormatters.entrySet().iterator();
/* 1431 */       while (it.hasNext()) {
/* 1432 */         Map.Entry<Integer, Format> entry = (Map.Entry)it.next();
/* 1433 */         other.cachedFormatters.put(entry.getKey(), entry.getValue());
/*      */       }
/*      */     } else {
/* 1436 */       other.cachedFormatters = null;
/*      */     }
/*      */     
/* 1439 */     other.msgPattern = (this.msgPattern == null ? null : (MessagePattern)this.msgPattern.clone());
/* 1440 */     other.stockDateFormatter = (this.stockDateFormatter == null ? null : (Format)this.stockDateFormatter.clone());
/* 1441 */     other.stockNumberFormatter = (this.stockNumberFormatter == null ? null : (Format)this.stockNumberFormatter.clone());
/*      */     
/* 1443 */     other.pluralProvider = null;
/* 1444 */     return other;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1453 */     if (this == obj)
/* 1454 */       return true;
/* 1455 */     if ((obj == null) || (getClass() != obj.getClass()))
/* 1456 */       return false;
/* 1457 */     MessageFormat other = (MessageFormat)obj;
/* 1458 */     return (Utility.objectEquals(this.ulocale, other.ulocale)) && (Utility.objectEquals(this.msgPattern, other.msgPattern)) && (Utility.objectEquals(this.cachedFormatters, other.cachedFormatters)) && (Utility.objectEquals(this.customFormatArgStarts, other.customFormatArgStarts));
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
/* 1472 */     return this.msgPattern.getPatternString().hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Field
/*      */     extends Format.Field
/*      */   {
/*      */     private static final long serialVersionUID = 7510380454602616157L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Field(String name)
/*      */     {
/* 1494 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/* 1506 */       if (getClass() != Field.class) {
/* 1507 */         throw new InvalidObjectException("A subclass of MessageFormat.Field must implement readResolve.");
/*      */       }
/*      */       
/* 1510 */       if (getName().equals(ARGUMENT.getName())) {
/* 1511 */         return ARGUMENT;
/*      */       }
/* 1513 */       throw new InvalidObjectException("Unknown attribute name.");
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
/* 1526 */     public static final Field ARGUMENT = new Field("message argument field");
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
/*      */   private void format(int msgStart, double pluralNumber, Object[] args, Map<String, Object> argsMap, AppendableWrapper dest, FieldPosition fp)
/*      */   {
/* 1585 */     String msgString = this.msgPattern.getPatternString();
/* 1586 */     int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
/* 1587 */     for (int i = msgStart + 1;; i++) {
/* 1588 */       MessagePattern.Part part = this.msgPattern.getPart(i);
/* 1589 */       MessagePattern.Part.Type type = part.getType();
/* 1590 */       int index = part.getIndex();
/* 1591 */       dest.append(msgString, prevIndex, index);
/* 1592 */       if (type == MessagePattern.Part.Type.MSG_LIMIT) {
/* 1593 */         return;
/*      */       }
/* 1595 */       prevIndex = part.getLimit();
/* 1596 */       if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
/* 1597 */         if (this.stockNumberFormatter == null) {
/* 1598 */           this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
/*      */         }
/* 1600 */         dest.formatAndAppend(this.stockNumberFormatter, Double.valueOf(pluralNumber));
/*      */ 
/*      */       }
/* 1603 */       else if (type == MessagePattern.Part.Type.ARG_START)
/*      */       {
/*      */ 
/* 1606 */         int argLimit = this.msgPattern.getLimitPartIndex(i);
/* 1607 */         MessagePattern.ArgType argType = part.getArgType();
/* 1608 */         part = this.msgPattern.getPart(++i);
/*      */         
/* 1610 */         String noArg = null;
/* 1611 */         Object argId = null;
/* 1612 */         Object arg; if (args != null) {
/* 1613 */           int argNumber = part.getValue();
/* 1614 */           if (dest.attributes != null)
/*      */           {
/* 1616 */             argId = new Integer(argNumber); }
/*      */           Object arg;
/* 1618 */           if ((0 <= argNumber) && (argNumber < args.length)) {
/* 1619 */             arg = args[argNumber];
/*      */           } else {
/* 1621 */             Object arg = null;
/* 1622 */             noArg = "{" + argNumber + "}";
/*      */           }
/*      */         } else { String key;
/*      */           String key;
/* 1626 */           if (part.getType() == MessagePattern.Part.Type.ARG_NAME) {
/* 1627 */             key = this.msgPattern.getSubstring(part);
/*      */           } else {
/* 1629 */             key = Integer.toString(part.getValue());
/*      */           }
/* 1631 */           argId = key;
/* 1632 */           Object arg; if ((argsMap != null) && (argsMap.containsKey(key))) {
/* 1633 */             arg = argsMap.get(key);
/*      */           } else {
/* 1635 */             arg = null;
/* 1636 */             noArg = "{" + key + "}";
/*      */           }
/*      */         }
/* 1639 */         i++;
/* 1640 */         int prevDestLength = dest.length;
/* 1641 */         Format formatter = null;
/* 1642 */         if (noArg != null) {
/* 1643 */           dest.append(noArg);
/* 1644 */         } else if (arg == null) {
/* 1645 */           dest.append("null");
/* 1646 */         } else if ((this.cachedFormatters != null) && ((formatter = (Format)this.cachedFormatters.get(Integer.valueOf(i - 2))) != null))
/*      */         {
/* 1648 */           if (((formatter instanceof ChoiceFormat)) || ((formatter instanceof PluralFormat)) || ((formatter instanceof SelectFormat)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1653 */             String subMsgString = formatter.format(arg);
/* 1654 */             if ((subMsgString.indexOf('{') >= 0) || ((subMsgString.indexOf('\'') >= 0) && (!this.msgPattern.jdkAposMode())))
/*      */             {
/* 1656 */               MessageFormat subMsgFormat = new MessageFormat(subMsgString, this.ulocale);
/* 1657 */               subMsgFormat.format(0, 0.0D, args, argsMap, dest, null);
/* 1658 */             } else if (dest.attributes == null) {
/* 1659 */               dest.append(subMsgString);
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/* 1667 */               dest.formatAndAppend(formatter, arg);
/*      */             }
/*      */           } else {
/* 1670 */             dest.formatAndAppend(formatter, arg);
/*      */           }
/* 1672 */         } else if ((argType == MessagePattern.ArgType.NONE) || ((this.cachedFormatters != null) && (this.cachedFormatters.containsKey(Integer.valueOf(i - 2)))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1677 */           if ((arg instanceof Number))
/*      */           {
/* 1679 */             if (this.stockNumberFormatter == null) {
/* 1680 */               this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
/*      */             }
/* 1682 */             dest.formatAndAppend(this.stockNumberFormatter, arg);
/* 1683 */           } else if ((arg instanceof Date))
/*      */           {
/* 1685 */             if (this.stockDateFormatter == null) {
/* 1686 */               this.stockDateFormatter = DateFormat.getDateTimeInstance(3, 3, this.ulocale);
/*      */             }
/*      */             
/* 1689 */             dest.formatAndAppend(this.stockDateFormatter, arg);
/*      */           } else {
/* 1691 */             dest.append(arg.toString());
/*      */           }
/* 1693 */         } else if (argType == MessagePattern.ArgType.CHOICE) {
/* 1694 */           if (!(arg instanceof Number)) {
/* 1695 */             throw new IllegalArgumentException("'" + arg + "' is not a Number");
/*      */           }
/* 1697 */           double number = ((Number)arg).doubleValue();
/* 1698 */           int subMsgStart = findChoiceSubMessage(this.msgPattern, i, number);
/* 1699 */           formatComplexSubMessage(subMsgStart, 0.0D, args, argsMap, dest);
/* 1700 */         } else if (argType == MessagePattern.ArgType.PLURAL) {
/* 1701 */           if (!(arg instanceof Number)) {
/* 1702 */             throw new IllegalArgumentException("'" + arg + "' is not a Number");
/*      */           }
/* 1704 */           double number = ((Number)arg).doubleValue();
/* 1705 */           if (this.pluralProvider == null) {
/* 1706 */             this.pluralProvider = new PluralSelectorProvider(this.ulocale);
/*      */           }
/* 1708 */           int subMsgStart = PluralFormat.findSubMessage(this.msgPattern, i, this.pluralProvider, number);
/* 1709 */           double offset = this.msgPattern.getPluralOffset(subMsgStart);
/* 1710 */           formatComplexSubMessage(subMsgStart, number - offset, args, argsMap, dest);
/* 1711 */         } else if (argType == MessagePattern.ArgType.SELECT) {
/* 1712 */           int subMsgStart = SelectFormat.findSubMessage(this.msgPattern, i, arg.toString());
/* 1713 */           formatComplexSubMessage(subMsgStart, 0.0D, args, argsMap, dest);
/*      */         }
/*      */         else {
/* 1716 */           throw new IllegalStateException("unexpected argType " + argType);
/*      */         }
/* 1718 */         fp = updateMetaData(dest, prevDestLength, fp, argId);
/* 1719 */         prevIndex = this.msgPattern.getPart(argLimit).getLimit();
/* 1720 */         i = argLimit;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void formatComplexSubMessage(int msgStart, double pluralNumber, Object[] args, Map<String, Object> argsMap, AppendableWrapper dest)
/*      */   {
/* 1728 */     if (!this.msgPattern.jdkAposMode()) {
/* 1729 */       format(msgStart, pluralNumber, args, argsMap, dest, null);
/* 1730 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1737 */     String msgString = this.msgPattern.getPatternString();
/*      */     
/* 1739 */     StringBuilder sb = null;
/* 1740 */     int prevIndex = this.msgPattern.getPart(msgStart).getLimit();
/* 1741 */     int i = msgStart;
/* 1742 */     for (;;) { MessagePattern.Part part = this.msgPattern.getPart(++i);
/* 1743 */       MessagePattern.Part.Type type = part.getType();
/* 1744 */       int index = part.getIndex();
/* 1745 */       if (type == MessagePattern.Part.Type.MSG_LIMIT) {
/* 1746 */         if (sb == null) {
/* 1747 */           String subMsgString = msgString.substring(prevIndex, index); break;
/*      */         }
/* 1749 */         String subMsgString = sb.append(msgString, prevIndex, index).toString();
/*      */         
/* 1751 */         break; }
/* 1752 */       if ((type == MessagePattern.Part.Type.REPLACE_NUMBER) || (type == MessagePattern.Part.Type.SKIP_SYNTAX)) {
/* 1753 */         if (sb == null) {
/* 1754 */           sb = new StringBuilder();
/*      */         }
/* 1756 */         sb.append(msgString, prevIndex, index);
/* 1757 */         if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
/* 1758 */           if (this.stockNumberFormatter == null) {
/* 1759 */             this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
/*      */           }
/* 1761 */           sb.append(this.stockNumberFormatter.format(Double.valueOf(pluralNumber)));
/*      */         }
/* 1763 */         prevIndex = part.getLimit();
/* 1764 */       } else if (type == MessagePattern.Part.Type.ARG_START) {
/* 1765 */         if (sb == null) {
/* 1766 */           sb = new StringBuilder();
/*      */         }
/* 1768 */         sb.append(msgString, prevIndex, index);
/* 1769 */         prevIndex = index;
/* 1770 */         i = this.msgPattern.getLimitPartIndex(i);
/* 1771 */         index = this.msgPattern.getPart(i).getLimit();
/* 1772 */         MessagePattern.appendReducedApostrophes(msgString, prevIndex, index, sb);
/* 1773 */         prevIndex = index;
/*      */       } }
/*      */     String subMsgString;
/* 1776 */     if (subMsgString.indexOf('{') >= 0) {
/* 1777 */       MessageFormat subMsgFormat = new MessageFormat("", this.ulocale);
/* 1778 */       subMsgFormat.applyPattern(subMsgString, MessagePattern.ApostropheMode.DOUBLE_REQUIRED);
/* 1779 */       subMsgFormat.format(0, 0.0D, args, argsMap, dest, null);
/*      */     } else {
/* 1781 */       dest.append(subMsgString);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getLiteralStringUntilNextArgument(int from)
/*      */   {
/* 1793 */     StringBuilder b = new StringBuilder();
/* 1794 */     String msgString = this.msgPattern.getPatternString();
/* 1795 */     int prevIndex = this.msgPattern.getPart(from).getLimit();
/* 1796 */     for (int i = from + 1;; i++) {
/* 1797 */       MessagePattern.Part part = this.msgPattern.getPart(i);
/* 1798 */       MessagePattern.Part.Type type = part.getType();
/* 1799 */       int index = part.getIndex();
/* 1800 */       b.append(msgString, prevIndex, index);
/* 1801 */       if ((type == MessagePattern.Part.Type.ARG_START) || (type == MessagePattern.Part.Type.MSG_LIMIT)) {
/* 1802 */         return b.toString();
/*      */       }
/*      */       
/* 1805 */       assert ((type == MessagePattern.Part.Type.SKIP_SYNTAX) || (type == MessagePattern.Part.Type.INSERT_CHAR)) : ("Unexpected Part " + part + " in parsed message.");
/* 1806 */       prevIndex = part.getLimit();
/*      */     }
/*      */   }
/*      */   
/*      */   private FieldPosition updateMetaData(AppendableWrapper dest, int prevLength, FieldPosition fp, Object argId)
/*      */   {
/* 1812 */     if ((dest.attributes != null) && (prevLength < dest.length)) {
/* 1813 */       dest.attributes.add(new AttributeAndPosition(argId, prevLength, dest.length));
/*      */     }
/* 1815 */     if ((fp != null) && (Field.ARGUMENT.equals(fp.getFieldAttribute()))) {
/* 1816 */       fp.setBeginIndex(prevLength);
/* 1817 */       fp.setEndIndex(dest.length);
/* 1818 */       return null;
/*      */     }
/* 1820 */     return fp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int findChoiceSubMessage(MessagePattern pattern, int partIndex, double number)
/*      */   {
/* 1832 */     int count = pattern.countParts();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1837 */     partIndex += 2;
/*      */     int msgStart;
/*      */     for (;;) {
/* 1840 */       msgStart = partIndex;
/* 1841 */       partIndex = pattern.getLimitPartIndex(partIndex);
/* 1842 */       partIndex++; if (partIndex >= count) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 1847 */       MessagePattern.Part part = pattern.getPart(partIndex++);
/* 1848 */       MessagePattern.Part.Type type = part.getType();
/* 1849 */       if (type == MessagePattern.Part.Type.ARG_LIMIT) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1855 */       assert (type.hasNumericValue());
/* 1856 */       double boundary = pattern.getNumericValue(part);
/*      */       
/* 1858 */       int selectorIndex = pattern.getPatternIndex(partIndex++);
/* 1859 */       char boundaryChar = pattern.getPatternString().charAt(selectorIndex);
/* 1860 */       if (boundaryChar == '<' ? number <= boundary : number < boundary) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1868 */     return msgStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static double parseChoiceArgument(MessagePattern pattern, int partIndex, String source, ParsePosition pos)
/*      */   {
/* 1876 */     int start = pos.getIndex();
/* 1877 */     int furthest = start;
/* 1878 */     double bestNumber = NaN.0D;
/* 1879 */     double tempNumber = 0.0D;
/* 1880 */     while (pattern.getPartType(partIndex) != MessagePattern.Part.Type.ARG_LIMIT) {
/* 1881 */       tempNumber = pattern.getNumericValue(pattern.getPart(partIndex));
/* 1882 */       partIndex += 2;
/* 1883 */       int msgLimit = pattern.getLimitPartIndex(partIndex);
/* 1884 */       int len = matchStringUntilLimitPart(pattern, partIndex, msgLimit, source, start);
/* 1885 */       if (len >= 0) {
/* 1886 */         int newIndex = start + len;
/* 1887 */         if (newIndex > furthest) {
/* 1888 */           furthest = newIndex;
/* 1889 */           bestNumber = tempNumber;
/* 1890 */           if (furthest == source.length()) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1895 */       partIndex = msgLimit + 1;
/*      */     }
/* 1897 */     if (furthest == start) {
/* 1898 */       pos.setErrorIndex(start);
/*      */     } else {
/* 1900 */       pos.setIndex(furthest);
/*      */     }
/* 1902 */     return bestNumber;
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
/*      */   private static int matchStringUntilLimitPart(MessagePattern pattern, int partIndex, int limitPartIndex, String source, int sourceOffset)
/*      */   {
/* 1916 */     int matchingSourceLength = 0;
/* 1917 */     String msgString = pattern.getPatternString();
/* 1918 */     int prevIndex = pattern.getPart(partIndex).getLimit();
/*      */     for (;;) {
/* 1920 */       MessagePattern.Part part = pattern.getPart(++partIndex);
/* 1921 */       if ((partIndex == limitPartIndex) || (part.getType() == MessagePattern.Part.Type.SKIP_SYNTAX)) {
/* 1922 */         int index = part.getIndex();
/* 1923 */         int length = index - prevIndex;
/* 1924 */         if ((length != 0) && (!source.regionMatches(sourceOffset, msgString, prevIndex, length))) {
/* 1925 */           return -1;
/*      */         }
/* 1927 */         matchingSourceLength += length;
/* 1928 */         if (partIndex == limitPartIndex) {
/* 1929 */           return matchingSourceLength;
/*      */         }
/* 1931 */         prevIndex = part.getLimit();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class PluralSelectorProvider
/*      */     implements PluralFormat.PluralSelector
/*      */   {
/*      */     private ULocale locale;
/*      */     
/*      */     private PluralRules rules;
/*      */     
/* 1944 */     public PluralSelectorProvider(ULocale loc) { this.locale = loc; }
/*      */     
/*      */     public String select(double number) {
/* 1947 */       if (this.rules == null) {
/* 1948 */         this.rules = PluralRules.forLocale(this.locale);
/*      */       }
/* 1950 */       return this.rules.select(number);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void format(Object arguments, AppendableWrapper result, FieldPosition fp)
/*      */   {
/* 1958 */     if ((arguments == null) || ((arguments instanceof Map))) {
/* 1959 */       format(null, (Map)arguments, result, fp);
/*      */     } else {
/* 1961 */       format((Object[])arguments, null, result, fp);
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
/*      */   private void format(Object[] arguments, Map<String, Object> argsMap, AppendableWrapper dest, FieldPosition fp)
/*      */   {
/* 1974 */     if ((arguments != null) && (this.msgPattern.hasNamedArguments())) {
/* 1975 */       throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
/*      */     }
/*      */     
/*      */ 
/* 1979 */     format(0, 0.0D, arguments, argsMap, dest, fp);
/*      */   }
/*      */   
/*      */   private void resetPattern() {
/* 1983 */     if (this.msgPattern != null) {
/* 1984 */       this.msgPattern.clear();
/*      */     }
/* 1986 */     if (this.cachedFormatters != null) {
/* 1987 */       this.cachedFormatters.clear();
/*      */     }
/* 1989 */     this.customFormatArgStarts = null;
/*      */   }
/*      */   
/* 1992 */   private static final String[] typeList = { "number", "date", "time", "spellout", "ordinal", "duration" };
/*      */   
/*      */   private static final int TYPE_NUMBER = 0;
/*      */   
/*      */   private static final int TYPE_DATE = 1;
/*      */   
/*      */   private static final int TYPE_TIME = 2;
/*      */   private static final int TYPE_SPELLOUT = 3;
/*      */   private static final int TYPE_ORDINAL = 4;
/*      */   private static final int TYPE_DURATION = 5;
/* 2002 */   private static final String[] modifierList = { "", "currency", "percent", "integer" };
/*      */   
/*      */   private static final int MODIFIER_EMPTY = 0;
/*      */   
/*      */   private static final int MODIFIER_CURRENCY = 1;
/*      */   
/*      */   private static final int MODIFIER_PERCENT = 2;
/*      */   
/*      */   private static final int MODIFIER_INTEGER = 3;
/* 2011 */   private static final String[] dateModifierList = { "", "short", "medium", "long", "full" };
/*      */   
/*      */   private static final int DATE_MODIFIER_EMPTY = 0;
/*      */   
/*      */   private static final int DATE_MODIFIER_SHORT = 1;
/*      */   
/*      */   private static final int DATE_MODIFIER_MEDIUM = 2;
/*      */   
/*      */   private static final int DATE_MODIFIER_LONG = 3;
/*      */   private static final int DATE_MODIFIER_FULL = 4;
/*      */   
/*      */   private Format createAppropriateFormat(String type, String style)
/*      */   {
/* 2024 */     Format newFormat = null;
/* 2025 */     int subformatType = findKeyword(type, typeList);
/* 2026 */     switch (subformatType) {
/*      */     case 0: 
/* 2028 */       switch (findKeyword(style, modifierList)) {
/*      */       case 0: 
/* 2030 */         newFormat = NumberFormat.getInstance(this.ulocale);
/* 2031 */         break;
/*      */       case 1: 
/* 2033 */         newFormat = NumberFormat.getCurrencyInstance(this.ulocale);
/* 2034 */         break;
/*      */       case 2: 
/* 2036 */         newFormat = NumberFormat.getPercentInstance(this.ulocale);
/* 2037 */         break;
/*      */       case 3: 
/* 2039 */         newFormat = NumberFormat.getIntegerInstance(this.ulocale);
/* 2040 */         break;
/*      */       default: 
/* 2042 */         newFormat = new DecimalFormat(style, new DecimalFormatSymbols(this.ulocale));
/*      */       }
/* 2044 */       break;
/*      */     
/*      */ 
/*      */     case 1: 
/* 2048 */       switch (findKeyword(style, dateModifierList)) {
/*      */       case 0: 
/* 2050 */         newFormat = DateFormat.getDateInstance(2, this.ulocale);
/* 2051 */         break;
/*      */       case 1: 
/* 2053 */         newFormat = DateFormat.getDateInstance(3, this.ulocale);
/* 2054 */         break;
/*      */       case 2: 
/* 2056 */         newFormat = DateFormat.getDateInstance(2, this.ulocale);
/* 2057 */         break;
/*      */       case 3: 
/* 2059 */         newFormat = DateFormat.getDateInstance(1, this.ulocale);
/* 2060 */         break;
/*      */       case 4: 
/* 2062 */         newFormat = DateFormat.getDateInstance(0, this.ulocale);
/* 2063 */         break;
/*      */       default: 
/* 2065 */         newFormat = new SimpleDateFormat(style, this.ulocale); }
/* 2066 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 2070 */       switch (findKeyword(style, dateModifierList)) {
/*      */       case 0: 
/* 2072 */         newFormat = DateFormat.getTimeInstance(2, this.ulocale);
/* 2073 */         break;
/*      */       case 1: 
/* 2075 */         newFormat = DateFormat.getTimeInstance(3, this.ulocale);
/* 2076 */         break;
/*      */       case 2: 
/* 2078 */         newFormat = DateFormat.getTimeInstance(2, this.ulocale);
/* 2079 */         break;
/*      */       case 3: 
/* 2081 */         newFormat = DateFormat.getTimeInstance(1, this.ulocale);
/* 2082 */         break;
/*      */       case 4: 
/* 2084 */         newFormat = DateFormat.getTimeInstance(0, this.ulocale);
/* 2085 */         break;
/*      */       default: 
/* 2087 */         newFormat = new SimpleDateFormat(style, this.ulocale); }
/* 2088 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 3: 
/* 2093 */       RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 1);
/*      */       
/* 2095 */       String ruleset = style.trim();
/* 2096 */       if (ruleset.length() != 0) {
/*      */         try {
/* 2098 */           rbnf.setDefaultRuleSet(ruleset);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/*      */ 
/* 2104 */       newFormat = rbnf;
/*      */       
/* 2106 */       break;
/*      */     
/*      */     case 4: 
/* 2109 */       RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 2);
/*      */       
/* 2111 */       String ruleset = style.trim();
/* 2112 */       if (ruleset.length() != 0) {
/*      */         try {
/* 2114 */           rbnf.setDefaultRuleSet(ruleset);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/*      */ 
/* 2120 */       newFormat = rbnf;
/*      */       
/* 2122 */       break;
/*      */     
/*      */     case 5: 
/* 2125 */       RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(this.ulocale, 3);
/*      */       
/* 2127 */       String ruleset = style.trim();
/* 2128 */       if (ruleset.length() != 0) {
/*      */         try {
/* 2130 */           rbnf.setDefaultRuleSet(ruleset);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/*      */ 
/* 2136 */       newFormat = rbnf;
/*      */       
/* 2138 */       break;
/*      */     default: 
/* 2140 */       throw new IllegalArgumentException("Unknown format type \"" + type + "\"");
/*      */     }
/* 2142 */     return newFormat;
/*      */   }
/*      */   
/* 2145 */   private static final Locale rootLocale = new Locale("");
/*      */   private static final char SINGLE_QUOTE = '\'';
/*      */   
/* 2148 */   private static final int findKeyword(String s, String[] list) { s = PatternProps.trimWhiteSpace(s).toLowerCase(rootLocale);
/* 2149 */     for (int i = 0; i < list.length; i++) {
/* 2150 */       if (s.equals(list[i]))
/* 2151 */         return i;
/*      */     }
/* 2153 */     return -1;
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
/*      */   private void writeObject(ObjectOutputStream out)
/*      */     throws IOException
/*      */   {
/* 2171 */     out.defaultWriteObject();
/*      */     
/*      */ 
/* 2174 */     out.writeObject(this.ulocale.toLanguageTag());
/*      */     
/* 2176 */     if (this.msgPattern == null) {
/* 2177 */       this.msgPattern = new MessagePattern();
/*      */     }
/* 2179 */     out.writeObject(this.msgPattern.getApostropheMode());
/*      */     
/* 2181 */     out.writeObject(this.msgPattern.getPatternString());
/*      */     int formatIndex;
/* 2183 */     int partIndex; if ((this.customFormatArgStarts == null) || (this.customFormatArgStarts.isEmpty())) {
/* 2184 */       out.writeInt(0);
/*      */     } else {
/* 2186 */       out.writeInt(this.customFormatArgStarts.size());
/* 2187 */       formatIndex = 0;
/* 2188 */       for (partIndex = 0; (partIndex = nextTopLevelArgStart(partIndex)) >= 0;) {
/* 2189 */         if (this.customFormatArgStarts.contains(Integer.valueOf(partIndex))) {
/* 2190 */           out.writeInt(formatIndex);
/* 2191 */           out.writeObject(this.cachedFormatters.get(Integer.valueOf(partIndex)));
/*      */         }
/* 2193 */         formatIndex++;
/*      */       }
/*      */     }
/*      */     
/* 2197 */     out.writeInt(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream in)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2205 */     in.defaultReadObject();
/*      */     
/* 2207 */     String languageTag = (String)in.readObject();
/* 2208 */     this.ulocale = ULocale.forLanguageTag(languageTag);
/* 2209 */     MessagePattern.ApostropheMode aposMode = (MessagePattern.ApostropheMode)in.readObject();
/* 2210 */     if ((this.msgPattern == null) || (aposMode != this.msgPattern.getApostropheMode())) {
/* 2211 */       this.msgPattern = new MessagePattern(aposMode);
/*      */     }
/* 2213 */     String msg = (String)in.readObject();
/* 2214 */     if (msg != null) {
/* 2215 */       applyPattern(msg);
/*      */     }
/*      */     
/* 2218 */     for (int numFormatters = in.readInt(); numFormatters > 0; numFormatters--) {
/* 2219 */       int formatIndex = in.readInt();
/* 2220 */       Format formatter = (Format)in.readObject();
/* 2221 */       setFormat(formatIndex, formatter);
/*      */     }
/*      */     
/* 2224 */     for (int numPairs = in.readInt(); numPairs > 0; numPairs--) {
/* 2225 */       in.readInt();
/* 2226 */       in.readObject();
/*      */     }
/*      */   }
/*      */   
/*      */   private void cacheExplicitFormats() {
/* 2231 */     if (this.cachedFormatters != null) {
/* 2232 */       this.cachedFormatters.clear();
/*      */     }
/* 2234 */     this.customFormatArgStarts = null;
/*      */     
/*      */ 
/* 2237 */     int limit = this.msgPattern.countParts() - 2;
/*      */     
/*      */ 
/* 2240 */     for (int i = 1; i < limit; i++) {
/* 2241 */       MessagePattern.Part part = this.msgPattern.getPart(i);
/* 2242 */       if (part.getType() == MessagePattern.Part.Type.ARG_START)
/*      */       {
/*      */ 
/* 2245 */         MessagePattern.ArgType argType = part.getArgType();
/* 2246 */         if (argType == MessagePattern.ArgType.SIMPLE)
/*      */         {
/*      */ 
/* 2249 */           int index = i;
/* 2250 */           i += 2;
/* 2251 */           String explicitType = this.msgPattern.getSubstring(this.msgPattern.getPart(i++));
/* 2252 */           String style = "";
/* 2253 */           if ((part = this.msgPattern.getPart(i)).getType() == MessagePattern.Part.Type.ARG_STYLE) {
/* 2254 */             style = this.msgPattern.getSubstring(part);
/* 2255 */             i++;
/*      */           }
/* 2257 */           Format formatter = createAppropriateFormat(explicitType, style);
/* 2258 */           setArgStartFormat(index, formatter);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void setArgStartFormat(int argStart, Format formatter)
/*      */   {
/* 2266 */     if (this.cachedFormatters == null) {
/* 2267 */       this.cachedFormatters = new HashMap();
/*      */     }
/* 2269 */     this.cachedFormatters.put(Integer.valueOf(argStart), formatter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setCustomArgStartFormat(int argStart, Format formatter)
/*      */   {
/* 2277 */     setArgStartFormat(argStart, formatter);
/* 2278 */     if (this.customFormatArgStarts == null) {
/* 2279 */       this.customFormatArgStarts = new HashSet();
/*      */     }
/* 2281 */     this.customFormatArgStarts.add(Integer.valueOf(argStart));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final char CURLY_BRACE_LEFT = '{';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final char CURLY_BRACE_RIGHT = '}';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STATE_INITIAL = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STATE_SINGLE_QUOTE = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STATE_IN_QUOTE = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STATE_MSG_ELEMENT = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String autoQuoteApostrophe(String pattern)
/*      */   {
/* 2322 */     StringBuilder buf = new StringBuilder(pattern.length() * 2);
/* 2323 */     int state = 0;
/* 2324 */     int braceCount = 0;
/* 2325 */     int i = 0; for (int j = pattern.length(); i < j; i++) {
/* 2326 */       char c = pattern.charAt(i);
/* 2327 */       switch (state) {
/*      */       case 0: 
/* 2329 */         switch (c) {
/*      */         case '\'': 
/* 2331 */           state = 1;
/* 2332 */           break;
/*      */         case '{': 
/* 2334 */           state = 3;
/* 2335 */           braceCount++;
/*      */         }
/*      */         
/* 2338 */         break;
/*      */       case 1: 
/* 2340 */         switch (c) {
/*      */         case '\'': 
/* 2342 */           state = 0;
/* 2343 */           break;
/*      */         case '{': 
/*      */         case '}': 
/* 2346 */           state = 2;
/* 2347 */           break;
/*      */         default: 
/* 2349 */           buf.append('\'');
/* 2350 */           state = 0; }
/* 2351 */         break;
/*      */       
/*      */ 
/*      */       case 2: 
/* 2355 */         switch (c) {
/*      */         case '\'': 
/* 2357 */           state = 0;
/*      */         }
/*      */         
/* 2360 */         break;
/*      */       case 3: 
/* 2362 */         switch (c) {
/*      */         case '{': 
/* 2364 */           braceCount++;
/* 2365 */           break;
/*      */         case '}': 
/* 2367 */           braceCount--; if (braceCount == 0) {
/* 2368 */             state = 0;
/*      */           }
/*      */           break;
/*      */         }
/* 2372 */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/* 2378 */       buf.append(c);
/*      */     }
/*      */     
/* 2381 */     if ((state == 1) || (state == 2)) {
/* 2382 */       buf.append('\'');
/*      */     }
/* 2384 */     return new String(buf);
/*      */   }
/*      */   
/*      */   private static final class AppendableWrapper
/*      */   {
/*      */     private Appendable app;
/*      */     private int length;
/*      */     private List<MessageFormat.AttributeAndPosition> attributes;
/*      */     
/*      */     public AppendableWrapper(StringBuilder sb) {
/* 2394 */       this.app = sb;
/* 2395 */       this.length = sb.length();
/* 2396 */       this.attributes = null;
/*      */     }
/*      */     
/*      */     public AppendableWrapper(StringBuffer sb) {
/* 2400 */       this.app = sb;
/* 2401 */       this.length = sb.length();
/* 2402 */       this.attributes = null;
/*      */     }
/*      */     
/*      */     public void useAttributes() {
/* 2406 */       this.attributes = new ArrayList();
/*      */     }
/*      */     
/*      */     public void append(CharSequence s) {
/*      */       try {
/* 2411 */         this.app.append(s);
/* 2412 */         this.length += s.length();
/*      */       } catch (IOException e) {
/* 2414 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void append(CharSequence s, int start, int limit) {
/*      */       try {
/* 2420 */         this.app.append(s, start, limit);
/* 2421 */         this.length += limit - start;
/*      */       } catch (IOException e) {
/* 2423 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void append(CharacterIterator iterator) {
/* 2428 */       this.length += append(this.app, iterator);
/*      */     }
/*      */     
/*      */     public static int append(Appendable result, CharacterIterator iterator) {
/*      */       try {
/* 2433 */         int start = iterator.getBeginIndex();
/* 2434 */         int limit = iterator.getEndIndex();
/* 2435 */         int length = limit - start;
/* 2436 */         if (start < limit) {
/* 2437 */           result.append(iterator.first());
/* 2438 */           for (;;) { start++; if (start >= limit) break;
/* 2439 */             result.append(iterator.next());
/*      */           }
/*      */         }
/* 2442 */         return length;
/*      */       } catch (IOException e) {
/* 2444 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void formatAndAppend(Format formatter, Object arg) {
/* 2449 */       if (this.attributes == null) {
/* 2450 */         append(formatter.format(arg));
/*      */       } else {
/* 2452 */         AttributedCharacterIterator formattedArg = formatter.formatToCharacterIterator(arg);
/* 2453 */         int prevLength = this.length;
/* 2454 */         append(formattedArg);
/*      */         
/* 2456 */         formattedArg.first();
/* 2457 */         int start = formattedArg.getIndex();
/* 2458 */         int limit = formattedArg.getEndIndex();
/* 2459 */         int offset = prevLength - start;
/* 2460 */         while (start < limit) {
/* 2461 */           Map<AttributedCharacterIterator.Attribute, Object> map = formattedArg.getAttributes();
/* 2462 */           int runLimit = formattedArg.getRunLimit();
/* 2463 */           if (map.size() != 0) {
/* 2464 */             for (Map.Entry<AttributedCharacterIterator.Attribute, Object> entry : map.entrySet()) {
/* 2465 */               this.attributes.add(new MessageFormat.AttributeAndPosition((AttributedCharacterIterator.Attribute)entry.getKey(), entry.getValue(), offset + start, offset + runLimit));
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2471 */           start = runLimit;
/* 2472 */           formattedArg.setIndex(start);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AttributeAndPosition
/*      */   {
/*      */     private AttributedCharacterIterator.Attribute key;
/*      */     private Object value;
/*      */     private int start;
/*      */     private int limit;
/*      */     
/*      */     public AttributeAndPosition(Object fieldValue, int startIndex, int limitIndex)
/*      */     {
/* 2487 */       init(MessageFormat.Field.ARGUMENT, fieldValue, startIndex, limitIndex);
/*      */     }
/*      */     
/*      */     public AttributeAndPosition(AttributedCharacterIterator.Attribute field, Object fieldValue, int startIndex, int limitIndex) {
/* 2491 */       init(field, fieldValue, startIndex, limitIndex);
/*      */     }
/*      */     
/*      */     public void init(AttributedCharacterIterator.Attribute field, Object fieldValue, int startIndex, int limitIndex) {
/* 2495 */       this.key = field;
/* 2496 */       this.value = fieldValue;
/* 2497 */       this.start = startIndex;
/* 2498 */       this.limit = limitIndex;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\MessageFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */