/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataRecord
/*     */ {
/*     */   byte pl;
/*     */   String[][] pluralNames;
/*     */   byte[] genders;
/*     */   String[] singularNames;
/*     */   String[] halfNames;
/*     */   String[] numberNames;
/*     */   String[] mediumNames;
/*     */   String[] shortNames;
/*     */   String[] measures;
/*     */   String[] rqdSuffixes;
/*     */   String[] optSuffixes;
/*     */   String[] halves;
/*     */   byte[] halfPlacements;
/*     */   byte[] halfSupport;
/*     */   String fifteenMinutes;
/*     */   String fiveMinutes;
/*     */   boolean requiresDigitSeparator;
/*     */   String digitPrefix;
/*     */   String countSep;
/*     */   String shortUnitSep;
/*     */   String[] unitSep;
/*     */   boolean[] unitSepRequiresDP;
/*     */   boolean[] requiresSkipMarker;
/*     */   byte numberSystem;
/*     */   char zero;
/*     */   char decimalSep;
/*     */   boolean omitSingularCount;
/*     */   boolean omitDualCount;
/*     */   byte zeroHandling;
/*     */   byte decimalHandling;
/*     */   byte fractionHandling;
/*     */   String skippedUnitMarker;
/*     */   boolean allowZero;
/*     */   boolean weeksAloneOnly;
/*     */   byte useMilliseconds;
/*     */   ScopeData[] scopeData;
/*     */   
/*     */   public static DataRecord read(String ln, RecordReader in)
/*     */   {
/*  57 */     if (in.open("DataRecord")) {
/*  58 */       DataRecord record = new DataRecord();
/*  59 */       record.pl = in.namedIndex("pl", EPluralization.names);
/*  60 */       record.pluralNames = in.stringTable("pluralName");
/*  61 */       record.genders = in.namedIndexArray("gender", EGender.names);
/*  62 */       record.singularNames = in.stringArray("singularName");
/*  63 */       record.halfNames = in.stringArray("halfName");
/*  64 */       record.numberNames = in.stringArray("numberName");
/*  65 */       record.mediumNames = in.stringArray("mediumName");
/*  66 */       record.shortNames = in.stringArray("shortName");
/*  67 */       record.measures = in.stringArray("measure");
/*  68 */       record.rqdSuffixes = in.stringArray("rqdSuffix");
/*  69 */       record.optSuffixes = in.stringArray("optSuffix");
/*  70 */       record.halves = in.stringArray("halves");
/*  71 */       record.halfPlacements = in.namedIndexArray("halfPlacement", EHalfPlacement.names);
/*     */       
/*  73 */       record.halfSupport = in.namedIndexArray("halfSupport", EHalfSupport.names);
/*     */       
/*  75 */       record.fifteenMinutes = in.string("fifteenMinutes");
/*  76 */       record.fiveMinutes = in.string("fiveMinutes");
/*  77 */       record.requiresDigitSeparator = in.bool("requiresDigitSeparator");
/*  78 */       record.digitPrefix = in.string("digitPrefix");
/*  79 */       record.countSep = in.string("countSep");
/*  80 */       record.shortUnitSep = in.string("shortUnitSep");
/*  81 */       record.unitSep = in.stringArray("unitSep");
/*  82 */       record.unitSepRequiresDP = in.boolArray("unitSepRequiresDP");
/*  83 */       record.requiresSkipMarker = in.boolArray("requiresSkipMarker");
/*  84 */       record.numberSystem = in.namedIndex("numberSystem", ENumberSystem.names);
/*     */       
/*  86 */       record.zero = in.character("zero");
/*  87 */       record.decimalSep = in.character("decimalSep");
/*  88 */       record.omitSingularCount = in.bool("omitSingularCount");
/*  89 */       record.omitDualCount = in.bool("omitDualCount");
/*  90 */       record.zeroHandling = in.namedIndex("zeroHandling", EZeroHandling.names);
/*     */       
/*  92 */       record.decimalHandling = in.namedIndex("decimalHandling", EDecimalHandling.names);
/*     */       
/*  94 */       record.fractionHandling = in.namedIndex("fractionHandling", EFractionHandling.names);
/*     */       
/*  96 */       record.skippedUnitMarker = in.string("skippedUnitMarker");
/*  97 */       record.allowZero = in.bool("allowZero");
/*  98 */       record.weeksAloneOnly = in.bool("weeksAloneOnly");
/*  99 */       record.useMilliseconds = in.namedIndex("useMilliseconds", EMilliSupport.names);
/*     */       
/* 101 */       if (in.open("ScopeDataList")) {
/* 102 */         List<ScopeData> list = new ArrayList();
/*     */         ScopeData data;
/* 104 */         while (null != (data = ScopeData.read(in))) {
/* 105 */           list.add(data);
/*     */         }
/* 107 */         if (in.close()) {
/* 108 */           record.scopeData = ((ScopeData[])list.toArray(new ScopeData[list.size()]));
/*     */         }
/*     */       }
/*     */       
/* 112 */       if (in.close()) {
/* 113 */         return record;
/*     */       }
/*     */     } else {
/* 116 */       throw new InternalError("did not find DataRecord while reading " + ln);
/*     */     }
/*     */     
/* 119 */     throw new InternalError("null data read while reading " + ln);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(RecordWriter out)
/*     */   {
/* 125 */     out.open("DataRecord");
/* 126 */     out.namedIndex("pl", EPluralization.names, this.pl);
/* 127 */     out.stringTable("pluralName", this.pluralNames);
/* 128 */     out.namedIndexArray("gender", EGender.names, this.genders);
/* 129 */     out.stringArray("singularName", this.singularNames);
/* 130 */     out.stringArray("halfName", this.halfNames);
/* 131 */     out.stringArray("numberName", this.numberNames);
/* 132 */     out.stringArray("mediumName", this.mediumNames);
/* 133 */     out.stringArray("shortName", this.shortNames);
/* 134 */     out.stringArray("measure", this.measures);
/* 135 */     out.stringArray("rqdSuffix", this.rqdSuffixes);
/* 136 */     out.stringArray("optSuffix", this.optSuffixes);
/* 137 */     out.stringArray("halves", this.halves);
/* 138 */     out.namedIndexArray("halfPlacement", EHalfPlacement.names, this.halfPlacements);
/*     */     
/* 140 */     out.namedIndexArray("halfSupport", EHalfSupport.names, this.halfSupport);
/* 141 */     out.string("fifteenMinutes", this.fifteenMinutes);
/* 142 */     out.string("fiveMinutes", this.fiveMinutes);
/* 143 */     out.bool("requiresDigitSeparator", this.requiresDigitSeparator);
/* 144 */     out.string("digitPrefix", this.digitPrefix);
/* 145 */     out.string("countSep", this.countSep);
/* 146 */     out.string("shortUnitSep", this.shortUnitSep);
/* 147 */     out.stringArray("unitSep", this.unitSep);
/* 148 */     out.boolArray("unitSepRequiresDP", this.unitSepRequiresDP);
/* 149 */     out.boolArray("requiresSkipMarker", this.requiresSkipMarker);
/* 150 */     out.namedIndex("numberSystem", ENumberSystem.names, this.numberSystem);
/* 151 */     out.character("zero", this.zero);
/* 152 */     out.character("decimalSep", this.decimalSep);
/* 153 */     out.bool("omitSingularCount", this.omitSingularCount);
/* 154 */     out.bool("omitDualCount", this.omitDualCount);
/* 155 */     out.namedIndex("zeroHandling", EZeroHandling.names, this.zeroHandling);
/* 156 */     out.namedIndex("decimalHandling", EDecimalHandling.names, this.decimalHandling);
/*     */     
/* 158 */     out.namedIndex("fractionHandling", EFractionHandling.names, this.fractionHandling);
/*     */     
/* 160 */     out.string("skippedUnitMarker", this.skippedUnitMarker);
/* 161 */     out.bool("allowZero", this.allowZero);
/* 162 */     out.bool("weeksAloneOnly", this.weeksAloneOnly);
/* 163 */     out.namedIndex("useMilliseconds", EMilliSupport.names, this.useMilliseconds);
/* 164 */     if (this.scopeData != null) {
/* 165 */       out.open("ScopeDataList");
/* 166 */       for (int i = 0; i < this.scopeData.length; i++) {
/* 167 */         this.scopeData[i].write(out);
/*     */       }
/* 169 */       out.close();
/*     */     }
/* 171 */     out.close();
/*     */   }
/*     */   
/*     */   public static class ScopeData {
/*     */     String prefix;
/*     */     boolean requiresDigitPrefix;
/*     */     String suffix;
/*     */     
/*     */     public void write(RecordWriter out) {
/* 180 */       out.open("ScopeData");
/* 181 */       out.string("prefix", this.prefix);
/* 182 */       out.bool("requiresDigitPrefix", this.requiresDigitPrefix);
/* 183 */       out.string("suffix", this.suffix);
/* 184 */       out.close();
/*     */     }
/*     */     
/*     */     public static ScopeData read(RecordReader in) {
/* 188 */       if (in.open("ScopeData")) {
/* 189 */         ScopeData scope = new ScopeData();
/* 190 */         scope.prefix = in.string("prefix");
/* 191 */         scope.requiresDigitPrefix = in.bool("requiresDigitPrefix");
/* 192 */         scope.suffix = in.string("suffix");
/* 193 */         if (in.close()) {
/* 194 */           return scope;
/*     */         }
/*     */       }
/* 197 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ETimeLimit {
/*     */     public static final byte NOLIMIT = 0;
/*     */     public static final byte LT = 1;
/*     */     public static final byte MT = 2;
/* 205 */     public static final String[] names = { "NOLIMIT", "LT", "MT" };
/*     */   }
/*     */   
/*     */   public static abstract interface ETimeDirection {
/*     */     public static final byte NODIRECTION = 0;
/*     */     public static final byte PAST = 1;
/*     */     public static final byte FUTURE = 2;
/* 212 */     public static final String[] names = { "NODIRECTION", "PAST", "FUTURE" };
/*     */   }
/*     */   
/*     */   public static abstract interface EUnitVariant {
/*     */     public static final byte PLURALIZED = 0;
/*     */     public static final byte MEDIUM = 1;
/*     */     public static final byte SHORT = 2;
/* 219 */     public static final String[] names = { "PLURALIZED", "MEDIUM", "SHORT" };
/*     */   }
/*     */   
/*     */   public static abstract interface ECountVariant {
/*     */     public static final byte INTEGER = 0;
/*     */     public static final byte INTEGER_CUSTOM = 1;
/*     */     public static final byte HALF_FRACTION = 2;
/*     */     public static final byte DECIMAL1 = 3;
/*     */     public static final byte DECIMAL2 = 4;
/*     */     public static final byte DECIMAL3 = 5;
/* 229 */     public static final String[] names = { "INTEGER", "INTEGER_CUSTOM", "HALF_FRACTION", "DECIMAL1", "DECIMAL2", "DECIMAL3" };
/*     */   }
/*     */   
/*     */   public static abstract interface EPluralization
/*     */   {
/*     */     public static final byte NONE = 0;
/*     */     public static final byte PLURAL = 1;
/*     */     public static final byte DUAL = 2;
/*     */     public static final byte PAUCAL = 3;
/*     */     public static final byte HEBREW = 4;
/*     */     public static final byte ARABIC = 5;
/* 240 */     public static final String[] names = { "NONE", "PLURAL", "DUAL", "PAUCAL", "HEBREW", "ARABIC" };
/*     */   }
/*     */   
/*     */   public static abstract interface EHalfPlacement
/*     */   {
/*     */     public static final byte PREFIX = 0;
/*     */     public static final byte AFTER_FIRST = 1;
/*     */     public static final byte LAST = 2;
/* 248 */     public static final String[] names = { "PREFIX", "AFTER_FIRST", "LAST" };
/*     */   }
/*     */   
/*     */   public static abstract interface ENumberSystem {
/*     */     public static final byte DEFAULT = 0;
/*     */     public static final byte CHINESE_TRADITIONAL = 1;
/*     */     public static final byte CHINESE_SIMPLIFIED = 2;
/*     */     public static final byte KOREAN = 3;
/* 256 */     public static final String[] names = { "DEFAULT", "CHINESE_TRADITIONAL", "CHINESE_SIMPLIFIED", "KOREAN" };
/*     */   }
/*     */   
/*     */   public static abstract interface EZeroHandling
/*     */   {
/*     */     public static final byte ZPLURAL = 0;
/*     */     public static final byte ZSINGULAR = 1;
/* 263 */     public static final String[] names = { "ZPLURAL", "ZSINGULAR" };
/*     */   }
/*     */   
/*     */   public static abstract interface EDecimalHandling {
/*     */     public static final byte DPLURAL = 0;
/*     */     public static final byte DSINGULAR = 1;
/*     */     public static final byte DSINGULAR_SUBONE = 2;
/*     */     public static final byte DPAUCAL = 3;
/* 271 */     public static final String[] names = { "DPLURAL", "DSINGULAR", "DSINGULAR_SUBONE", "DPAUCAL" };
/*     */   }
/*     */   
/*     */   public static abstract interface EFractionHandling
/*     */   {
/*     */     public static final byte FPLURAL = 0;
/*     */     public static final byte FSINGULAR_PLURAL = 1;
/*     */     public static final byte FSINGULAR_PLURAL_ANDAHALF = 2;
/*     */     public static final byte FPAUCAL = 3;
/* 280 */     public static final String[] names = { "FPLURAL", "FSINGULAR_PLURAL", "FSINGULAR_PLURAL_ANDAHALF", "FPAUCAL" };
/*     */   }
/*     */   
/*     */   public static abstract interface EHalfSupport
/*     */   {
/*     */     public static final byte YES = 0;
/*     */     public static final byte NO = 1;
/*     */     public static final byte ONE_PLUS = 2;
/* 288 */     public static final String[] names = { "YES", "NO", "ONE_PLUS" };
/*     */   }
/*     */   
/*     */   public static abstract interface EMilliSupport {
/*     */     public static final byte YES = 0;
/*     */     public static final byte NO = 1;
/*     */     public static final byte WITH_SECONDS = 2;
/* 295 */     public static final String[] names = { "YES", "NO", "WITH_SECONDS" };
/*     */   }
/*     */   
/*     */   public static abstract interface ESeparatorVariant {
/*     */     public static final byte NONE = 0;
/*     */     public static final byte SHORT = 1;
/*     */     public static final byte FULL = 2;
/* 302 */     public static final String[] names = { "NONE", "SHORT", "FULL" };
/*     */   }
/*     */   
/*     */   public static abstract interface EGender {
/*     */     public static final byte M = 0;
/*     */     public static final byte F = 1;
/*     */     public static final byte N = 2;
/* 309 */     public static final String[] names = { "M", "F", "N" };
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\DataRecord.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */