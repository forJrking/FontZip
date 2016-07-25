/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
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
/*     */ class RBBINode
/*     */ {
/*     */   static final int setRef = 0;
/*     */   static final int uset = 1;
/*     */   static final int varRef = 2;
/*     */   static final int leafChar = 3;
/*     */   static final int lookAhead = 4;
/*     */   static final int tag = 5;
/*     */   static final int endMark = 6;
/*     */   static final int opStart = 7;
/*     */   static final int opCat = 8;
/*     */   static final int opOr = 9;
/*     */   static final int opStar = 10;
/*     */   static final int opPlus = 11;
/*     */   static final int opQuestion = 12;
/*     */   static final int opBreak = 13;
/*     */   static final int opReverse = 14;
/*     */   static final int opLParen = 15;
/*     */   static final int nodeTypeLimit = 16;
/*  40 */   static final String[] nodeTypeNames = { "setRef", "uset", "varRef", "leafChar", "lookAhead", "tag", "endMark", "opStart", "opCat", "opOr", "opStar", "opPlus", "opQuestion", "opBreak", "opReverse", "opLParen" };
/*     */   
/*     */ 
/*     */   static final int precZero = 0;
/*     */   
/*     */ 
/*     */   static final int precStart = 1;
/*     */   
/*     */ 
/*     */   static final int precLParen = 2;
/*     */   
/*     */ 
/*     */   static final int precOpOr = 3;
/*     */   
/*     */ 
/*     */   static final int precOpCat = 4;
/*     */   
/*     */ 
/*     */   int fType;
/*     */   
/*     */ 
/*     */   RBBINode fParent;
/*     */   
/*     */ 
/*     */   RBBINode fLeftChild;
/*     */   
/*     */ 
/*     */   RBBINode fRightChild;
/*     */   
/*     */   UnicodeSet fInputSet;
/*     */   
/*  71 */   int fPrecedence = 0;
/*     */   
/*     */ 
/*     */   String fText;
/*     */   
/*     */ 
/*     */   int fFirstPos;
/*     */   
/*     */ 
/*     */   int fLastPos;
/*     */   
/*     */ 
/*     */   boolean fNullable;
/*     */   
/*     */   int fVal;
/*     */   
/*     */   boolean fLookAheadEnd;
/*     */   
/*     */   Set<RBBINode> fFirstPosSet;
/*     */   
/*     */   Set<RBBINode> fLastPosSet;
/*     */   
/*     */   Set<RBBINode> fFollowPos;
/*     */   
/*     */   int fSerialNum;
/*     */   
/*     */   static int gLastSerial;
/*     */   
/*     */ 
/*     */   RBBINode(int t)
/*     */   {
/* 102 */     Assert.assrt(t < 16);
/* 103 */     this.fSerialNum = (++gLastSerial);
/* 104 */     this.fType = t;
/*     */     
/* 106 */     this.fFirstPosSet = new HashSet();
/* 107 */     this.fLastPosSet = new HashSet();
/* 108 */     this.fFollowPos = new HashSet();
/* 109 */     if (t == 8) {
/* 110 */       this.fPrecedence = 4;
/* 111 */     } else if (t == 9) {
/* 112 */       this.fPrecedence = 3;
/* 113 */     } else if (t == 7) {
/* 114 */       this.fPrecedence = 1;
/* 115 */     } else if (t == 15) {
/* 116 */       this.fPrecedence = 2;
/*     */     } else {
/* 118 */       this.fPrecedence = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   RBBINode(RBBINode other) {
/* 123 */     this.fSerialNum = (++gLastSerial);
/* 124 */     this.fType = other.fType;
/* 125 */     this.fInputSet = other.fInputSet;
/* 126 */     this.fPrecedence = other.fPrecedence;
/* 127 */     this.fText = other.fText;
/* 128 */     this.fFirstPos = other.fFirstPos;
/* 129 */     this.fLastPos = other.fLastPos;
/* 130 */     this.fNullable = other.fNullable;
/* 131 */     this.fVal = other.fVal;
/* 132 */     this.fFirstPosSet = new HashSet(other.fFirstPosSet);
/* 133 */     this.fLastPosSet = new HashSet(other.fLastPosSet);
/* 134 */     this.fFollowPos = new HashSet(other.fFollowPos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   RBBINode cloneTree()
/*     */   {
/*     */     RBBINode n;
/*     */     
/*     */ 
/*     */ 
/*     */     RBBINode n;
/*     */     
/*     */ 
/* 149 */     if (this.fType == 2)
/*     */     {
/*     */ 
/* 152 */       n = this.fLeftChild.cloneTree(); } else { RBBINode n;
/* 153 */       if (this.fType == 1) {
/* 154 */         n = this;
/*     */       } else {
/* 156 */         n = new RBBINode(this);
/* 157 */         if (this.fLeftChild != null) {
/* 158 */           n.fLeftChild = this.fLeftChild.cloneTree();
/* 159 */           n.fLeftChild.fParent = n;
/*     */         }
/* 161 */         if (this.fRightChild != null) {
/* 162 */           n.fRightChild = this.fRightChild.cloneTree();
/* 163 */           n.fRightChild.fParent = n;
/*     */         }
/*     */       } }
/* 166 */     return n;
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
/*     */   RBBINode flattenVariables()
/*     */   {
/* 190 */     if (this.fType == 2) {
/* 191 */       RBBINode retNode = this.fLeftChild.cloneTree();
/*     */       
/* 193 */       return retNode;
/*     */     }
/*     */     
/* 196 */     if (this.fLeftChild != null) {
/* 197 */       this.fLeftChild = this.fLeftChild.flattenVariables();
/* 198 */       this.fLeftChild.fParent = this;
/*     */     }
/* 200 */     if (this.fRightChild != null) {
/* 201 */       this.fRightChild = this.fRightChild.flattenVariables();
/* 202 */       this.fRightChild.fParent = this;
/*     */     }
/* 204 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void flattenSets()
/*     */   {
/* 216 */     Assert.assrt(this.fType != 0);
/*     */     
/* 218 */     if (this.fLeftChild != null) {
/* 219 */       if (this.fLeftChild.fType == 0) {
/* 220 */         RBBINode setRefNode = this.fLeftChild;
/* 221 */         RBBINode usetNode = setRefNode.fLeftChild;
/* 222 */         RBBINode replTree = usetNode.fLeftChild;
/* 223 */         this.fLeftChild = replTree.cloneTree();
/* 224 */         this.fLeftChild.fParent = this;
/*     */       } else {
/* 226 */         this.fLeftChild.flattenSets();
/*     */       }
/*     */     }
/*     */     
/* 230 */     if (this.fRightChild != null) {
/* 231 */       if (this.fRightChild.fType == 0) {
/* 232 */         RBBINode setRefNode = this.fRightChild;
/* 233 */         RBBINode usetNode = setRefNode.fLeftChild;
/* 234 */         RBBINode replTree = usetNode.fLeftChild;
/* 235 */         this.fRightChild = replTree.cloneTree();
/* 236 */         this.fRightChild.fParent = this;
/*     */       }
/*     */       else {
/* 239 */         this.fRightChild.flattenSets();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void findNodes(List<RBBINode> dest, int kind)
/*     */   {
/* 251 */     if (this.fType == kind) {
/* 252 */       dest.add(this);
/*     */     }
/* 254 */     if (this.fLeftChild != null) {
/* 255 */       this.fLeftChild.findNodes(dest, kind);
/*     */     }
/* 257 */     if (this.fRightChild != null) {
/* 258 */       this.fRightChild.findNodes(dest, kind);
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
/*     */   static void printNode(RBBINode n)
/*     */   {
/* 272 */     if (n == null) {
/* 273 */       System.out.print(" -- null --\n");
/*     */     } else {
/* 275 */       printInt(n.fSerialNum, 10);
/* 276 */       printString(nodeTypeNames[n.fType], 11);
/* 277 */       printInt(n.fParent == null ? 0 : n.fParent.fSerialNum, 11);
/* 278 */       printInt(n.fLeftChild == null ? 0 : n.fLeftChild.fSerialNum, 11);
/* 279 */       printInt(n.fRightChild == null ? 0 : n.fRightChild.fSerialNum, 12);
/* 280 */       printInt(n.fFirstPos, 12);
/* 281 */       printInt(n.fVal, 7);
/*     */       
/* 283 */       if (n.fType == 2) {
/* 284 */         System.out.print(" " + n.fText);
/*     */       }
/*     */     }
/* 287 */     System.out.println("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void printString(String s, int minWidth)
/*     */   {
/* 296 */     for (int i = minWidth; i < 0; i++)
/*     */     {
/* 298 */       System.out.print(' ');
/*     */     }
/* 300 */     for (int i = s.length(); i < minWidth; i++) {
/* 301 */       System.out.print(' ');
/*     */     }
/* 303 */     System.out.print(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void printInt(int i, int minWidth)
/*     */   {
/* 313 */     String s = Integer.toString(i);
/* 314 */     printString(s, Math.max(minWidth, s.length() + 1));
/*     */   }
/*     */   
/*     */ 
/*     */   static void printHex(int i, int minWidth)
/*     */   {
/* 320 */     String s = Integer.toString(i, 16);
/* 321 */     String leadingZeroes = "00000".substring(0, Math.max(0, 5 - s.length()));
/*     */     
/* 323 */     s = leadingZeroes + s;
/* 324 */     printString(s, minWidth);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void printTree(boolean printHeading)
/*     */   {
/* 336 */     if (printHeading) {
/* 337 */       System.out.println("-------------------------------------------------------------------");
/* 338 */       System.out.println("    Serial       type     Parent  LeftChild  RightChild    position  value");
/*     */     }
/* 340 */     printNode(this);
/*     */     
/*     */ 
/* 343 */     if (this.fType != 2) {
/* 344 */       if (this.fLeftChild != null) {
/* 345 */         this.fLeftChild.printTree(false);
/*     */       }
/*     */       
/* 348 */       if (this.fRightChild != null) {
/* 349 */         this.fRightChild.printTree(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBINode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */