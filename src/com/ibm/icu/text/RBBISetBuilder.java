/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import com.ibm.icu.impl.IntTrieBuilder;
/*     */ import com.ibm.icu.impl.TrieBuilder.DataManipulate;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RBBISetBuilder
/*     */ {
/*     */   RBBIRuleBuilder fRB;
/*     */   RangeDescriptor fRangeList;
/*     */   IntTrieBuilder fTrie;
/*     */   int fTrieSize;
/*     */   int fGroupCount;
/*     */   boolean fSawBOF;
/*     */   
/*     */   static class RangeDescriptor
/*     */   {
/*     */     int fStartChar;
/*     */     int fEndChar;
/*     */     int fNum;
/*     */     List<RBBINode> fIncludesSets;
/*     */     RangeDescriptor fNext;
/*     */     
/*     */     RangeDescriptor()
/*     */     {
/*  48 */       this.fIncludesSets = new ArrayList();
/*     */     }
/*     */     
/*     */     RangeDescriptor(RangeDescriptor other) {
/*  52 */       this.fStartChar = other.fStartChar;
/*  53 */       this.fEndChar = other.fEndChar;
/*  54 */       this.fNum = other.fNum;
/*  55 */       this.fIncludesSets = new ArrayList(other.fIncludesSets);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void split(int where)
/*     */     {
/*  64 */       Assert.assrt((where > this.fStartChar) && (where <= this.fEndChar));
/*  65 */       RangeDescriptor nr = new RangeDescriptor(this);
/*     */       
/*     */ 
/*     */ 
/*  69 */       nr.fStartChar = where;
/*  70 */       this.fEndChar = (where - 1);
/*  71 */       nr.fNext = this.fNext;
/*  72 */       this.fNext = nr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void setDictionaryFlag()
/*     */     {
/* 101 */       for (int i = 0; i < this.fIncludesSets.size(); i++) {
/* 102 */         RBBINode usetNode = (RBBINode)this.fIncludesSets.get(i);
/* 103 */         String setName = "";
/* 104 */         RBBINode setRef = usetNode.fParent;
/* 105 */         if (setRef != null) {
/* 106 */           RBBINode varRef = setRef.fParent;
/* 107 */           if ((varRef != null) && (varRef.fType == 2)) {
/* 108 */             setName = varRef.fText;
/*     */           }
/*     */         }
/* 111 */         if (setName.equals("dictionary")) {
/* 112 */           this.fNum |= 0x4000;
/* 113 */           break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   RBBISetBuilder(RBBIRuleBuilder rb)
/*     */   {
/* 145 */     this.fRB = rb;
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
/*     */   void build()
/*     */   {
/* 158 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("usets") >= 0)) { printSets();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 163 */     this.fRangeList = new RangeDescriptor();
/* 164 */     this.fRangeList.fStartChar = 0;
/* 165 */     this.fRangeList.fEndChar = 1114111;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     for (RBBINode usetNode : this.fRB.fUSetNodes) {
/* 171 */       UnicodeSet inputSet = usetNode.fInputSet;
/* 172 */       int inputSetRangeCount = inputSet.getRangeCount();
/* 173 */       int inputSetRangeIndex = 0;
/* 174 */       RangeDescriptor rlRange = this.fRangeList;
/*     */       
/*     */ 
/* 177 */       while (inputSetRangeIndex < inputSetRangeCount)
/*     */       {
/*     */ 
/* 180 */         int inputSetRangeBegin = inputSet.getRangeStart(inputSetRangeIndex);
/* 181 */         int inputSetRangeEnd = inputSet.getRangeEnd(inputSetRangeIndex);
/*     */         
/*     */ 
/*     */ 
/* 185 */         while (rlRange.fEndChar < inputSetRangeBegin) {
/* 186 */           rlRange = rlRange.fNext;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */         if (rlRange.fStartChar < inputSetRangeBegin) {
/* 196 */           rlRange.split(inputSetRangeBegin);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 205 */           if (rlRange.fEndChar > inputSetRangeEnd) {
/* 206 */             rlRange.split(inputSetRangeEnd + 1);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 211 */           if (rlRange.fIncludesSets.indexOf(usetNode) == -1) {
/* 212 */             rlRange.fIncludesSets.add(usetNode);
/*     */           }
/*     */           
/*     */ 
/* 216 */           if (inputSetRangeEnd == rlRange.fEndChar) {
/* 217 */             inputSetRangeIndex++;
/*     */           }
/* 219 */           rlRange = rlRange.fNext;
/*     */         }
/*     */       }
/*     */     }
/* 223 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("range") >= 0)) { printRanges();
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
/*     */ 
/* 237 */     for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
/* 238 */       for (RangeDescriptor rlSearchRange = this.fRangeList; rlSearchRange != rlRange; rlSearchRange = rlSearchRange.fNext) {
/* 239 */         if (rlRange.fIncludesSets.equals(rlSearchRange.fIncludesSets)) {
/* 240 */           rlRange.fNum = rlSearchRange.fNum;
/* 241 */           break;
/*     */         }
/*     */       }
/* 244 */       if (rlRange.fNum == 0) {
/* 245 */         this.fGroupCount += 1;
/* 246 */         rlRange.fNum = (this.fGroupCount + 2);
/* 247 */         rlRange.setDictionaryFlag();
/* 248 */         addValToSets(rlRange.fIncludesSets, this.fGroupCount + 2);
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
/* 262 */     String eofString = "eof";
/* 263 */     String bofString = "bof";
/*     */     
/* 265 */     for (RBBINode usetNode : this.fRB.fUSetNodes) {
/* 266 */       UnicodeSet inputSet = usetNode.fInputSet;
/* 267 */       if (inputSet.contains(eofString)) {
/* 268 */         addValToSet(usetNode, 1);
/*     */       }
/* 270 */       if (inputSet.contains(bofString)) {
/* 271 */         addValToSet(usetNode, 2);
/* 272 */         this.fSawBOF = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 277 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("rgroup") >= 0)) printRangeGroups();
/* 278 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("esets") >= 0)) { printSets();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */     this.fTrie = new IntTrieBuilder(null, 100000, 0, 0, true);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 291 */     for (rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
/* 292 */       this.fTrie.setRange(rlRange.fStartChar, rlRange.fEndChar + 1, rlRange.fNum, true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   class RBBIDataManipulate
/*     */     implements TrieBuilder.DataManipulate
/*     */   {
/*     */     RBBIDataManipulate() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public int getFoldedValue(int start, int offset)
/*     */     {
/* 308 */       boolean[] inBlockZero = new boolean[1];
/*     */       
/* 310 */       int limit = start + 1024;
/* 311 */       while (start < limit) {
/* 312 */         int value = RBBISetBuilder.this.fTrie.getValue(start, inBlockZero);
/* 313 */         if (inBlockZero[0] != 0) {
/* 314 */           start += 32;
/* 315 */         } else { if (value != 0) {
/* 316 */             return offset | 0x8000;
/*     */           }
/* 318 */           start++;
/*     */         }
/*     */       }
/* 321 */       return 0;
/*     */     } }
/*     */   
/* 324 */   RBBIDataManipulate dm = new RBBIDataManipulate();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getTrieSize()
/*     */   {
/* 332 */     int size = 0;
/*     */     
/*     */     try
/*     */     {
/* 336 */       size = this.fTrie.serialize(null, true, this.dm);
/*     */     } catch (IOException e) {
/* 338 */       Assert.assrt(false);
/*     */     }
/* 340 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void serializeTrie(OutputStream os)
/*     */     throws IOException
/*     */   {
/* 350 */     this.fTrie.serialize(os, true, this.dm);
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
/*     */   void addValToSets(List<RBBINode> sets, int val)
/*     */   {
/* 368 */     for (RBBINode usetNode : sets) {
/* 369 */       addValToSet(usetNode, val);
/*     */     }
/*     */   }
/*     */   
/*     */   void addValToSet(RBBINode usetNode, int val) {
/* 374 */     RBBINode leafNode = new RBBINode(3);
/* 375 */     leafNode.fVal = val;
/* 376 */     if (usetNode.fLeftChild == null) {
/* 377 */       usetNode.fLeftChild = leafNode;
/* 378 */       leafNode.fParent = usetNode;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 383 */       RBBINode orNode = new RBBINode(9);
/* 384 */       orNode.fLeftChild = usetNode.fLeftChild;
/* 385 */       orNode.fRightChild = leafNode;
/* 386 */       orNode.fLeftChild.fParent = orNode;
/* 387 */       orNode.fRightChild.fParent = orNode;
/* 388 */       usetNode.fLeftChild = orNode;
/* 389 */       orNode.fParent = usetNode;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getNumCharCategories()
/*     */   {
/* 400 */     return this.fGroupCount + 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean sawBOF()
/*     */   {
/* 410 */     return this.fSawBOF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getFirstChar(int category)
/*     */   {
/* 422 */     int retVal = -1;
/* 423 */     for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
/* 424 */       if (rlRange.fNum == category) {
/* 425 */         retVal = rlRange.fStartChar;
/* 426 */         break;
/*     */       }
/*     */     }
/* 429 */     return retVal;
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
/*     */   void printRanges()
/*     */   {
/* 445 */     System.out.print("\n\n Nonoverlapping Ranges ...\n");
/* 446 */     for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
/* 447 */       System.out.print(" " + rlRange.fNum + "   " + rlRange.fStartChar + "-" + rlRange.fEndChar);
/*     */       
/* 449 */       for (int i = 0; i < rlRange.fIncludesSets.size(); i++) {
/* 450 */         RBBINode usetNode = (RBBINode)rlRange.fIncludesSets.get(i);
/* 451 */         String setName = "anon";
/* 452 */         RBBINode setRef = usetNode.fParent;
/* 453 */         if (setRef != null) {
/* 454 */           RBBINode varRef = setRef.fParent;
/* 455 */           if ((varRef != null) && (varRef.fType == 2)) {
/* 456 */             setName = varRef.fText;
/*     */           }
/*     */         }
/* 459 */         System.out.print(setName);System.out.print("  ");
/*     */       }
/* 461 */       System.out.println("");
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
/*     */   void printRangeGroups()
/*     */   {
/* 478 */     int lastPrintedGroupNum = 0;
/*     */     
/* 480 */     System.out.print("\nRanges grouped by Unicode Set Membership...\n");
/* 481 */     for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
/* 482 */       int groupNum = rlRange.fNum & 0xBFFF;
/* 483 */       if (groupNum > lastPrintedGroupNum) {
/* 484 */         lastPrintedGroupNum = groupNum;
/* 485 */         if (groupNum < 10) System.out.print(" ");
/* 486 */         System.out.print(groupNum + " ");
/*     */         
/* 488 */         if ((rlRange.fNum & 0x4000) != 0) { System.out.print(" <DICT> ");
/*     */         }
/* 490 */         for (int i = 0; i < rlRange.fIncludesSets.size(); i++) {
/* 491 */           RBBINode usetNode = (RBBINode)rlRange.fIncludesSets.get(i);
/* 492 */           String setName = "anon";
/* 493 */           RBBINode setRef = usetNode.fParent;
/* 494 */           if (setRef != null) {
/* 495 */             RBBINode varRef = setRef.fParent;
/* 496 */             if ((varRef != null) && (varRef.fType == 2)) {
/* 497 */               setName = varRef.fText;
/*     */             }
/*     */           }
/* 500 */           System.out.print(setName);System.out.print(" ");
/*     */         }
/*     */         
/* 503 */         i = 0;
/* 504 */         for (RangeDescriptor tRange = rlRange; tRange != null; tRange = tRange.fNext) {
/* 505 */           if (tRange.fNum == rlRange.fNum) {
/* 506 */             if (i++ % 5 == 0) {
/* 507 */               System.out.print("\n    ");
/*     */             }
/* 509 */             RBBINode.printHex(tRange.fStartChar, -1);
/* 510 */             System.out.print("-");
/* 511 */             RBBINode.printHex(tRange.fEndChar, 0);
/*     */           }
/*     */         }
/* 514 */         System.out.print("\n");
/*     */       }
/*     */     }
/* 517 */     System.out.print("\n");
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
/*     */   void printSets()
/*     */   {
/* 531 */     System.out.print("\n\nUnicode Sets List\n------------------\n");
/* 532 */     for (int i = 0; i < this.fRB.fUSetNodes.size(); i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 538 */       RBBINode usetNode = (RBBINode)this.fRB.fUSetNodes.get(i);
/*     */       
/*     */ 
/* 541 */       RBBINode.printInt(2, i);
/* 542 */       String setName = "anonymous";
/* 543 */       RBBINode setRef = usetNode.fParent;
/* 544 */       if (setRef != null) {
/* 545 */         RBBINode varRef = setRef.fParent;
/* 546 */         if ((varRef != null) && (varRef.fType == 2)) {
/* 547 */           setName = varRef.fText;
/*     */         }
/*     */       }
/* 550 */       System.out.print("  " + setName);
/* 551 */       System.out.print("   ");
/* 552 */       System.out.print(usetNode.fText);
/* 553 */       System.out.print("\n");
/* 554 */       if (usetNode.fLeftChild != null) {
/* 555 */         usetNode.fLeftChild.printTree(true);
/*     */       }
/*     */     }
/* 558 */     System.out.print("\n");
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBISetBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */