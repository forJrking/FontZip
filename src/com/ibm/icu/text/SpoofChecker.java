/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUData;
/*      */ import com.ibm.icu.impl.Trie2;
/*      */ import com.ibm.icu.impl.Trie2Writable;
/*      */ import com.ibm.icu.impl.Trie2_16;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.lang.UScript;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.LineNumberReader;
/*      */ import java.io.Reader;
/*      */ import java.text.ParseException;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Hashtable;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SpoofChecker
/*      */ {
/*      */   public static final int SINGLE_SCRIPT_CONFUSABLE = 1;
/*      */   public static final int MIXED_SCRIPT_CONFUSABLE = 2;
/*      */   public static final int WHOLE_SCRIPT_CONFUSABLE = 4;
/*      */   public static final int ANY_CASE = 8;
/*      */   public static final int SINGLE_SCRIPT = 16;
/*      */   public static final int INVISIBLE = 32;
/*      */   public static final int CHAR_LIMIT = 64;
/*      */   public static final int ALL_CHECKS = 127;
/*      */   static final int MAGIC = 944111087;
/*      */   private int fMagic;
/*      */   private int fChecks;
/*      */   private SpoofData fSpoofData;
/*      */   private Set<ULocale> fAllowedLocales;
/*      */   private UnicodeSet fAllowedCharsSet;
/*      */   static final int SL_TABLE_FLAG = 16777216;
/*      */   static final int SA_TABLE_FLAG = 33554432;
/*      */   static final int ML_TABLE_FLAG = 67108864;
/*      */   static final int MA_TABLE_FLAG = 134217728;
/*      */   static final int KEY_MULTIPLE_VALUES = 268435456;
/*      */   static final int KEY_LENGTH_SHIFT = 29;
/*      */   
/*      */   public static class Builder
/*      */   {
/*      */     int fMagic;
/*      */     int fChecks;
/*      */     SpoofChecker.SpoofData fSpoofData;
/*      */     UnicodeSet fAllowedCharsSet;
/*      */     Set<ULocale> fAllowedLocales;
/*      */     
/*      */     public Builder()
/*      */     {
/*  272 */       this.fMagic = 944111087;
/*  273 */       this.fChecks = 127;
/*  274 */       this.fSpoofData = null;
/*  275 */       this.fAllowedCharsSet = new UnicodeSet(0, 1114111);
/*  276 */       this.fAllowedLocales = new LinkedHashSet();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Builder(SpoofChecker src)
/*      */     {
/*  288 */       this.fMagic = src.fMagic;
/*  289 */       this.fChecks = src.fChecks;
/*  290 */       this.fSpoofData = null;
/*  291 */       this.fAllowedCharsSet = src.fAllowedCharsSet.cloneAsThawed();
/*  292 */       this.fAllowedLocales = new LinkedHashSet();
/*  293 */       this.fAllowedLocales.addAll(src.fAllowedLocales);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SpoofChecker build()
/*      */     {
/*  304 */       if (this.fSpoofData == null) {
/*      */         try {
/*  306 */           this.fSpoofData = SpoofChecker.SpoofData.getDefault();
/*      */         } catch (IOException e) {
/*  308 */           return null;
/*      */         }
/*      */       }
/*  311 */       if (!SpoofChecker.SpoofData.validateDataVersion(this.fSpoofData.fRawData)) {
/*  312 */         return null;
/*      */       }
/*  314 */       SpoofChecker result = new SpoofChecker(null);
/*  315 */       result.fMagic = this.fMagic;
/*  316 */       result.fChecks = this.fChecks;
/*  317 */       result.fSpoofData = this.fSpoofData;
/*  318 */       result.fAllowedCharsSet = ((UnicodeSet)this.fAllowedCharsSet.clone());
/*  319 */       result.fAllowedCharsSet.freeze();
/*  320 */       result.fAllowedLocales = this.fAllowedLocales;
/*  321 */       return result;
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
/*      */ 
/*      */     public Builder setData(Reader confusables, Reader confusablesWholeScript)
/*      */       throws ParseException, IOException
/*      */     {
/*  343 */       this.fSpoofData = new SpoofChecker.SpoofData();
/*  344 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  345 */       DataOutputStream os = new DataOutputStream(bos);
/*      */       
/*  347 */       ConfusabledataBuilder.buildConfusableData(this.fSpoofData, confusables);
/*  348 */       WSConfusableDataBuilder.buildWSConfusableData(this.fSpoofData, os, confusablesWholeScript);
/*  349 */       return this;
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
/*      */     public Builder setChecks(int checks)
/*      */     {
/*  365 */       if (0 != (checks & 0xFFFFFF80)) {
/*  366 */         throw new IllegalArgumentException("Bad Spoof Checks value.");
/*      */       }
/*  368 */       this.fChecks = (checks & 0x7F);
/*  369 */       return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Builder setAllowedLocales(Set<ULocale> locales)
/*      */     {
/*  402 */       this.fAllowedCharsSet.clear();
/*      */       
/*  404 */       for (ULocale locale : locales)
/*      */       {
/*      */ 
/*  407 */         addScriptChars(locale, this.fAllowedCharsSet);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  412 */       this.fAllowedLocales = new LinkedHashSet();
/*  413 */       if (locales.size() == 0) {
/*  414 */         this.fAllowedCharsSet.add(0, 1114111);
/*  415 */         this.fChecks &= 0xFFFFFFBF;
/*  416 */         return this;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  421 */       UnicodeSet tempSet = new UnicodeSet();
/*  422 */       tempSet.applyIntPropertyValue(4106, 0);
/*  423 */       this.fAllowedCharsSet.addAll(tempSet);
/*  424 */       tempSet.applyIntPropertyValue(4106, 1);
/*  425 */       this.fAllowedCharsSet.addAll(tempSet);
/*      */       
/*      */ 
/*  428 */       this.fAllowedLocales.addAll(locales);
/*  429 */       this.fChecks |= 0x40;
/*  430 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void addScriptChars(ULocale locale, UnicodeSet allowedChars)
/*      */     {
/*  437 */       int[] scripts = UScript.getCode(locale);
/*  438 */       UnicodeSet tmpSet = new UnicodeSet();
/*      */       
/*  440 */       for (int i = 0; i < scripts.length; i++) {
/*  441 */         tmpSet.applyIntPropertyValue(4106, scripts[i]);
/*  442 */         allowedChars.addAll(tmpSet);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Builder setAllowedChars(UnicodeSet chars)
/*      */     {
/*  462 */       this.fAllowedCharsSet = chars.cloneAsThawed();
/*  463 */       this.fAllowedLocales = new LinkedHashSet();
/*  464 */       this.fChecks |= 0x40;
/*  465 */       return this;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static class WSConfusableDataBuilder
/*      */     {
/*  505 */       static String parseExp = "(?m)^([ \\t]*(?:#.*?)?)$|^(?:\\s*([0-9A-F]{4,})(?:..([0-9A-F]{4,}))?\\s*;\\s*([A-Za-z]+)\\s*;\\s*([A-Za-z]+)\\s*;\\s*(?:(A)|(L))[ \\t]*(?:#.*?)?)$|^(.*?)$";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       static void readWholeFileToString(Reader reader, StringBuffer buffer)
/*      */         throws IOException
/*      */       {
/*  532 */         LineNumberReader lnr = new LineNumberReader(reader);
/*      */         for (;;) {
/*  534 */           String line = lnr.readLine();
/*  535 */           if (line == null) {
/*      */             break;
/*      */           }
/*  538 */           buffer.append(line);
/*  539 */           buffer.append('\n');
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       static void buildWSConfusableData(SpoofChecker.SpoofData fSpoofData, DataOutputStream os, Reader confusablesWS)
/*      */         throws ParseException, IOException
/*      */       {
/*  547 */         Pattern parseRegexp = null;
/*  548 */         StringBuffer input = new StringBuffer();
/*  549 */         int lineNum = 0;
/*      */         
/*  551 */         Vector<BuilderScriptSet> scriptSets = null;
/*  552 */         int rtScriptSetsCount = 2;
/*      */         
/*  554 */         Trie2Writable anyCaseTrie = new Trie2Writable(0, 0);
/*  555 */         Trie2Writable lowerCaseTrie = new Trie2Writable(0, 0);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */         scriptSets = new Vector();
/*  571 */         scriptSets.addElement(null);
/*  572 */         scriptSets.addElement(null);
/*      */         
/*  574 */         readWholeFileToString(confusablesWS, input);
/*      */         
/*  576 */         parseRegexp = Pattern.compile(parseExp);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  581 */         if (input.charAt(0) == 65279) {
/*  582 */           input.setCharAt(0, ' ');
/*      */         }
/*      */         
/*      */ 
/*  586 */         Matcher matcher = parseRegexp.matcher(input);
/*  587 */         while (matcher.find()) {
/*  588 */           lineNum++;
/*  589 */           if (matcher.start(1) < 0)
/*      */           {
/*      */ 
/*      */ 
/*  593 */             if (matcher.start(8) >= 0)
/*      */             {
/*  595 */               throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Unrecognized input: " + matcher.group(), matcher.start());
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  601 */             int startCodePoint = Integer.parseInt(matcher.group(2), 16);
/*  602 */             if (startCodePoint > 1114111) {
/*  603 */               throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": out of range code point: " + matcher.group(2), matcher.start(2));
/*      */             }
/*      */             
/*  606 */             int endCodePoint = startCodePoint;
/*  607 */             if (matcher.start(3) >= 0) {
/*  608 */               endCodePoint = Integer.parseInt(matcher.group(3), 16);
/*      */             }
/*  610 */             if (endCodePoint > 1114111) {
/*  611 */               throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": out of range code point: " + matcher.group(3), matcher.start(3));
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  616 */             String srcScriptName = matcher.group(4);
/*  617 */             String targScriptName = matcher.group(5);
/*  618 */             int srcScript = UCharacter.getPropertyValueEnum(4106, srcScriptName);
/*  619 */             int targScript = UCharacter.getPropertyValueEnum(4106, targScriptName);
/*  620 */             if (srcScript == -1) {
/*  621 */               throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Invalid script code t: " + matcher.group(4), matcher.start(4));
/*      */             }
/*      */             
/*  624 */             if (targScript == -1) {
/*  625 */               throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Invalid script code t: " + matcher.group(5), matcher.start(5));
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  630 */             Trie2Writable table = anyCaseTrie;
/*  631 */             if (matcher.start(7) >= 0) {
/*  632 */               table = lowerCaseTrie;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  645 */             for (int cp = startCodePoint; cp <= endCodePoint; cp++) {
/*  646 */               int setIndex = table.get(cp);
/*  647 */               BuilderScriptSet bsset = null;
/*  648 */               if (setIndex > 0) {
/*  649 */                 assert (setIndex < scriptSets.size());
/*  650 */                 bsset = (BuilderScriptSet)scriptSets.elementAt(setIndex);
/*      */               } else {
/*  652 */                 bsset = new BuilderScriptSet();
/*  653 */                 bsset.codePoint = cp;
/*  654 */                 bsset.trie = table;
/*  655 */                 bsset.sset = new SpoofChecker.ScriptSet();
/*  656 */                 setIndex = scriptSets.size();
/*  657 */                 bsset.index = setIndex;
/*  658 */                 bsset.rindex = 0;
/*  659 */                 scriptSets.addElement(bsset);
/*  660 */                 table.set(cp, setIndex);
/*      */               }
/*  662 */               bsset.sset.Union(targScript);
/*  663 */               bsset.sset.Union(srcScript);
/*      */               
/*  665 */               int cpScript = UScript.getScript(cp);
/*  666 */               if (cpScript != srcScript)
/*      */               {
/*  668 */                 throw new ParseException("ConfusablesWholeScript, line " + lineNum + ": Mismatch between source script and code point " + Integer.toString(cp, 16), matcher.start(5));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  684 */         rtScriptSetsCount = 2;
/*  685 */         for (int outeri = 2; outeri < scriptSets.size(); outeri++) {
/*  686 */           BuilderScriptSet outerSet = (BuilderScriptSet)scriptSets.elementAt(outeri);
/*  687 */           if (outerSet.index == outeri)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  693 */             outerSet.rindex = (rtScriptSetsCount++);
/*  694 */             for (int inneri = outeri + 1; inneri < scriptSets.size(); inneri++) {
/*  695 */               BuilderScriptSet innerSet = (BuilderScriptSet)scriptSets.elementAt(inneri);
/*  696 */               if ((outerSet.sset.equals(innerSet.sset)) && (outerSet.sset != innerSet.sset)) {
/*  697 */                 innerSet.sset = outerSet.sset;
/*  698 */                 innerSet.index = outeri;
/*  699 */                 innerSet.rindex = outerSet.rindex;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  715 */         for (int i = 2; i < scriptSets.size(); i++) {
/*  716 */           BuilderScriptSet bSet = (BuilderScriptSet)scriptSets.elementAt(i);
/*  717 */           if (bSet.rindex != i) {
/*  718 */             bSet.trie.set(bSet.codePoint, bSet.rindex);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  730 */         UnicodeSet ignoreSet = new UnicodeSet();
/*  731 */         ignoreSet.applyIntPropertyValue(4106, 0);
/*  732 */         UnicodeSet inheritedSet = new UnicodeSet();
/*  733 */         inheritedSet.applyIntPropertyValue(4106, 1);
/*  734 */         ignoreSet.addAll(inheritedSet);
/*  735 */         for (int rn = 0; rn < ignoreSet.getRangeCount(); rn++) {
/*  736 */           int rangeStart = ignoreSet.getRangeStart(rn);
/*  737 */           int rangeEnd = ignoreSet.getRangeEnd(rn);
/*  738 */           anyCaseTrie.setRange(rangeStart, rangeEnd, 1, true);
/*  739 */           lowerCaseTrie.setRange(rangeStart, rangeEnd, 1, true);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  745 */         anyCaseTrie.toTrie2_16().serialize(os);
/*  746 */         lowerCaseTrie.toTrie2_16().serialize(os);
/*      */         
/*  748 */         fSpoofData.fRawData.fScriptSetsLength = rtScriptSetsCount;
/*  749 */         int rindex = 2;
/*  750 */         for (int i = 2; i < scriptSets.size(); i++) {
/*  751 */           BuilderScriptSet bSet = (BuilderScriptSet)scriptSets.elementAt(i);
/*  752 */           if (bSet.rindex >= rindex)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  757 */             assert (rindex == bSet.rindex);
/*  758 */             bSet.sset.output(os);
/*  759 */             rindex++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       private static class BuilderScriptSet
/*      */       {
/*      */         int codePoint;
/*      */         
/*      */ 
/*      */         Trie2Writable trie;
/*      */         
/*      */ 
/*      */         SpoofChecker.ScriptSet sset;
/*      */         
/*      */ 
/*      */         int index;
/*      */         
/*      */ 
/*      */         int rindex;
/*      */         
/*      */ 
/*      */         BuilderScriptSet()
/*      */         {
/*  785 */           this.codePoint = -1;
/*  786 */           this.trie = null;
/*  787 */           this.sset = null;
/*  788 */           this.index = 0;
/*  789 */           this.rindex = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static class ConfusabledataBuilder
/*      */     {
/*      */       private SpoofChecker.SpoofData fSpoofData;
/*      */       
/*      */ 
/*      */ 
/*      */       private ByteArrayOutputStream bos;
/*      */       
/*      */ 
/*      */       private DataOutputStream os;
/*      */       
/*      */ 
/*      */       private Hashtable<Integer, SPUString> fSLTable;
/*      */       
/*      */ 
/*      */       private Hashtable<Integer, SPUString> fSATable;
/*      */       
/*      */ 
/*      */       private Hashtable<Integer, SPUString> fMLTable;
/*      */       
/*      */ 
/*      */       private Hashtable<Integer, SPUString> fMATable;
/*      */       
/*      */ 
/*      */       private UnicodeSet fKeySet;
/*      */       
/*      */ 
/*      */       private StringBuffer fStringTable;
/*      */       
/*      */ 
/*      */       private Vector<Integer> fKeyVec;
/*      */       
/*      */ 
/*      */       private Vector<Integer> fValueVec;
/*      */       
/*      */ 
/*      */       private Vector<Integer> fStringLengthsTable;
/*      */       
/*      */ 
/*      */       private SPUStringPool stringPool;
/*      */       
/*      */ 
/*      */       private Pattern fParseLine;
/*      */       
/*      */ 
/*      */       private Pattern fParseHexNum;
/*      */       
/*      */ 
/*      */       private int fLineNum;
/*      */       
/*      */ 
/*      */ 
/*      */       ConfusabledataBuilder(SpoofChecker.SpoofData spData, ByteArrayOutputStream bos)
/*      */       {
/*  851 */         this.bos = bos;
/*  852 */         this.os = new DataOutputStream(bos);
/*  853 */         this.fSpoofData = spData;
/*  854 */         this.fSLTable = new Hashtable();
/*  855 */         this.fSATable = new Hashtable();
/*  856 */         this.fMLTable = new Hashtable();
/*  857 */         this.fMATable = new Hashtable();
/*  858 */         this.fKeySet = new UnicodeSet();
/*  859 */         this.fKeyVec = new Vector();
/*  860 */         this.fValueVec = new Vector();
/*  861 */         this.stringPool = new SPUStringPool();
/*      */       }
/*      */       
/*      */       void build(Reader confusables) throws ParseException, IOException {
/*  865 */         StringBuffer fInput = new StringBuffer();
/*  866 */         SpoofChecker.Builder.WSConfusableDataBuilder.readWholeFileToString(confusables, fInput);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  877 */         this.fParseLine = Pattern.compile("(?m)^[ \\t]*([0-9A-Fa-f]+)[ \\t]+;[ \\t]*([0-9A-Fa-f]+(?:[ \\t]+[0-9A-Fa-f]+)*)[ \\t]*;\\s*(?:(SL)|(SA)|(ML)|(MA))[ \\t]*(?:#.*?)?$|^([ \\t]*(?:#.*?)?)$|^(.*?)$");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  887 */         this.fParseHexNum = Pattern.compile("\\s*([0-9A-F]+)");
/*      */         
/*      */ 
/*      */ 
/*  891 */         if (fInput.charAt(0) == 65279) {
/*  892 */           fInput.setCharAt(0, ' ');
/*      */         }
/*      */         
/*      */ 
/*  896 */         Matcher matcher = this.fParseLine.matcher(fInput);
/*  897 */         while (matcher.find()) {
/*  898 */           this.fLineNum += 1;
/*  899 */           if (matcher.start(7) < 0)
/*      */           {
/*      */ 
/*      */ 
/*  903 */             if (matcher.start(8) >= 0)
/*      */             {
/*      */ 
/*  906 */               throw new ParseException("Confusables, line " + this.fLineNum + ": Unrecognized Line: " + matcher.group(8), matcher.start(8));
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  913 */             int keyChar = Integer.parseInt(matcher.group(1), 16);
/*  914 */             if (keyChar > 1114111) {
/*  915 */               throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + matcher.group(1), matcher.start(1));
/*      */             }
/*      */             
/*  918 */             Matcher m = this.fParseHexNum.matcher(matcher.group(2));
/*      */             
/*  920 */             StringBuilder mapString = new StringBuilder();
/*  921 */             while (m.find()) {
/*  922 */               int c = Integer.parseInt(m.group(1), 16);
/*  923 */               if (keyChar > 1114111) {
/*  924 */                 throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + Integer.toString(c, 16), matcher.start(2));
/*      */               }
/*      */               
/*  927 */               mapString.appendCodePoint(c);
/*      */             }
/*  929 */             assert (mapString.length() >= 1);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  934 */             SPUString smapString = this.stringPool.addString(mapString.toString());
/*      */             
/*      */ 
/*  937 */             Hashtable<Integer, SPUString> table = matcher.start(6) >= 0 ? this.fMATable : matcher.start(5) >= 0 ? this.fMLTable : matcher.start(4) >= 0 ? this.fSATable : matcher.start(3) >= 0 ? this.fSLTable : null;
/*      */             
/*      */ 
/*  940 */             assert (table != null);
/*  941 */             table.put(Integer.valueOf(keyChar), smapString);
/*  942 */             this.fKeySet.add(keyChar);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  958 */         this.stringPool.sort();
/*  959 */         this.fStringTable = new StringBuffer();
/*  960 */         this.fStringLengthsTable = new Vector();
/*  961 */         int previousStringLength = 0;
/*  962 */         int previousStringIndex = 0;
/*  963 */         int poolSize = this.stringPool.size();
/*      */         
/*  965 */         for (int i = 0; i < poolSize; i++) {
/*  966 */           SPUString s = this.stringPool.getByIndex(i);
/*  967 */           int strLen = s.fStr.length();
/*  968 */           int strIndex = this.fStringTable.length();
/*  969 */           assert (strLen >= previousStringLength);
/*  970 */           if (strLen == 1)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  977 */             s.fStrTableIndex = s.fStr.charAt(0);
/*      */           } else {
/*  979 */             if ((strLen > previousStringLength) && (previousStringLength >= 4)) {
/*  980 */               this.fStringLengthsTable.addElement(Integer.valueOf(previousStringIndex));
/*  981 */               this.fStringLengthsTable.addElement(Integer.valueOf(previousStringLength));
/*      */             }
/*  983 */             s.fStrTableIndex = strIndex;
/*  984 */             this.fStringTable.append(s.fStr);
/*      */           }
/*  986 */           previousStringLength = strLen;
/*  987 */           previousStringIndex = strIndex;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  994 */         if (previousStringLength >= 4) {
/*  995 */           this.fStringLengthsTable.addElement(Integer.valueOf(previousStringIndex));
/*  996 */           this.fStringLengthsTable.addElement(Integer.valueOf(previousStringLength));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1011 */         for (int range = 0; range < this.fKeySet.getRangeCount(); range++)
/*      */         {
/*      */ 
/*      */ 
/* 1015 */           for (int keyChar = this.fKeySet.getRangeStart(range); keyChar <= this.fKeySet.getRangeEnd(range); keyChar++) {
/* 1016 */             addKeyEntry(keyChar, this.fSLTable, 16777216);
/* 1017 */             addKeyEntry(keyChar, this.fSATable, 33554432);
/* 1018 */             addKeyEntry(keyChar, this.fMLTable, 67108864);
/* 1019 */             addKeyEntry(keyChar, this.fMATable, 134217728);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1024 */         outputData();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       void addKeyEntry(int keyChar, Hashtable<Integer, SPUString> table, int tableFlag)
/*      */       {
/* 1042 */         SPUString targetMapping = (SPUString)table.get(Integer.valueOf(keyChar));
/* 1043 */         if (targetMapping == null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1048 */           return;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1055 */         boolean keyHasMultipleValues = false;
/*      */         
/* 1057 */         for (int i = this.fKeyVec.size() - 1; i >= 0; i--) {
/* 1058 */           int key = ((Integer)this.fKeyVec.elementAt(i)).intValue();
/* 1059 */           if ((key & 0xFFFFFF) != keyChar) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1065 */           String mapping = getMapping(i);
/* 1066 */           if (mapping.equals(targetMapping.fStr))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1071 */             key |= tableFlag;
/* 1072 */             this.fKeyVec.setElementAt(Integer.valueOf(key), i);
/* 1073 */             return;
/*      */           }
/* 1075 */           keyHasMultipleValues = true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1082 */         int newKey = keyChar | tableFlag;
/* 1083 */         if (keyHasMultipleValues) {
/* 1084 */           newKey |= 0x10000000;
/*      */         }
/* 1086 */         int adjustedMappingLength = targetMapping.fStr.length() - 1;
/* 1087 */         if (adjustedMappingLength > 3) {
/* 1088 */           adjustedMappingLength = 3;
/*      */         }
/* 1090 */         newKey |= adjustedMappingLength << 29;
/*      */         
/* 1092 */         int newData = targetMapping.fStrTableIndex;
/*      */         
/* 1094 */         this.fKeyVec.addElement(Integer.valueOf(newKey));
/* 1095 */         this.fValueVec.addElement(Integer.valueOf(newData));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1100 */         if (keyHasMultipleValues) {
/* 1101 */           int previousKeyIndex = this.fKeyVec.size() - 2;
/* 1102 */           int previousKey = ((Integer)this.fKeyVec.elementAt(previousKeyIndex)).intValue();
/* 1103 */           previousKey |= 0x10000000;
/* 1104 */           this.fKeyVec.setElementAt(Integer.valueOf(previousKey), previousKeyIndex);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       String getMapping(int index)
/*      */       {
/* 1111 */         int key = ((Integer)this.fKeyVec.elementAt(index)).intValue();
/* 1112 */         int value = ((Integer)this.fValueVec.elementAt(index)).intValue();
/* 1113 */         int length = SpoofChecker.getKeyLength(key);
/*      */         
/* 1115 */         switch (length) {
/*      */         case 0: 
/* 1117 */           char[] cs = { (char)value };
/* 1118 */           return new String(cs);
/*      */         case 1: 
/*      */         case 2: 
/* 1121 */           return this.fStringTable.substring(value, value + length + 1);
/*      */         case 3: 
/* 1123 */           length = 0;
/*      */           
/* 1125 */           for (int i = 0; i < this.fStringLengthsTable.size(); i += 2) {
/* 1126 */             int lastIndexWithLen = ((Integer)this.fStringLengthsTable.elementAt(i)).intValue();
/* 1127 */             if (value <= lastIndexWithLen) {
/* 1128 */               length = ((Integer)this.fStringLengthsTable.elementAt(i + 1)).intValue();
/* 1129 */               break;
/*      */             }
/*      */           }
/* 1132 */           assert (length >= 3);
/* 1133 */           return this.fStringTable.substring(value, value + length);
/*      */         }
/* 1135 */         if (!$assertionsDisabled) { throw new AssertionError();
/*      */         }
/* 1137 */         return new String();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       void outputData()
/*      */         throws IOException
/*      */       {
/* 1146 */         SpoofChecker.SpoofDataHeader rawData = this.fSpoofData.fRawData;
/*      */         
/*      */ 
/*      */ 
/* 1150 */         int numKeys = this.fKeyVec.size();
/*      */         
/* 1152 */         int previousKey = 0;
/* 1153 */         rawData.output(this.os);
/* 1154 */         rawData.fCFUKeys = this.os.size();
/* 1155 */         assert (rawData.fCFUKeys == 128);
/* 1156 */         rawData.fCFUKeysSize = numKeys;
/* 1157 */         for (int i = 0; i < numKeys; i++) {
/* 1158 */           int key = ((Integer)this.fKeyVec.elementAt(i)).intValue();
/* 1159 */           assert ((key & 0xFFFFFF) >= (previousKey & 0xFFFFFF));
/* 1160 */           assert ((key & 0xFF000000) != 0);
/* 1161 */           this.os.writeInt(key);
/* 1162 */           previousKey = key;
/*      */         }
/*      */         
/*      */ 
/* 1166 */         int numValues = this.fValueVec.size();
/* 1167 */         assert (numKeys == numValues);
/* 1168 */         rawData.fCFUStringIndex = this.os.size();
/* 1169 */         rawData.fCFUStringIndexSize = numValues;
/* 1170 */         for (i = 0; i < numValues; i++) {
/* 1171 */           int value = ((Integer)this.fValueVec.elementAt(i)).intValue();
/* 1172 */           assert (value < 65535);
/* 1173 */           this.os.writeShort((short)value);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1178 */         int stringsLength = this.fStringTable.length();
/*      */         
/*      */ 
/* 1181 */         String strings = this.fStringTable.toString();
/* 1182 */         rawData.fCFUStringTable = this.os.size();
/* 1183 */         rawData.fCFUStringTableLen = stringsLength;
/* 1184 */         for (i = 0; i < stringsLength; i++) {
/* 1185 */           this.os.writeChar(strings.charAt(i));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1194 */         int lengthTableLength = this.fStringLengthsTable.size();
/* 1195 */         int previousLength = 0;
/*      */         
/*      */ 
/*      */ 
/* 1199 */         rawData.fCFUStringLengthsSize = (lengthTableLength / 2);
/* 1200 */         rawData.fCFUStringLengths = this.os.size();
/* 1201 */         for (i = 0; i < lengthTableLength; i += 2) {
/* 1202 */           int offset = ((Integer)this.fStringLengthsTable.elementAt(i)).intValue();
/* 1203 */           int length = ((Integer)this.fStringLengthsTable.elementAt(i + 1)).intValue();
/* 1204 */           assert (offset < stringsLength);
/* 1205 */           assert (length < 40);
/* 1206 */           assert (length > previousLength);
/* 1207 */           this.os.writeShort((short)offset);
/* 1208 */           this.os.writeShort((short)length);
/* 1209 */           previousLength = length;
/*      */         }
/*      */         
/* 1212 */         this.os.flush();
/* 1213 */         DataInputStream is = new DataInputStream(new ByteArrayInputStream(this.bos.toByteArray()));
/* 1214 */         is.mark(Integer.MAX_VALUE);
/* 1215 */         this.fSpoofData.initPtrs(is);
/*      */       }
/*      */       
/*      */       public static void buildConfusableData(SpoofChecker.SpoofData spData, Reader confusables) throws IOException, ParseException
/*      */       {
/* 1220 */         ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 1221 */         ConfusabledataBuilder builder = new ConfusabledataBuilder(spData, bos);
/* 1222 */         builder.build(confusables);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private static class SPUString
/*      */       {
/*      */         String fStr;
/*      */         
/*      */ 
/*      */ 
/*      */         int fStrTableIndex;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         SPUString(String s)
/*      */         {
/* 1242 */           this.fStr = s;
/* 1243 */           this.fStrTableIndex = 0;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       private static class SPUStringComparator
/*      */         implements Comparator<SpoofChecker.Builder.ConfusabledataBuilder.SPUString>
/*      */       {
/*      */         public int compare(SpoofChecker.Builder.ConfusabledataBuilder.SPUString sL, SpoofChecker.Builder.ConfusabledataBuilder.SPUString sR)
/*      */         {
/* 1253 */           int lenL = sL.fStr.length();
/* 1254 */           int lenR = sR.fStr.length();
/* 1255 */           if (lenL < lenR)
/* 1256 */             return -1;
/* 1257 */           if (lenL > lenR) {
/* 1258 */             return 1;
/*      */           }
/* 1260 */           return sL.fStr.compareTo(sR.fStr);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       private static class SPUStringPool
/*      */       {
/*      */         private Vector<SpoofChecker.Builder.ConfusabledataBuilder.SPUString> fVec;
/*      */         
/*      */         private Hashtable<String, SpoofChecker.Builder.ConfusabledataBuilder.SPUString> fHash;
/*      */         
/*      */         public SPUStringPool()
/*      */         {
/* 1273 */           this.fVec = new Vector();
/* 1274 */           this.fHash = new Hashtable();
/*      */         }
/*      */         
/*      */         public int size() {
/* 1278 */           return this.fVec.size();
/*      */         }
/*      */         
/*      */         public SpoofChecker.Builder.ConfusabledataBuilder.SPUString getByIndex(int index)
/*      */         {
/* 1283 */           SpoofChecker.Builder.ConfusabledataBuilder.SPUString retString = (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)this.fVec.elementAt(index);
/* 1284 */           return retString;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         public SpoofChecker.Builder.ConfusabledataBuilder.SPUString addString(String src)
/*      */         {
/* 1291 */           SpoofChecker.Builder.ConfusabledataBuilder.SPUString hashedString = (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)this.fHash.get(src);
/* 1292 */           if (hashedString == null) {
/* 1293 */             hashedString = new SpoofChecker.Builder.ConfusabledataBuilder.SPUString(src);
/* 1294 */             this.fHash.put(src, hashedString);
/* 1295 */             this.fVec.addElement(hashedString);
/*      */           }
/* 1297 */           return hashedString;
/*      */         }
/*      */         
/*      */         public void sort()
/*      */         {
/* 1302 */           Collections.sort(this.fVec, new SpoofChecker.Builder.ConfusabledataBuilder.SPUStringComparator(null));
/*      */         }
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
/*      */   public int getChecks()
/*      */   {
/* 1320 */     return this.fChecks;
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
/*      */   public Set<ULocale> getAllowedLocales()
/*      */   {
/* 1338 */     return this.fAllowedLocales;
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
/*      */   public UnicodeSet getAllowedChars()
/*      */   {
/* 1353 */     return this.fAllowedCharsSet;
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
/*      */   public static class CheckResult
/*      */   {
/*      */     public int checks;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int position;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public CheckResult()
/*      */     {
/* 1388 */       this.checks = 0;
/* 1389 */       this.position = 0;
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
/*      */   public boolean failsChecks(String text, CheckResult checkResult)
/*      */   {
/* 1407 */     int length = text.length();
/*      */     
/* 1409 */     int result = 0;
/* 1410 */     int failPos = Integer.MAX_VALUE;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1417 */     int scriptCount = -1;
/*      */     
/* 1419 */     if (0 != (this.fChecks & 0x10)) {
/* 1420 */       scriptCount = scriptScan(text, checkResult);
/*      */       
/*      */ 
/* 1423 */       if (scriptCount >= 2)
/*      */       {
/*      */ 
/* 1426 */         result |= 0x10;
/*      */       }
/*      */     }
/*      */     int i;
/* 1430 */     if (0 != (this.fChecks & 0x40))
/*      */     {
/*      */ 
/* 1433 */       for (i = 0; i < length;)
/*      */       {
/* 1435 */         int c = Character.codePointAt(text, i);
/* 1436 */         i = Character.offsetByCodePoints(text, i, 1);
/* 1437 */         if (!this.fAllowedCharsSet.contains(c)) {
/* 1438 */           result |= 0x40;
/* 1439 */           if (i < failPos) {
/* 1440 */             failPos = i;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1447 */     if (0 != (this.fChecks & 0x26))
/*      */     {
/* 1449 */       String nfdText = Normalizer.normalize(text, Normalizer.NFD, 0);
/*      */       int firstNonspacingMark;
/* 1451 */       boolean haveMultipleMarks; UnicodeSet marksSeenSoFar; int i; if (0 != (this.fChecks & 0x20))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1457 */         firstNonspacingMark = 0;
/* 1458 */         haveMultipleMarks = false;
/* 1459 */         marksSeenSoFar = new UnicodeSet();
/*      */         
/* 1461 */         for (i = 0; i < length;)
/*      */         {
/* 1463 */           int c = Character.codePointAt(nfdText, i);
/* 1464 */           i = Character.offsetByCodePoints(nfdText, i, 1);
/* 1465 */           if (Character.getType(c) != 6) {
/* 1466 */             firstNonspacingMark = 0;
/* 1467 */             if (haveMultipleMarks) {
/* 1468 */               marksSeenSoFar.clear();
/* 1469 */               haveMultipleMarks = false;
/*      */             }
/*      */             
/*      */           }
/* 1473 */           else if (firstNonspacingMark == 0) {
/* 1474 */             firstNonspacingMark = c;
/*      */           }
/*      */           else {
/* 1477 */             if (!haveMultipleMarks) {
/* 1478 */               marksSeenSoFar.add(firstNonspacingMark);
/* 1479 */               haveMultipleMarks = true;
/*      */             }
/* 1481 */             if (marksSeenSoFar.contains(c))
/*      */             {
/*      */ 
/* 1484 */               result |= 0x20;
/* 1485 */               failPos = i;
/* 1486 */               break;
/*      */             }
/* 1488 */             marksSeenSoFar.add(c);
/*      */           }
/*      */         }
/*      */       }
/* 1492 */       if (0 != (this.fChecks & 0x6))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1514 */         if (scriptCount == -1) {
/* 1515 */           scriptCount = scriptScan(text, null);
/*      */         }
/*      */         
/* 1518 */         ScriptSet scripts = new ScriptSet();
/* 1519 */         wholeScriptCheck(nfdText, scripts);
/* 1520 */         int confusableScriptCount = scripts.countMembers();
/*      */         
/*      */ 
/*      */ 
/* 1524 */         if ((0 != (this.fChecks & 0x4)) && (confusableScriptCount >= 2) && (scriptCount == 1)) {
/* 1525 */           result |= 0x4;
/*      */         }
/*      */         
/* 1528 */         if ((0 != (this.fChecks & 0x2)) && (confusableScriptCount >= 1) && (scriptCount > 1)) {
/* 1529 */           result |= 0x2;
/*      */         }
/*      */       }
/*      */     }
/* 1533 */     if (checkResult != null) {
/* 1534 */       checkResult.checks = result;
/* 1535 */       if (failPos != Integer.MAX_VALUE) {
/* 1536 */         checkResult.position = failPos;
/*      */       }
/*      */     }
/* 1539 */     return 0 != result;
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
/*      */   public boolean failsChecks(String text)
/*      */   {
/* 1553 */     return failsChecks(text, null);
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
/*      */   public int areConfusable(String s1, String s2)
/*      */   {
/* 1588 */     if ((this.fChecks & 0x7) == 0) {
/* 1589 */       throw new IllegalArgumentException("No confusable checks are enabled.");
/*      */     }
/* 1591 */     int flagsForSkeleton = this.fChecks & 0x8;
/*      */     
/*      */ 
/*      */ 
/* 1595 */     int result = 0;
/* 1596 */     int s1ScriptCount = scriptScan(s1, null);
/* 1597 */     int s2ScriptCount = scriptScan(s2, null);
/*      */     
/* 1599 */     if (0 != (this.fChecks & 0x1))
/*      */     {
/* 1601 */       if ((s1ScriptCount <= 1) && (s2ScriptCount <= 1)) {
/* 1602 */         flagsForSkeleton |= 0x1;
/* 1603 */         String s1Skeleton = getSkeleton(flagsForSkeleton, s1);
/* 1604 */         String s2Skeleton = getSkeleton(flagsForSkeleton, s2);
/* 1605 */         if ((s1Skeleton.length() == s2Skeleton.length()) && (s1Skeleton.equals(s2Skeleton))) {
/* 1606 */           result |= 0x1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1611 */     if (0 != (result & 0x1))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1617 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1623 */     boolean possiblyWholeScriptConfusables = (s1ScriptCount <= 1) && (s2ScriptCount <= 1) && (0 != (this.fChecks & 0x4));
/*      */     
/*      */ 
/*      */ 
/* 1627 */     if ((0 != (this.fChecks & 0x2)) || (possiblyWholeScriptConfusables))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1633 */       flagsForSkeleton &= 0xFFFFFFFE;
/* 1634 */       String s1Skeleton = getSkeleton(flagsForSkeleton, s1);
/* 1635 */       String s2Skeleton = getSkeleton(flagsForSkeleton, s2);
/* 1636 */       if ((s1Skeleton.length() == s2Skeleton.length()) && (s1Skeleton.equals(s2Skeleton))) {
/* 1637 */         result |= 0x2;
/* 1638 */         if (possiblyWholeScriptConfusables) {
/* 1639 */           result |= 0x4;
/*      */         }
/*      */       }
/*      */     }
/* 1643 */     return result;
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
/*      */   public String getSkeleton(int type, String s)
/*      */   {
/* 1668 */     if ((type & 0xFFFFFFF6) != 0)
/*      */     {
/* 1670 */       return null;
/*      */     }
/*      */     
/* 1673 */     int tableMask = 0;
/* 1674 */     switch (type) {
/*      */     case 0: 
/* 1676 */       tableMask = 67108864;
/* 1677 */       break;
/*      */     case 1: 
/* 1679 */       tableMask = 16777216;
/* 1680 */       break;
/*      */     case 8: 
/* 1682 */       tableMask = 134217728;
/* 1683 */       break;
/*      */     case 9: 
/* 1685 */       tableMask = 33554432;
/* 1686 */       break;
/*      */     case 2: case 3: case 4: case 5: 
/*      */     case 6: case 7: default: 
/* 1689 */       return null;
/*      */     }
/*      */     
/*      */     
/* 1693 */     String nfdInput = Normalizer.normalize(s, Normalizer.NFD, 0);
/* 1694 */     int normalizedLen = nfdInput.length();
/*      */     
/*      */ 
/*      */ 
/* 1698 */     int inputIndex = 0;
/* 1699 */     StringBuilder skelStr = new StringBuilder();
/* 1700 */     while (inputIndex < normalizedLen)
/*      */     {
/* 1702 */       int c = Character.codePointAt(nfdInput, inputIndex);
/* 1703 */       inputIndex = Character.offsetByCodePoints(nfdInput, inputIndex, 1);
/* 1704 */       confusableLookup(c, tableMask, skelStr);
/*      */     }
/*      */     
/* 1707 */     String result = skelStr.toString();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1712 */     if (!Normalizer.isNormalized(result, Normalizer.NFD, 0)) {
/* 1713 */       String normedResult = Normalizer.normalize(result, Normalizer.NFD, 0);
/* 1714 */       result = normedResult;
/*      */     }
/* 1716 */     return result;
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
/*      */   private void confusableLookup(int inChar, int tableMask, StringBuilder dest)
/*      */   {
/* 1729 */     int low = 0;
/* 1730 */     int mid = 0;
/* 1731 */     int limit = this.fSpoofData.fRawData.fCFUKeysSize;
/*      */     
/* 1733 */     boolean foundChar = false;
/*      */     do
/*      */     {
/* 1736 */       int delta = (limit - low) / 2;
/* 1737 */       mid = low + delta;
/* 1738 */       int midc = this.fSpoofData.fCFUKeys[mid] & 0x1FFFFF;
/* 1739 */       if (inChar == midc) {
/* 1740 */         foundChar = true;
/* 1741 */         break; }
/* 1742 */       if (inChar < midc) {
/* 1743 */         limit = mid;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1748 */         low = mid + 1;
/*      */       }
/* 1750 */     } while (low < limit);
/* 1751 */     if (!foundChar) {
/* 1752 */       dest.appendCodePoint(inChar);
/* 1753 */       return;
/*      */     }
/*      */     
/* 1756 */     boolean foundKey = false;
/* 1757 */     int keyFlags = this.fSpoofData.fCFUKeys[mid] & 0xFF000000;
/* 1758 */     if ((keyFlags & tableMask) == 0)
/*      */     {
/*      */ 
/* 1761 */       if (0 != (keyFlags & 0x10000000))
/*      */       {
/* 1763 */         for (int altMid = mid - 1; (this.fSpoofData.fCFUKeys[altMid] & 0xFFFFFF) == inChar; altMid--) {
/* 1764 */           keyFlags = this.fSpoofData.fCFUKeys[altMid] & 0xFF000000;
/* 1765 */           if (0 != (keyFlags & tableMask)) {
/* 1766 */             mid = altMid;
/* 1767 */             foundKey = true;
/* 1768 */             break;
/*      */           }
/*      */         }
/* 1771 */         if (!foundKey) {
/* 1772 */           for (altMid = mid + 1; (this.fSpoofData.fCFUKeys[altMid] & 0xFFFFFF) == inChar; altMid++) {
/* 1773 */             keyFlags = this.fSpoofData.fCFUKeys[altMid] & 0xFF000000;
/* 1774 */             if (0 != (keyFlags & tableMask)) {
/* 1775 */               mid = altMid;
/* 1776 */               foundKey = true;
/* 1777 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1782 */       if (!foundKey)
/*      */       {
/*      */ 
/* 1785 */         dest.appendCodePoint(inChar);
/* 1786 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1790 */     int stringLen = getKeyLength(keyFlags) + 1;
/* 1791 */     int keyTableIndex = mid;
/*      */     
/*      */ 
/*      */ 
/* 1795 */     short value = this.fSpoofData.fCFUValues[keyTableIndex];
/* 1796 */     if (stringLen == 1) {
/* 1797 */       dest.append((char)value);
/* 1798 */       return;
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
/* 1811 */     if (stringLen == 4) {
/* 1812 */       int stringLengthsLimit = this.fSpoofData.fRawData.fCFUStringLengthsSize;
/* 1813 */       for (int ix = 0; ix < stringLengthsLimit; ix++) {
/* 1814 */         if (this.fSpoofData.fCFUStringLengths[ix].fLastString >= value) {
/* 1815 */           stringLen = this.fSpoofData.fCFUStringLengths[ix].fStrLength;
/* 1816 */           break;
/*      */         }
/*      */       }
/* 1819 */       assert (ix < stringLengthsLimit);
/*      */     }
/*      */     
/* 1822 */     assert (value + stringLen <= this.fSpoofData.fRawData.fCFUStringTableLen);
/* 1823 */     dest.append(this.fSpoofData.fCFUStrings, value, stringLen);
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
/*      */   void wholeScriptCheck(CharSequence text, ScriptSet result)
/*      */   {
/* 1837 */     int inputIdx = 0;
/*      */     
/*      */ 
/* 1840 */     Trie2 table = 0 != (this.fChecks & 0x8) ? this.fSpoofData.fAnyCaseTrie : this.fSpoofData.fLowerCaseTrie;
/* 1841 */     result.setAll();
/* 1842 */     while (inputIdx < text.length()) {
/* 1843 */       int c = Character.codePointAt(text, inputIdx);
/* 1844 */       inputIdx = Character.offsetByCodePoints(text, inputIdx, 1);
/* 1845 */       int index = table.get(c);
/* 1846 */       if (index == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1851 */         int cpScript = UScript.getScript(c);
/* 1852 */         assert (cpScript > 1);
/* 1853 */         result.intersect(cpScript);
/* 1854 */       } else if (index != 1)
/*      */       {
/*      */ 
/* 1857 */         result.intersect(this.fSpoofData.fScriptSets[index]);
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
/*      */   int scriptScan(CharSequence text, CheckResult checkResult)
/*      */   {
/* 1875 */     int inputIdx = 0;
/*      */     
/* 1877 */     int scriptCount = 0;
/* 1878 */     int lastScript = -1;
/* 1879 */     int sc = -1;
/* 1880 */     while ((inputIdx < text.length()) && (scriptCount < 2)) {
/* 1881 */       int c = Character.codePointAt(text, inputIdx);
/* 1882 */       inputIdx = Character.offsetByCodePoints(text, inputIdx, 1);
/* 1883 */       sc = UScript.getScript(c);
/* 1884 */       if ((sc != 0) && (sc != 1) && (sc != 103))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1892 */         if ((sc == 22) || (sc == 20) || (sc == 18)) {
/* 1893 */           sc = 17;
/*      */         }
/* 1895 */         if (sc != lastScript) {
/* 1896 */           scriptCount++;
/* 1897 */           lastScript = sc;
/*      */         }
/*      */       } }
/* 1900 */     if ((scriptCount == 2) && (checkResult != null)) {
/* 1901 */       checkResult.position = inputIdx;
/*      */     }
/* 1903 */     return scriptCount;
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
/*      */   static final int getKeyLength(int x)
/*      */   {
/* 1973 */     return x >> 29 & 0x3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class SpoofDataHeader
/*      */   {
/*      */     int fMagic;
/*      */     
/*      */ 
/* 1984 */     byte[] fFormatVersion = new byte[4];
/*      */     
/*      */ 
/*      */     int fLength;
/*      */     
/*      */ 
/*      */     int fCFUKeys;
/*      */     
/*      */ 
/*      */     int fCFUKeysSize;
/*      */     
/*      */     int fCFUStringIndex;
/*      */     
/*      */     int fCFUStringIndexSize;
/*      */     
/*      */     int fCFUStringTable;
/*      */     
/*      */     int fCFUStringTableLen;
/*      */     
/*      */     int fCFUStringLengths;
/*      */     
/*      */     int fCFUStringLengthsSize;
/*      */     
/*      */     int fAnyCaseTrie;
/*      */     
/*      */     int fAnyCaseTrieLength;
/*      */     
/*      */     int fLowerCaseTrie;
/*      */     
/*      */     int fLowerCaseTrieLength;
/*      */     
/*      */     int fScriptSets;
/*      */     
/*      */     int fScriptSetsLength;
/*      */     
/* 2019 */     int[] unused = new int[15];
/*      */     
/*      */     public SpoofDataHeader() {}
/*      */     
/*      */     public SpoofDataHeader(DataInputStream dis)
/*      */       throws IOException
/*      */     {
/* 2026 */       this.fMagic = dis.readInt();
/* 2027 */       for (int i = 0; i < this.fFormatVersion.length; i++) {
/* 2028 */         this.fFormatVersion[i] = dis.readByte();
/*      */       }
/* 2030 */       this.fLength = dis.readInt();
/* 2031 */       this.fCFUKeys = dis.readInt();
/* 2032 */       this.fCFUKeysSize = dis.readInt();
/* 2033 */       this.fCFUStringIndex = dis.readInt();
/* 2034 */       this.fCFUStringIndexSize = dis.readInt();
/* 2035 */       this.fCFUStringTable = dis.readInt();
/* 2036 */       this.fCFUStringTableLen = dis.readInt();
/* 2037 */       this.fCFUStringLengths = dis.readInt();
/* 2038 */       this.fCFUStringLengthsSize = dis.readInt();
/* 2039 */       this.fAnyCaseTrie = dis.readInt();
/* 2040 */       this.fAnyCaseTrieLength = dis.readInt();
/* 2041 */       this.fLowerCaseTrie = dis.readInt();
/* 2042 */       this.fLowerCaseTrieLength = dis.readInt();
/* 2043 */       this.fScriptSets = dis.readInt();
/* 2044 */       this.fScriptSetsLength = dis.readInt();
/* 2045 */       for (i = 0; i < this.unused.length; i++) {
/* 2046 */         this.unused[i] = dis.readInt();
/*      */       }
/*      */     }
/*      */     
/*      */     public void output(DataOutputStream os) throws IOException
/*      */     {
/* 2052 */       os.writeInt(this.fMagic);
/* 2053 */       for (int i = 0; i < this.fFormatVersion.length; i++) {
/* 2054 */         os.writeByte(this.fFormatVersion[i]);
/*      */       }
/* 2056 */       os.writeInt(this.fLength);
/* 2057 */       os.writeInt(this.fCFUKeys);
/* 2058 */       os.writeInt(this.fCFUKeysSize);
/* 2059 */       os.writeInt(this.fCFUStringIndex);
/* 2060 */       os.writeInt(this.fCFUStringIndexSize);
/* 2061 */       os.writeInt(this.fCFUStringTable);
/* 2062 */       os.writeInt(this.fCFUStringTableLen);
/* 2063 */       os.writeInt(this.fCFUStringLengths);
/* 2064 */       os.writeInt(this.fCFUStringLengthsSize);
/* 2065 */       os.writeInt(this.fAnyCaseTrie);
/* 2066 */       os.writeInt(this.fAnyCaseTrieLength);
/* 2067 */       os.writeInt(this.fLowerCaseTrie);
/* 2068 */       os.writeInt(this.fLowerCaseTrieLength);
/* 2069 */       os.writeInt(this.fScriptSets);
/* 2070 */       os.writeInt(this.fScriptSetsLength);
/* 2071 */       for (i = 0; i < this.unused.length; i++) {
/* 2072 */         os.writeInt(this.unused[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SpoofData
/*      */   {
/*      */     SpoofChecker.SpoofDataHeader fRawData;
/*      */     int[] fCFUKeys;
/*      */     short[] fCFUValues;
/*      */     SpoofStringLengthsElement[] fCFUStringLengths;
/*      */     char[] fCFUStrings;
/*      */     Trie2 fAnyCaseTrie;
/*      */     Trie2 fLowerCaseTrie;
/*      */     SpoofChecker.ScriptSet[] fScriptSets;
/*      */     
/*      */     public static SpoofData getDefault() throws IOException
/*      */     {
/* 2090 */       InputStream is = ICUData.getRequiredStream("data/icudt48b/confusables.cfu");
/*      */       
/* 2092 */       SpoofData This = new SpoofData(is);
/* 2093 */       return This;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SpoofData()
/*      */     {
/* 2103 */       this.fRawData = new SpoofChecker.SpoofDataHeader();
/*      */       
/* 2105 */       this.fRawData.fMagic = 944111087;
/* 2106 */       this.fRawData.fFormatVersion[0] = 1;
/* 2107 */       this.fRawData.fFormatVersion[1] = 0;
/* 2108 */       this.fRawData.fFormatVersion[2] = 0;
/* 2109 */       this.fRawData.fFormatVersion[3] = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public SpoofData(InputStream is)
/*      */       throws IOException
/*      */     {
/* 2117 */       DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
/* 2118 */       dis.skip(128L);
/* 2119 */       assert (dis.markSupported());
/* 2120 */       dis.mark(Integer.MAX_VALUE);
/*      */       
/* 2122 */       this.fRawData = new SpoofChecker.SpoofDataHeader(dis);
/* 2123 */       initPtrs(dis);
/*      */     }
/*      */     
/*      */ 
/*      */     static boolean validateDataVersion(SpoofChecker.SpoofDataHeader rawData)
/*      */     {
/* 2129 */       if ((rawData == null) || (rawData.fMagic != 944111087) || (rawData.fFormatVersion[0] > 1) || (rawData.fFormatVersion[1] > 0))
/*      */       {
/* 2131 */         return false;
/*      */       }
/* 2133 */       return true;
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
/*      */     void initPtrs(DataInputStream dis)
/*      */       throws IOException
/*      */     {
/* 2149 */       this.fCFUKeys = null;
/* 2150 */       this.fCFUValues = null;
/* 2151 */       this.fCFUStringLengths = null;
/* 2152 */       this.fCFUStrings = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2157 */       dis.reset();
/* 2158 */       dis.skip(this.fRawData.fCFUKeys);
/* 2159 */       if (this.fRawData.fCFUKeys != 0) {
/* 2160 */         this.fCFUKeys = new int[this.fRawData.fCFUKeysSize];
/* 2161 */         for (int i = 0; i < this.fRawData.fCFUKeysSize; i++) {
/* 2162 */           this.fCFUKeys[i] = dis.readInt();
/*      */         }
/*      */       }
/*      */       
/* 2166 */       dis.reset();
/* 2167 */       dis.skip(this.fRawData.fCFUStringIndex);
/* 2168 */       if (this.fRawData.fCFUStringIndex != 0) {
/* 2169 */         this.fCFUValues = new short[this.fRawData.fCFUStringIndexSize];
/* 2170 */         for (int i = 0; i < this.fRawData.fCFUStringIndexSize; i++) {
/* 2171 */           this.fCFUValues[i] = dis.readShort();
/*      */         }
/*      */       }
/*      */       
/* 2175 */       dis.reset();
/* 2176 */       dis.skip(this.fRawData.fCFUStringTable);
/* 2177 */       if (this.fRawData.fCFUStringTable != 0) {
/* 2178 */         this.fCFUStrings = new char[this.fRawData.fCFUStringTableLen];
/* 2179 */         for (int i = 0; i < this.fRawData.fCFUStringTableLen; i++) {
/* 2180 */           this.fCFUStrings[i] = dis.readChar();
/*      */         }
/*      */       }
/*      */       
/* 2184 */       dis.reset();
/* 2185 */       dis.skip(this.fRawData.fCFUStringLengths);
/* 2186 */       if (this.fRawData.fCFUStringLengths != 0) {
/* 2187 */         this.fCFUStringLengths = new SpoofStringLengthsElement[this.fRawData.fCFUStringLengthsSize];
/* 2188 */         for (int i = 0; i < this.fRawData.fCFUStringLengthsSize; i++) {
/* 2189 */           this.fCFUStringLengths[i] = new SpoofStringLengthsElement(null);
/* 2190 */           this.fCFUStringLengths[i].fLastString = dis.readShort();
/* 2191 */           this.fCFUStringLengths[i].fStrLength = dis.readShort();
/*      */         }
/*      */       }
/*      */       
/* 2195 */       dis.reset();
/* 2196 */       dis.skip(this.fRawData.fAnyCaseTrie);
/* 2197 */       if ((this.fAnyCaseTrie == null) && (this.fRawData.fAnyCaseTrie != 0)) {
/* 2198 */         this.fAnyCaseTrie = Trie2.createFromSerialized(dis);
/*      */       }
/* 2200 */       dis.reset();
/* 2201 */       dis.skip(this.fRawData.fLowerCaseTrie);
/* 2202 */       if ((this.fLowerCaseTrie == null) && (this.fRawData.fLowerCaseTrie != 0)) {
/* 2203 */         this.fLowerCaseTrie = Trie2.createFromSerialized(dis);
/*      */       }
/*      */       
/* 2206 */       dis.reset();
/* 2207 */       dis.skip(this.fRawData.fScriptSets);
/* 2208 */       if (this.fRawData.fScriptSets != 0) {
/* 2209 */         this.fScriptSets = new SpoofChecker.ScriptSet[this.fRawData.fScriptSetsLength];
/* 2210 */         for (int i = 0; i < this.fRawData.fScriptSetsLength; i++) {
/* 2211 */           this.fScriptSets[i] = new SpoofChecker.ScriptSet(dis);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static class SpoofStringLengthsElement
/*      */     {
/*      */       short fLastString;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       short fStrLength;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class ScriptSet
/*      */   {
/*      */     public ScriptSet() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ScriptSet(DataInputStream dis)
/*      */       throws IOException
/*      */     {
/* 2249 */       for (int j = 0; j < this.bits.length; j++) {
/* 2250 */         this.bits[j] = dis.readInt();
/*      */       }
/*      */     }
/*      */     
/*      */     public void output(DataOutputStream os) throws IOException {
/* 2255 */       for (int i = 0; i < this.bits.length; i++) {
/* 2256 */         os.writeInt(this.bits[i]);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean equals(ScriptSet other) {
/* 2261 */       for (int i = 0; i < this.bits.length; i++) {
/* 2262 */         if (this.bits[i] != other.bits[i]) {
/* 2263 */           return false;
/*      */         }
/*      */       }
/* 2266 */       return true;
/*      */     }
/*      */     
/*      */     public void Union(int script) {
/* 2270 */       int index = script / 32;
/* 2271 */       int bit = 1 << (script & 0x1F);
/* 2272 */       assert (index < this.bits.length * 4 * 4);
/* 2273 */       this.bits[index] |= bit;
/*      */     }
/*      */     
/*      */     public void Union(ScriptSet other)
/*      */     {
/* 2278 */       for (int i = 0; i < this.bits.length; i++) {
/* 2279 */         this.bits[i] |= other.bits[i];
/*      */       }
/*      */     }
/*      */     
/*      */     public void intersect(ScriptSet other) {
/* 2284 */       for (int i = 0; i < this.bits.length; i++) {
/* 2285 */         this.bits[i] &= other.bits[i];
/*      */       }
/*      */     }
/*      */     
/*      */     public void intersect(int script) {
/* 2290 */       int index = script / 32;
/* 2291 */       int bit = 1 << (script & 0x1F);
/* 2292 */       assert (index < this.bits.length * 4 * 4);
/*      */       
/* 2294 */       for (int i = 0; i < index; i++) {
/* 2295 */         this.bits[i] = 0;
/*      */       }
/* 2297 */       this.bits[index] &= bit;
/* 2298 */       for (i = index + 1; i < this.bits.length; i++) {
/* 2299 */         this.bits[i] = 0;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setAll() {
/* 2304 */       for (int i = 0; i < this.bits.length; i++) {
/* 2305 */         this.bits[i] = -1;
/*      */       }
/*      */     }
/*      */     
/*      */     public void resetAll()
/*      */     {
/* 2311 */       for (int i = 0; i < this.bits.length; i++) {
/* 2312 */         this.bits[i] = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public int countMembers()
/*      */     {
/* 2319 */       int count = 0;
/* 2320 */       for (int i = 0; i < this.bits.length; i++) {
/* 2321 */         int x = this.bits[i];
/* 2322 */         while (x > 0) {
/* 2323 */           count++;
/* 2324 */           x &= x - 1;
/*      */         }
/*      */       }
/* 2327 */       return count;
/*      */     }
/*      */     
/* 2330 */     private int[] bits = new int[6];
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SpoofChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */