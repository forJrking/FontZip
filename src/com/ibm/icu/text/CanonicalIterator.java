/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Norm2AllModes;
/*     */ import com.ibm.icu.impl.Normalizer2Impl;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CanonicalIterator
/*     */ {
/*     */   public CanonicalIterator(String source)
/*     */   {
/*  51 */     Norm2AllModes allModes = Norm2AllModes.getNFCInstance();
/*  52 */     this.nfd = allModes.decomp;
/*  53 */     this.nfcImpl = allModes.impl.ensureCanonIterData();
/*  54 */     setSource(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSource()
/*     */   {
/*  63 */     return this.source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  71 */     this.done = false;
/*  72 */     for (int i = 0; i < this.current.length; i++) {
/*  73 */       this.current[i] = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String next()
/*     */   {
/*  85 */     if (this.done) { return null;
/*     */     }
/*     */     
/*     */ 
/*  89 */     this.buffer.setLength(0);
/*  90 */     for (int i = 0; i < this.pieces.length; i++) {
/*  91 */       this.buffer.append(this.pieces[i][this.current[i]]);
/*     */     }
/*  93 */     String result = this.buffer.toString();
/*     */     
/*     */ 
/*     */ 
/*  97 */     for (int i = this.current.length - 1;; i--) {
/*  98 */       if (i < 0) {
/*  99 */         this.done = true;
/* 100 */         break;
/*     */       }
/* 102 */       this.current[i] += 1;
/* 103 */       if (this.current[i] < this.pieces[i].length) break;
/* 104 */       this.current[i] = 0;
/*     */     }
/* 106 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSource(String newSource)
/*     */   {
/* 116 */     this.source = this.nfd.normalize(newSource);
/* 117 */     this.done = false;
/*     */     
/*     */ 
/* 120 */     if (newSource.length() == 0) {
/* 121 */       this.pieces = new String[1][];
/* 122 */       this.current = new int[1];
/* 123 */       this.pieces[0] = { "" };
/* 124 */       return;
/*     */     }
/*     */     
/*     */ 
/* 128 */     List<String> segmentList = new ArrayList();
/*     */     
/* 130 */     int start = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 135 */     int i = UTF16.findOffsetFromCodePoint(this.source, 1);
/*     */     int cp;
/* 137 */     for (; i < this.source.length(); i += Character.charCount(cp)) {
/* 138 */       cp = this.source.codePointAt(i);
/* 139 */       if (this.nfcImpl.isCanonSegmentStarter(cp)) {
/* 140 */         segmentList.add(this.source.substring(start, i));
/* 141 */         start = i;
/*     */       }
/*     */     }
/* 144 */     segmentList.add(this.source.substring(start, i));
/*     */     
/*     */ 
/* 147 */     this.pieces = new String[segmentList.size()][];
/* 148 */     this.current = new int[segmentList.size()];
/* 149 */     for (i = 0; i < this.pieces.length; i++) {
/* 150 */       if (PROGRESS) System.out.println("SEGMENT");
/* 151 */       this.pieces[i] = getEquivalents((String)segmentList.get(i));
/*     */     }
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static void permute(String source, boolean skipZeros, Set<String> output)
/*     */   {
/* 171 */     if ((source.length() <= 2) && (UTF16.countCodePoint(source) <= 1)) {
/* 172 */       output.add(source);
/* 173 */       return;
/*     */     }
/*     */     
/*     */ 
/* 177 */     Set<String> subpermute = new HashSet();
/*     */     int cp;
/* 179 */     String chStr; for (int i = 0; i < source.length(); i += UTF16.getCharCount(cp)) {
/* 180 */       cp = UTF16.charAt(source, i);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 185 */       if ((!skipZeros) || (i == 0) || (UCharacter.getCombiningClass(cp) != 0))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */         subpermute.clear();
/* 192 */         permute(source.substring(0, i) + source.substring(i + UTF16.getCharCount(cp)), skipZeros, subpermute);
/*     */         
/*     */ 
/*     */ 
/* 196 */         chStr = UTF16.valueOf(source, i);
/* 197 */         for (String s : subpermute) {
/* 198 */           String piece = chStr + s;
/*     */           
/* 200 */           output.add(piece);
/*     */         }
/*     */       }
/*     */     }
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
/* 227 */   private static boolean PROGRESS = false;
/*     */   
/* 229 */   private static boolean SKIP_ZEROS = true;
/*     */   
/*     */   private final Normalizer2 nfd;
/*     */   
/*     */   private final Normalizer2Impl nfcImpl;
/*     */   
/*     */   private String source;
/*     */   
/*     */   private boolean done;
/*     */   
/*     */   private String[][] pieces;
/*     */   
/*     */   private int[] current;
/*     */   
/* 243 */   private transient StringBuilder buffer = new StringBuilder();
/*     */   
/*     */ 
/*     */   private String[] getEquivalents(String segment)
/*     */   {
/* 248 */     Set<String> result = new HashSet();
/* 249 */     Set<String> basic = getEquivalents2(segment);
/* 250 */     Set<String> permutations = new HashSet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 255 */     Iterator<String> it = basic.iterator();
/* 256 */     while (it.hasNext()) {
/* 257 */       String item = (String)it.next();
/* 258 */       permutations.clear();
/* 259 */       permute(item, SKIP_ZEROS, permutations);
/* 260 */       Iterator<String> it2 = permutations.iterator();
/* 261 */       while (it2.hasNext()) {
/* 262 */         String possible = (String)it2.next();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */         if (Normalizer.compare(possible, segment, 0) == 0)
/*     */         {
/* 270 */           if (PROGRESS) System.out.println("Adding Permutation: " + Utility.hex(possible));
/* 271 */           result.add(possible);
/*     */ 
/*     */         }
/* 274 */         else if (PROGRESS) { System.out.println("-Skipping Permutation: " + Utility.hex(possible));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 280 */     String[] finalResult = new String[result.size()];
/* 281 */     result.toArray(finalResult);
/* 282 */     return finalResult;
/*     */   }
/*     */   
/*     */ 
/*     */   private Set<String> getEquivalents2(String segment)
/*     */   {
/* 288 */     Set<String> result = new HashSet();
/*     */     
/* 290 */     if (PROGRESS) { System.out.println("Adding: " + Utility.hex(segment));
/*     */     }
/* 292 */     result.add(segment);
/* 293 */     StringBuffer workingBuffer = new StringBuffer();
/* 294 */     UnicodeSet starts = new UnicodeSet();
/*     */     int cp;
/*     */     UnicodeSetIterator iter;
/*     */     String prefix;
/* 298 */     for (int i = 0; i < segment.length(); i += Character.charCount(cp))
/*     */     {
/*     */ 
/* 301 */       cp = segment.codePointAt(i);
/* 302 */       if (this.nfcImpl.getCanonStartSet(cp, starts))
/*     */       {
/*     */ 
/*     */ 
/* 306 */         for (iter = new UnicodeSetIterator(starts); iter.next();) {
/* 307 */           int cp2 = iter.codepoint;
/* 308 */           Set<String> remainder = extract(cp2, segment, i, workingBuffer);
/* 309 */           if (remainder != null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 314 */             prefix = segment.substring(0, i);
/* 315 */             prefix = prefix + UTF16.valueOf(cp2);
/* 316 */             for (String item : remainder)
/* 317 */               result.add(prefix + item);
/*     */           }
/*     */         } }
/*     */     }
/* 321 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<String> extract(int comp, String segment, int segmentPos, StringBuffer buf)
/*     */   {
/* 366 */     if (PROGRESS) { System.out.println(" extract: " + Utility.hex(UTF16.valueOf(comp)) + ", " + Utility.hex(segment.substring(segmentPos)));
/*     */     }
/*     */     
/* 369 */     String decomp = this.nfcImpl.getDecomposition(comp);
/* 370 */     if (decomp == null) {
/* 371 */       decomp = UTF16.valueOf(comp);
/*     */     }
/*     */     
/*     */ 
/* 375 */     boolean ok = false;
/*     */     
/* 377 */     int decompPos = 0;
/* 378 */     int decompCp = UTF16.charAt(decomp, 0);
/* 379 */     decompPos += UTF16.getCharCount(decompCp);
/*     */     
/* 381 */     buf.setLength(0);
/*     */     int cp;
/* 383 */     for (int i = segmentPos; i < segment.length(); i += UTF16.getCharCount(cp)) {
/* 384 */       cp = UTF16.charAt(segment, i);
/* 385 */       if (cp == decompCp) {
/* 386 */         if (PROGRESS) System.out.println("  matches: " + Utility.hex(UTF16.valueOf(cp)));
/* 387 */         if (decompPos == decomp.length()) {
/* 388 */           buf.append(segment.substring(i + UTF16.getCharCount(cp)));
/* 389 */           ok = true;
/* 390 */           break;
/*     */         }
/* 392 */         decompCp = UTF16.charAt(decomp, decompPos);
/* 393 */         decompPos += UTF16.getCharCount(decompCp);
/*     */       }
/*     */       else {
/* 396 */         if (PROGRESS) { System.out.println("  buffer: " + Utility.hex(UTF16.valueOf(cp)));
/*     */         }
/* 398 */         UTF16.append(buf, cp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */     if (!ok) return null;
/* 413 */     if (PROGRESS) System.out.println("Matches");
/* 414 */     if (buf.length() == 0) return SET_WITH_NULL_STRING;
/* 415 */     String remainder = buf.toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 424 */     if (0 != Normalizer.compare(UTF16.valueOf(comp) + remainder, segment.substring(segmentPos), 0)) { return null;
/*     */     }
/*     */     
/* 427 */     return getEquivalents2(remainder);
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
/* 442 */   private static final Set<String> SET_WITH_NULL_STRING = new HashSet();
/*     */   
/* 444 */   static { SET_WITH_NULL_STRING.add(""); }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CanonicalIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */