/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.Assert;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class RBBITableBuilder
/*      */ {
/*      */   private RBBIRuleBuilder fRB;
/*      */   private int fRootIx;
/*      */   private List<RBBIStateDescriptor> fDStates;
/*      */   
/*      */   static class RBBIStateDescriptor
/*      */   {
/*      */     boolean fMarked;
/*      */     int fAccepting;
/*      */     int fLookAhead;
/*      */     SortedSet<Integer> fTagVals;
/*      */     int fTagsIdx;
/*      */     Set<RBBINode> fPositions;
/*      */     int[] fDtran;
/*      */     
/*      */     RBBIStateDescriptor(int maxInputSymbol)
/*      */     {
/*   53 */       this.fTagVals = new TreeSet();
/*   54 */       this.fPositions = new HashSet();
/*   55 */       this.fDtran = new int[maxInputSymbol + 1];
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
/*      */   RBBITableBuilder(RBBIRuleBuilder rb, int rootNodeIx)
/*      */   {
/*   80 */     this.fRootIx = rootNodeIx;
/*   81 */     this.fRB = rb;
/*   82 */     this.fDStates = new ArrayList();
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
/*      */   void build()
/*      */   {
/*   97 */     if (this.fRB.fTreeRoots[this.fRootIx] == null) {
/*   98 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  105 */     this.fRB.fTreeRoots[this.fRootIx] = this.fRB.fTreeRoots[this.fRootIx].flattenVariables();
/*  106 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("ftree") >= 0)) {
/*  107 */       System.out.println("Parse tree after flattening variable references.");
/*  108 */       this.fRB.fTreeRoots[this.fRootIx].printTree(true);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  117 */     if (this.fRB.fSetBuilder.sawBOF()) {
/*  118 */       RBBINode bofTop = new RBBINode(8);
/*  119 */       RBBINode bofLeaf = new RBBINode(3);
/*  120 */       bofTop.fLeftChild = bofLeaf;
/*  121 */       bofTop.fRightChild = this.fRB.fTreeRoots[this.fRootIx];
/*  122 */       bofLeaf.fParent = bofTop;
/*  123 */       bofLeaf.fVal = 2;
/*  124 */       this.fRB.fTreeRoots[this.fRootIx] = bofTop;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  132 */     RBBINode cn = new RBBINode(8);
/*  133 */     cn.fLeftChild = this.fRB.fTreeRoots[this.fRootIx];
/*  134 */     this.fRB.fTreeRoots[this.fRootIx].fParent = cn;
/*  135 */     cn.fRightChild = new RBBINode(6);
/*  136 */     cn.fRightChild.fParent = cn;
/*  137 */     this.fRB.fTreeRoots[this.fRootIx] = cn;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */     this.fRB.fTreeRoots[this.fRootIx].flattenSets();
/*  144 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("stree") >= 0)) {
/*  145 */       System.out.println("Parse tree after flattening Unicode Set references.");
/*  146 */       this.fRB.fTreeRoots[this.fRootIx].printTree(true);
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
/*  157 */     calcNullable(this.fRB.fTreeRoots[this.fRootIx]);
/*  158 */     calcFirstPos(this.fRB.fTreeRoots[this.fRootIx]);
/*  159 */     calcLastPos(this.fRB.fTreeRoots[this.fRootIx]);
/*  160 */     calcFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
/*  161 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("pos") >= 0)) {
/*  162 */       System.out.print("\n");
/*  163 */       printPosSets(this.fRB.fTreeRoots[this.fRootIx]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  169 */     if (this.fRB.fChainRules) {
/*  170 */       calcChainedFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  176 */     if (this.fRB.fSetBuilder.sawBOF()) {
/*  177 */       bofFixup();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  183 */     buildStateTable();
/*  184 */     flagAcceptingStates();
/*  185 */     flagLookAheadStates();
/*  186 */     flagTaggedStates();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  193 */     mergeRuleStatusVals();
/*      */     
/*  195 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("states") >= 0)) { printStates();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void calcNullable(RBBINode n)
/*      */   {
/*  206 */     if (n == null) {
/*  207 */       return;
/*      */     }
/*  209 */     if ((n.fType == 0) || (n.fType == 6))
/*      */     {
/*      */ 
/*  212 */       n.fNullable = false;
/*  213 */       return;
/*      */     }
/*      */     
/*  216 */     if ((n.fType == 4) || (n.fType == 5))
/*      */     {
/*      */ 
/*  219 */       n.fNullable = true;
/*  220 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  226 */     calcNullable(n.fLeftChild);
/*  227 */     calcNullable(n.fRightChild);
/*      */     
/*      */ 
/*  230 */     if (n.fType == 9) {
/*  231 */       n.fNullable = ((n.fLeftChild.fNullable) || (n.fRightChild.fNullable));
/*      */     }
/*  233 */     else if (n.fType == 8) {
/*  234 */       n.fNullable = ((n.fLeftChild.fNullable) && (n.fRightChild.fNullable));
/*      */     }
/*  236 */     else if ((n.fType == 10) || (n.fType == 12)) {
/*  237 */       n.fNullable = true;
/*      */     }
/*      */     else {
/*  240 */       n.fNullable = false;
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
/*      */   void calcFirstPos(RBBINode n)
/*      */   {
/*  253 */     if (n == null) {
/*  254 */       return;
/*      */     }
/*  256 */     if ((n.fType == 3) || (n.fType == 6) || (n.fType == 4) || (n.fType == 5))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  261 */       n.fFirstPosSet.add(n);
/*  262 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  267 */     calcFirstPos(n.fLeftChild);
/*  268 */     calcFirstPos(n.fRightChild);
/*      */     
/*      */ 
/*  271 */     if (n.fType == 9) {
/*  272 */       n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
/*  273 */       n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
/*      */     }
/*  275 */     else if (n.fType == 8) {
/*  276 */       n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
/*  277 */       if (n.fLeftChild.fNullable) {
/*  278 */         n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
/*      */       }
/*      */     }
/*  281 */     else if ((n.fType == 10) || (n.fType == 12) || (n.fType == 11))
/*      */     {
/*      */ 
/*  284 */       n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void calcLastPos(RBBINode n)
/*      */   {
/*  296 */     if (n == null) {
/*  297 */       return;
/*      */     }
/*  299 */     if ((n.fType == 3) || (n.fType == 6) || (n.fType == 4) || (n.fType == 5))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  304 */       n.fLastPosSet.add(n);
/*  305 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  310 */     calcLastPos(n.fLeftChild);
/*  311 */     calcLastPos(n.fRightChild);
/*      */     
/*      */ 
/*  314 */     if (n.fType == 9) {
/*  315 */       n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
/*  316 */       n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
/*      */     }
/*  318 */     else if (n.fType == 8) {
/*  319 */       n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
/*  320 */       if (n.fRightChild.fNullable) {
/*  321 */         n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
/*      */       }
/*      */     }
/*  324 */     else if ((n.fType == 10) || (n.fType == 12) || (n.fType == 11))
/*      */     {
/*      */ 
/*  327 */       n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void calcFollowPos(RBBINode n)
/*      */   {
/*  339 */     if ((n == null) || (n.fType == 3) || (n.fType == 6))
/*      */     {
/*      */ 
/*  342 */       return;
/*      */     }
/*      */     
/*  345 */     calcFollowPos(n.fLeftChild);
/*  346 */     calcFollowPos(n.fRightChild);
/*      */     
/*      */ 
/*  349 */     if (n.fType == 8) {
/*  350 */       for (RBBINode i : n.fLeftChild.fLastPosSet) {
/*  351 */         i.fFollowPos.addAll(n.fRightChild.fFirstPosSet);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  356 */     if ((n.fType == 10) || (n.fType == 11))
/*      */     {
/*  358 */       for (RBBINode i : n.fLastPosSet) {
/*  359 */         i.fFollowPos.addAll(n.fFirstPosSet);
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
/*      */   void calcChainedFollowPos(RBBINode tree)
/*      */   {
/*  373 */     List<RBBINode> endMarkerNodes = new ArrayList();
/*  374 */     List<RBBINode> leafNodes = new ArrayList();
/*      */     
/*      */ 
/*  377 */     tree.findNodes(endMarkerNodes, 6);
/*      */     
/*      */ 
/*  380 */     tree.findNodes(leafNodes, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  385 */     RBBINode userRuleRoot = tree;
/*  386 */     if (this.fRB.fSetBuilder.sawBOF()) {
/*  387 */       userRuleRoot = tree.fLeftChild.fRightChild;
/*      */     }
/*  389 */     Assert.assrt(userRuleRoot != null);
/*  390 */     Set<RBBINode> matchStartNodes = userRuleRoot.fFirstPosSet;
/*      */     
/*      */ 
/*      */ 
/*  394 */     for (RBBINode tNode : leafNodes) {
/*  395 */       endNode = null;
/*      */       
/*      */ 
/*      */ 
/*  399 */       for (RBBINode endMarkerNode : endMarkerNodes) {
/*  400 */         if (tNode.fFollowPos.contains(endMarkerNode)) {
/*  401 */           endNode = tNode;
/*  402 */           break;
/*      */         }
/*      */       }
/*  405 */       if (endNode != null)
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
/*  416 */         if (this.fRB.fLBCMNoChain) {
/*  417 */           int c = this.fRB.fSetBuilder.getFirstChar(endNode.fVal);
/*  418 */           if (c != -1)
/*      */           {
/*  420 */             int cLBProp = UCharacter.getIntPropertyValue(c, 4104);
/*  421 */             if (cLBProp == 9) {
/*      */               continue;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  430 */         for (RBBINode startNode : matchStartNodes) {
/*  431 */           if (startNode.fType == 3)
/*      */           {
/*      */ 
/*      */ 
/*  435 */             if (endNode.fVal == startNode.fVal)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  443 */               endNode.fFollowPos.addAll(startNode.fFollowPos);
/*      */             }
/*      */           }
/*      */         }
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
/*      */     RBBINode endNode;
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
/*      */   void bofFixup()
/*      */   {
/*  472 */     RBBINode bofNode = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fLeftChild;
/*  473 */     Assert.assrt(bofNode.fType == 3);
/*  474 */     Assert.assrt(bofNode.fVal == 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  481 */     Set<RBBINode> matchStartNodes = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fRightChild.fFirstPosSet;
/*  482 */     for (RBBINode startNode : matchStartNodes) {
/*  483 */       if (startNode.fType == 3)
/*      */       {
/*      */ 
/*      */ 
/*  487 */         if (startNode.fVal == bofNode.fVal)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  493 */           bofNode.fFollowPos.addAll(startNode.fFollowPos);
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
/*      */   void buildStateTable()
/*      */   {
/*  510 */     int lastInputSymbol = this.fRB.fSetBuilder.getNumCharCategories() - 1;
/*  511 */     RBBIStateDescriptor failState = new RBBIStateDescriptor(lastInputSymbol);
/*  512 */     this.fDStates.add(failState);
/*      */     
/*      */ 
/*      */ 
/*  516 */     RBBIStateDescriptor initialState = new RBBIStateDescriptor(lastInputSymbol);
/*  517 */     initialState.fPositions.addAll(this.fRB.fTreeRoots[this.fRootIx].fFirstPosSet);
/*  518 */     this.fDStates.add(initialState);
/*      */     
/*      */     for (;;)
/*      */     {
/*  522 */       RBBIStateDescriptor T = null;
/*      */       
/*  524 */       for (int tx = 1; tx < this.fDStates.size(); tx++) {
/*  525 */         RBBIStateDescriptor temp = (RBBIStateDescriptor)this.fDStates.get(tx);
/*  526 */         if (!temp.fMarked) {
/*  527 */           T = temp;
/*  528 */           break;
/*      */         }
/*      */       }
/*  531 */       if (T == null) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  536 */       T.fMarked = true;
/*      */       
/*      */ 
/*      */ 
/*  540 */       for (int a = 1; a <= lastInputSymbol; a++)
/*      */       {
/*      */ 
/*      */ 
/*  544 */         Set<RBBINode> U = null;
/*  545 */         for (RBBINode p : T.fPositions) {
/*  546 */           if ((p.fType == 3) && (p.fVal == a)) {
/*  547 */             if (U == null) {
/*  548 */               U = new HashSet();
/*      */             }
/*  550 */             U.addAll(p.fFollowPos);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  555 */         int ux = 0;
/*  556 */         boolean UinDstates = false;
/*  557 */         if (U != null) {
/*  558 */           Assert.assrt(U.size() > 0);
/*      */           
/*  560 */           for (int ix = 0; ix < this.fDStates.size(); ix++)
/*      */           {
/*  562 */             RBBIStateDescriptor temp2 = (RBBIStateDescriptor)this.fDStates.get(ix);
/*  563 */             if (U.equals(temp2.fPositions)) {
/*  564 */               U = temp2.fPositions;
/*  565 */               ux = ix;
/*  566 */               UinDstates = true;
/*  567 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  572 */           if (!UinDstates)
/*      */           {
/*  574 */             RBBIStateDescriptor newState = new RBBIStateDescriptor(lastInputSymbol);
/*  575 */             newState.fPositions = U;
/*  576 */             this.fDStates.add(newState);
/*  577 */             ux = this.fDStates.size() - 1;
/*      */           }
/*      */           
/*      */ 
/*  581 */           T.fDtran[a] = ux;
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
/*      */   void flagAcceptingStates()
/*      */   {
/*  599 */     List<RBBINode> endMarkerNodes = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  604 */     this.fRB.fTreeRoots[this.fRootIx].findNodes(endMarkerNodes, 6);
/*      */     
/*  606 */     for (int i = 0; i < endMarkerNodes.size(); i++) {
/*  607 */       RBBINode endMarker = (RBBINode)endMarkerNodes.get(i);
/*  608 */       for (int n = 0; n < this.fDStates.size(); n++) {
/*  609 */         RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(n);
/*      */         
/*  611 */         if (sd.fPositions.contains(endMarker))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  616 */           if (sd.fAccepting == 0)
/*      */           {
/*  618 */             sd.fAccepting = endMarker.fVal;
/*  619 */             if (sd.fAccepting == 0) {
/*  620 */               sd.fAccepting = -1;
/*      */             }
/*      */           }
/*  623 */           if ((sd.fAccepting == -1) && (endMarker.fVal != 0))
/*      */           {
/*      */ 
/*      */ 
/*  627 */             sd.fAccepting = endMarker.fVal;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  634 */           if (endMarker.fLookAheadEnd)
/*      */           {
/*      */ 
/*      */ 
/*  638 */             sd.fLookAhead = sd.fAccepting;
/*      */           }
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
/*      */   void flagLookAheadStates()
/*      */   {
/*  652 */     List<RBBINode> lookAheadNodes = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  657 */     this.fRB.fTreeRoots[this.fRootIx].findNodes(lookAheadNodes, 4);
/*  658 */     for (int i = 0; i < lookAheadNodes.size(); i++) {
/*  659 */       RBBINode lookAheadNode = (RBBINode)lookAheadNodes.get(i);
/*      */       
/*  661 */       for (int n = 0; n < this.fDStates.size(); n++) {
/*  662 */         RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(n);
/*  663 */         if (sd.fPositions.contains(lookAheadNode)) {
/*  664 */           sd.fLookAhead = lookAheadNode.fVal;
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
/*      */   void flagTaggedStates()
/*      */   {
/*  679 */     List<RBBINode> tagNodes = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  684 */     this.fRB.fTreeRoots[this.fRootIx].findNodes(tagNodes, 5);
/*  685 */     for (int i = 0; i < tagNodes.size(); i++) {
/*  686 */       RBBINode tagNode = (RBBINode)tagNodes.get(i);
/*      */       
/*  688 */       for (int n = 0; n < this.fDStates.size(); n++) {
/*  689 */         RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(n);
/*  690 */         if (sd.fPositions.contains(tagNode)) {
/*  691 */           sd.fTagVals.add(Integer.valueOf(tagNode.fVal));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void mergeRuleStatusVals()
/*      */   {
/*  738 */     if (this.fRB.fRuleStatusVals.size() == 0) {
/*  739 */       this.fRB.fRuleStatusVals.add(Integer.valueOf(1));
/*  740 */       this.fRB.fRuleStatusVals.add(Integer.valueOf(0));
/*      */       
/*  742 */       SortedSet<Integer> s0 = new TreeSet();
/*  743 */       Integer izero = Integer.valueOf(0);
/*  744 */       this.fRB.fStatusSets.put(s0, izero);
/*  745 */       SortedSet<Integer> s1 = new TreeSet();
/*  746 */       s1.add(izero);
/*  747 */       this.fRB.fStatusSets.put(s0, izero);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  752 */     for (int n = 0; n < this.fDStates.size(); n++) {
/*  753 */       RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(n);
/*  754 */       Set<Integer> statusVals = sd.fTagVals;
/*  755 */       Integer arrayIndexI = (Integer)this.fRB.fStatusSets.get(statusVals);
/*  756 */       if (arrayIndexI == null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  761 */         arrayIndexI = Integer.valueOf(this.fRB.fRuleStatusVals.size());
/*  762 */         this.fRB.fStatusSets.put(statusVals, arrayIndexI);
/*      */         
/*      */ 
/*      */ 
/*  766 */         this.fRB.fRuleStatusVals.add(Integer.valueOf(statusVals.size()));
/*  767 */         this.fRB.fRuleStatusVals.addAll(statusVals);
/*      */       }
/*      */       
/*      */ 
/*  771 */       sd.fTagsIdx = arrayIndexI.intValue();
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
/*      */   void printPosSets(RBBINode n)
/*      */   {
/*  789 */     if (n == null) {
/*  790 */       return;
/*      */     }
/*  792 */     RBBINode.printNode(n);
/*  793 */     System.out.print("         Nullable:  " + n.fNullable);
/*      */     
/*  795 */     System.out.print("         firstpos:  ");
/*  796 */     printSet(n.fFirstPosSet);
/*      */     
/*  798 */     System.out.print("         lastpos:   ");
/*  799 */     printSet(n.fLastPosSet);
/*      */     
/*  801 */     System.out.print("         followpos: ");
/*  802 */     printSet(n.fFollowPos);
/*      */     
/*  804 */     printPosSets(n.fLeftChild);
/*  805 */     printPosSets(n.fRightChild);
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
/*      */   int getTableSize()
/*      */   {
/*  821 */     int size = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  826 */     if (this.fRB.fTreeRoots[this.fRootIx] == null) {
/*  827 */       return 0;
/*      */     }
/*      */     
/*  830 */     size = 16;
/*      */     
/*  832 */     int numRows = this.fDStates.size();
/*  833 */     int numCols = this.fRB.fSetBuilder.getNumCharCategories();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  839 */     int rowSize = 8 + 2 * numCols;
/*  840 */     size += numRows * rowSize;
/*  841 */     while (size % 8 > 0) {
/*  842 */       size++;
/*      */     }
/*      */     
/*  845 */     return size;
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
/*      */   short[] exportTable()
/*      */   {
/*  868 */     if (this.fRB.fTreeRoots[this.fRootIx] == null) {
/*  869 */       return new short[0];
/*      */     }
/*      */     
/*  872 */     Assert.assrt((this.fRB.fSetBuilder.getNumCharCategories() < 32767) && (this.fDStates.size() < 32767));
/*      */     
/*      */ 
/*  875 */     int numStates = this.fDStates.size();
/*      */     
/*      */ 
/*      */ 
/*  879 */     int rowLen = 4 + this.fRB.fSetBuilder.getNumCharCategories();
/*  880 */     int tableSize = getTableSize() / 2;
/*      */     
/*      */ 
/*  883 */     short[] table = new short[tableSize];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  890 */     table[0] = ((short)(numStates >>> 16));
/*  891 */     table[1] = ((short)(numStates & 0xFFFF));
/*      */     
/*      */ 
/*  894 */     table[2] = ((short)(rowLen >>> 16));
/*  895 */     table[3] = ((short)(rowLen & 0xFFFF));
/*      */     
/*      */ 
/*  898 */     int flags = 0;
/*  899 */     if (this.fRB.fLookAheadHardBreak) {
/*  900 */       flags |= 0x1;
/*      */     }
/*  902 */     if (this.fRB.fSetBuilder.sawBOF()) {
/*  903 */       flags |= 0x2;
/*      */     }
/*  905 */     table[4] = ((short)(flags >>> 16));
/*  906 */     table[5] = ((short)(flags & 0xFFFF));
/*      */     
/*  908 */     int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
/*  909 */     for (int state = 0; state < numStates; state++) {
/*  910 */       RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(state);
/*  911 */       int row = 8 + state * rowLen;
/*  912 */       Assert.assrt((32768 < sd.fAccepting) && (sd.fAccepting <= 32767));
/*  913 */       Assert.assrt((32768 < sd.fLookAhead) && (sd.fLookAhead <= 32767));
/*  914 */       table[(row + 0)] = ((short)sd.fAccepting);
/*  915 */       table[(row + 1)] = ((short)sd.fLookAhead);
/*  916 */       table[(row + 2)] = ((short)sd.fTagsIdx);
/*  917 */       for (int col = 0; col < numCharCategories; col++) {
/*  918 */         table[(row + 4 + col)] = ((short)sd.fDtran[col]);
/*      */       }
/*      */     }
/*  921 */     return table;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void printSet(Collection<RBBINode> s)
/*      */   {
/*  933 */     for (RBBINode n : s) {
/*  934 */       RBBINode.printInt(n.fSerialNum, 8);
/*      */     }
/*  936 */     System.out.println();
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
/*      */   void printStates()
/*      */   {
/*  951 */     System.out.print("state |           i n p u t     s y m b o l s \n");
/*  952 */     System.out.print("      | Acc  LA    Tag");
/*  953 */     for (int c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); c++) {
/*  954 */       RBBINode.printInt(c, 3);
/*      */     }
/*  956 */     System.out.print("\n");
/*  957 */     System.out.print("      |---------------");
/*  958 */     for (c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); c++) {
/*  959 */       System.out.print("---");
/*      */     }
/*  961 */     System.out.print("\n");
/*      */     
/*  963 */     for (int n = 0; n < this.fDStates.size(); n++) {
/*  964 */       RBBIStateDescriptor sd = (RBBIStateDescriptor)this.fDStates.get(n);
/*  965 */       RBBINode.printInt(n, 5);
/*  966 */       System.out.print(" | ");
/*      */       
/*  968 */       RBBINode.printInt(sd.fAccepting, 3);
/*  969 */       RBBINode.printInt(sd.fLookAhead, 4);
/*  970 */       RBBINode.printInt(sd.fTagsIdx, 6);
/*  971 */       System.out.print(" ");
/*  972 */       for (c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); c++) {
/*  973 */         RBBINode.printInt(sd.fDtran[c], 3);
/*      */       }
/*  975 */       System.out.print("\n");
/*      */     }
/*  977 */     System.out.print("\n\n");
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
/*      */   void printRuleStatusTable()
/*      */   {
/*  990 */     int thisRecord = 0;
/*  991 */     int nextRecord = 0;
/*      */     
/*  993 */     List<Integer> tbl = this.fRB.fRuleStatusVals;
/*      */     
/*  995 */     System.out.print("index |  tags \n");
/*  996 */     System.out.print("-------------------\n");
/*      */     
/*  998 */     while (nextRecord < tbl.size()) {
/*  999 */       thisRecord = nextRecord;
/* 1000 */       nextRecord = thisRecord + ((Integer)tbl.get(thisRecord)).intValue() + 1;
/* 1001 */       RBBINode.printInt(thisRecord, 7);
/* 1002 */       for (int i = thisRecord + 1; i < nextRecord; i++) {
/* 1003 */         int val = ((Integer)tbl.get(i)).intValue();
/* 1004 */         RBBINode.printInt(val, 7);
/*      */       }
/* 1006 */       System.out.print("\n");
/*      */     }
/* 1008 */     System.out.print("\n\n");
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBITableBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */