/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public abstract class StringTrieBuilder
/*     */ {
/*     */   public static enum Option
/*     */   {
/*  36 */     FAST, 
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
/*  48 */     SMALL;
/*     */     
/*     */ 
/*     */ 
/*     */     private Option() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void addImpl(CharSequence s, int value)
/*     */   {
/*  62 */     if (this.state != State.ADDING)
/*     */     {
/*  64 */       throw new IllegalStateException("Cannot add (string, value) pairs after build().");
/*     */     }
/*  66 */     if (s.length() > 65535)
/*     */     {
/*  68 */       throw new IndexOutOfBoundsException("The maximum string length is 0xffff.");
/*     */     }
/*  70 */     if (this.root == null) {
/*  71 */       this.root = createSuffixNode(s, 0, value);
/*     */     } else {
/*  73 */       this.root = this.root.add(this, s, 0, value);
/*     */     }
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected final void buildImpl(Option buildOption)
/*     */   {
/*  82 */     switch (this.state) {
/*     */     case ADDING: 
/*  84 */       if (this.root == null) {
/*  85 */         throw new IndexOutOfBoundsException("No (string, value) pairs were added.");
/*     */       }
/*  87 */       if (buildOption == Option.FAST) {
/*  88 */         this.state = State.BUILDING_FAST;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  99 */         this.state = State.BUILDING_SMALL;
/*     */       }
/* 101 */       break;
/*     */     
/*     */     case BUILDING_FAST: 
/*     */     case BUILDING_SMALL: 
/* 105 */       throw new IllegalStateException("Builder failed and must be clear()ed.");
/*     */     case BUILT: 
/* 107 */       return;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */     this.root = this.root.register(this);
/* 116 */     this.root.markRightEdgesFirst(-1);
/* 117 */     this.root.write(this);
/* 118 */     this.state = State.BUILT;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void clearImpl()
/*     */   {
/* 126 */     this.strings.setLength(0);
/* 127 */     this.nodes.clear();
/* 128 */     this.root = null;
/* 129 */     this.state = State.ADDING;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Node registerNode(Node newNode)
/*     */   {
/* 140 */     if (this.state == State.BUILDING_FAST) {
/* 141 */       return newNode;
/*     */     }
/*     */     
/* 144 */     Node oldNode = (Node)this.nodes.get(newNode);
/* 145 */     if (oldNode != null) {
/* 146 */       return oldNode;
/*     */     }
/*     */     
/*     */ 
/* 150 */     oldNode = (Node)this.nodes.put(newNode, newNode);
/* 151 */     assert (oldNode == null);
/* 152 */     return newNode;
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
/*     */   private final ValueNode registerFinalValue(int value)
/*     */   {
/* 165 */     this.lookupFinalValueNode.setFinalValue(value);
/* 166 */     Node oldNode = (Node)this.nodes.get(this.lookupFinalValueNode);
/* 167 */     if (oldNode != null) {
/* 168 */       return (ValueNode)oldNode;
/*     */     }
/* 170 */     ValueNode newNode = new ValueNode(value);
/*     */     
/*     */ 
/* 173 */     oldNode = (Node)this.nodes.put(newNode, newNode);
/* 174 */     assert (oldNode == null);
/* 175 */     return newNode;
/*     */   }
/*     */   
/*     */   private static abstract class Node { protected int offset;
/*     */     
/* 180 */     public Node() { this.offset = 0; }
/*     */     
/*     */ 
/*     */     public abstract int hashCode();
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 188 */       return (this == other) || (getClass() == other.getClass());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Node add(StringTrieBuilder builder, CharSequence s, int start, int sValue)
/*     */     {
/* 197 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Node register(StringTrieBuilder builder)
/*     */     {
/* 207 */       return this;
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
/*     */     public int markRightEdgesFirst(int edgeNumber)
/*     */     {
/* 236 */       if (this.offset == 0) {
/* 237 */         this.offset = edgeNumber;
/*     */       }
/* 239 */       return edgeNumber;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract void write(StringTrieBuilder paramStringTrieBuilder);
/*     */     
/*     */ 
/*     */ 
/*     */     public final void writeUnlessInsideRightEdge(int firstRight, int lastRight, StringTrieBuilder builder)
/*     */     {
/* 251 */       if ((this.offset < 0) && ((this.offset < lastRight) || (firstRight < this.offset)))
/* 252 */         write(builder);
/*     */     }
/*     */     
/* 255 */     public final int getOffset() { return this.offset; }
/*     */   }
/*     */   
/*     */   private static class ValueNode extends StringTrieBuilder.Node {
/*     */     protected boolean hasValue;
/*     */     protected int value;
/*     */     
/*     */     public ValueNode() {}
/*     */     
/*     */     public ValueNode(int v) {
/* 265 */       this.hasValue = true;
/* 266 */       this.value = v;
/*     */     }
/*     */     
/* 269 */     public final void setValue(int v) { assert (!this.hasValue);
/* 270 */       this.hasValue = true;
/* 271 */       this.value = v;
/*     */     }
/*     */     
/* 274 */     private void setFinalValue(int v) { this.hasValue = true;
/* 275 */       this.value = v;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 279 */       int hash = 1118481;
/* 280 */       if (this.hasValue) {
/* 281 */         hash = hash * 37 + this.value;
/*     */       }
/* 283 */       return hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 287 */       if (this == other) {
/* 288 */         return true;
/*     */       }
/* 290 */       if (!super.equals(other)) {
/* 291 */         return false;
/*     */       }
/* 293 */       ValueNode o = (ValueNode)other;
/* 294 */       return (this.hasValue == o.hasValue) && ((!this.hasValue) || (this.value == o.value));
/*     */     }
/*     */     
/*     */     public StringTrieBuilder.Node add(StringTrieBuilder builder, CharSequence s, int start, int sValue) {
/* 298 */       if (start == s.length()) {
/* 299 */         throw new IllegalArgumentException("Duplicate string.");
/*     */       }
/*     */       
/* 302 */       ValueNode node = builder.createSuffixNode(s, start, sValue);
/* 303 */       node.setValue(this.value);
/* 304 */       return node;
/*     */     }
/*     */     
/*     */     public void write(StringTrieBuilder builder) {
/* 308 */       this.offset = builder.writeValueAndFinal(this.value, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class IntermediateValueNode extends StringTrieBuilder.ValueNode
/*     */   {
/*     */     private StringTrieBuilder.Node next;
/*     */     
/*     */     public IntermediateValueNode(int v, StringTrieBuilder.Node nextNode) {
/* 317 */       this.next = nextNode;
/* 318 */       setValue(v);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 322 */       return (82767594 + this.value) * 37 + this.next.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 326 */       if (this == other) {
/* 327 */         return true;
/*     */       }
/* 329 */       if (!super.equals(other)) {
/* 330 */         return false;
/*     */       }
/* 332 */       IntermediateValueNode o = (IntermediateValueNode)other;
/* 333 */       return this.next == o.next;
/*     */     }
/*     */     
/*     */     public int markRightEdgesFirst(int edgeNumber) {
/* 337 */       if (this.offset == 0) {
/* 338 */         this.offset = (edgeNumber = this.next.markRightEdgesFirst(edgeNumber));
/*     */       }
/* 340 */       return edgeNumber;
/*     */     }
/*     */     
/*     */     public void write(StringTrieBuilder builder) {
/* 344 */       this.next.write(builder);
/* 345 */       this.offset = builder.writeValueAndFinal(this.value, false); } }
/*     */   
/*     */   private static final class LinearMatchNode extends StringTrieBuilder.ValueNode { private CharSequence strings;
/*     */     private int stringOffset;
/*     */     private int length;
/*     */     private StringTrieBuilder.Node next;
/*     */     private int hash;
/*     */     
/* 353 */     public LinearMatchNode(CharSequence builderStrings, int sOffset, int len, StringTrieBuilder.Node nextNode) { this.strings = builderStrings;
/* 354 */       this.stringOffset = sOffset;
/* 355 */       this.length = len;
/* 356 */       this.next = nextNode;
/*     */     }
/*     */     
/* 359 */     public int hashCode() { return this.hash; }
/*     */     
/*     */     public boolean equals(Object other) {
/* 362 */       if (this == other) {
/* 363 */         return true;
/*     */       }
/* 365 */       if (!super.equals(other)) {
/* 366 */         return false;
/*     */       }
/* 368 */       LinearMatchNode o = (LinearMatchNode)other;
/* 369 */       if ((this.length != o.length) || (this.next != o.next)) {
/* 370 */         return false;
/*     */       }
/* 372 */       int i = this.stringOffset;int j = o.stringOffset; for (int limit = this.stringOffset + this.length; i < limit; j++) {
/* 373 */         if (this.strings.charAt(i) != this.strings.charAt(j)) {
/* 374 */           return false;
/*     */         }
/* 372 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 377 */       return true;
/*     */     }
/*     */     
/*     */     public StringTrieBuilder.Node add(StringTrieBuilder builder, CharSequence s, int start, int sValue) {
/* 381 */       if (start == s.length()) {
/* 382 */         if (this.hasValue) {
/* 383 */           throw new IllegalArgumentException("Duplicate string.");
/*     */         }
/* 385 */         setValue(sValue);
/* 386 */         return this;
/*     */       }
/*     */       
/* 389 */       int limit = this.stringOffset + this.length;
/* 390 */       for (int i = this.stringOffset; i < limit; start++) {
/* 391 */         if (start == s.length())
/*     */         {
/* 393 */           int prefixLength = i - this.stringOffset;
/* 394 */           LinearMatchNode suffixNode = new LinearMatchNode(this.strings, i, this.length - prefixLength, this.next);
/* 395 */           suffixNode.setValue(sValue);
/* 396 */           this.length = prefixLength;
/* 397 */           this.next = suffixNode;
/* 398 */           return this;
/*     */         }
/* 400 */         char thisChar = this.strings.charAt(i);
/* 401 */         char newChar = s.charAt(start);
/* 402 */         if (thisChar != newChar)
/*     */         {
/* 404 */           StringTrieBuilder.DynamicBranchNode branchNode = new StringTrieBuilder.DynamicBranchNode();
/*     */           StringTrieBuilder.Node result;
/*     */           StringTrieBuilder.Node thisSuffixNode;
/* 407 */           StringTrieBuilder.Node result; if (i == this.stringOffset)
/*     */           {
/* 409 */             if (this.hasValue)
/*     */             {
/* 411 */               branchNode.setValue(this.value);
/* 412 */               this.value = 0;
/* 413 */               this.hasValue = false;
/*     */             }
/* 415 */             this.stringOffset += 1;
/* 416 */             this.length -= 1;
/* 417 */             StringTrieBuilder.Node thisSuffixNode = this.length > 0 ? this : this.next;
/*     */             
/* 419 */             result = branchNode; } else { StringTrieBuilder.Node result;
/* 420 */             if (i == limit - 1)
/*     */             {
/* 422 */               this.length -= 1;
/* 423 */               StringTrieBuilder.Node thisSuffixNode = this.next;
/* 424 */               this.next = branchNode;
/* 425 */               result = this;
/*     */             }
/*     */             else {
/* 428 */               int prefixLength = i - this.stringOffset;
/* 429 */               i++;
/* 430 */               thisSuffixNode = new LinearMatchNode(this.strings, i, this.length - (prefixLength + 1), this.next);
/*     */               
/* 432 */               this.length = prefixLength;
/* 433 */               this.next = branchNode;
/* 434 */               result = this;
/*     */             } }
/* 436 */           StringTrieBuilder.ValueNode newSuffixNode = builder.createSuffixNode(s, start + 1, sValue);
/* 437 */           branchNode.add(thisChar, thisSuffixNode);
/* 438 */           branchNode.add(newChar, newSuffixNode);
/* 439 */           return result;
/*     */         }
/* 390 */         i++;
/*     */       }
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
/* 443 */       this.next = this.next.add(builder, s, start, sValue);
/* 444 */       return this;
/*     */     }
/*     */     
/*     */     public StringTrieBuilder.Node register(StringTrieBuilder builder) {
/* 448 */       this.next = this.next.register(builder);
/*     */       
/* 450 */       int maxLinearMatchLength = builder.getMaxLinearMatchLength();
/* 451 */       while (this.length > maxLinearMatchLength) {
/* 452 */         int nextOffset = this.stringOffset + this.length - maxLinearMatchLength;
/* 453 */         this.length -= maxLinearMatchLength;
/* 454 */         LinearMatchNode suffixNode = new LinearMatchNode(this.strings, nextOffset, maxLinearMatchLength, this.next);
/*     */         
/* 456 */         suffixNode.setHashCode();
/* 457 */         this.next = builder.registerNode(suffixNode); }
/*     */       StringTrieBuilder.Node result;
/*     */       StringTrieBuilder.Node result;
/* 460 */       if ((this.hasValue) && (!builder.matchNodesCanHaveValues())) {
/* 461 */         int intermediateValue = this.value;
/* 462 */         this.value = 0;
/* 463 */         this.hasValue = false;
/* 464 */         setHashCode();
/* 465 */         result = new StringTrieBuilder.IntermediateValueNode(intermediateValue, builder.registerNode(this));
/*     */       } else {
/* 467 */         setHashCode();
/* 468 */         result = this;
/*     */       }
/* 470 */       return builder.registerNode(result);
/*     */     }
/*     */     
/*     */     public int markRightEdgesFirst(int edgeNumber) {
/* 474 */       if (this.offset == 0) {
/* 475 */         this.offset = (edgeNumber = this.next.markRightEdgesFirst(edgeNumber));
/*     */       }
/* 477 */       return edgeNumber;
/*     */     }
/*     */     
/*     */     public void write(StringTrieBuilder builder) {
/* 481 */       this.next.write(builder);
/* 482 */       builder.write(this.stringOffset, this.length);
/* 483 */       this.offset = builder.writeValueAndType(this.hasValue, this.value, builder.getMinLinearMatch() + this.length - 1);
/*     */     }
/*     */     
/*     */     private void setHashCode()
/*     */     {
/* 488 */       this.hash = ((124151391 + this.length) * 37 + this.next.hashCode());
/* 489 */       if (this.hasValue) {
/* 490 */         this.hash = (this.hash * 37 + this.value);
/*     */       }
/* 492 */       int i = this.stringOffset; for (int limit = this.stringOffset + this.length; i < limit; i++) {
/* 493 */         this.hash = (this.hash * 37 + this.strings.charAt(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class DynamicBranchNode
/*     */     extends StringTrieBuilder.ValueNode
/*     */   {
/*     */     public void add(char c, StringTrieBuilder.Node node)
/*     */     {
/* 508 */       int i = find(c);
/* 509 */       this.chars.insert(i, c);
/* 510 */       this.equal.add(i, node);
/*     */     }
/*     */     
/*     */     public StringTrieBuilder.Node add(StringTrieBuilder builder, CharSequence s, int start, int sValue) {
/* 514 */       if (start == s.length()) {
/* 515 */         if (this.hasValue) {
/* 516 */           throw new IllegalArgumentException("Duplicate string.");
/*     */         }
/* 518 */         setValue(sValue);
/* 519 */         return this;
/*     */       }
/*     */       
/* 522 */       char c = s.charAt(start++);
/* 523 */       int i = find(c);
/* 524 */       if ((i < this.chars.length()) && (c == this.chars.charAt(i))) {
/* 525 */         this.equal.set(i, ((StringTrieBuilder.Node)this.equal.get(i)).add(builder, s, start, sValue));
/*     */       } else {
/* 527 */         this.chars.insert(i, c);
/* 528 */         this.equal.add(i, builder.createSuffixNode(s, start, sValue));
/*     */       }
/* 530 */       return this;
/*     */     }
/*     */     
/*     */     public StringTrieBuilder.Node register(StringTrieBuilder builder) {
/* 534 */       StringTrieBuilder.Node subNode = register(builder, 0, this.chars.length());
/* 535 */       StringTrieBuilder.BranchHeadNode head = new StringTrieBuilder.BranchHeadNode(this.chars.length(), subNode);
/* 536 */       StringTrieBuilder.Node result = head;
/* 537 */       if (this.hasValue) {
/* 538 */         if (builder.matchNodesCanHaveValues()) {
/* 539 */           head.setValue(this.value);
/*     */         } else {
/* 541 */           result = new StringTrieBuilder.IntermediateValueNode(this.value, builder.registerNode(head));
/*     */         }
/*     */       }
/* 544 */       return builder.registerNode(result);
/*     */     }
/*     */     
/* 547 */     private StringTrieBuilder.Node register(StringTrieBuilder builder, int start, int limit) { int length = limit - start;
/* 548 */       if (length > builder.getMaxBranchLinearSubNodeLength())
/*     */       {
/* 550 */         int middle = start + length / 2;
/* 551 */         return builder.registerNode(new StringTrieBuilder.SplitBranchNode(this.chars.charAt(middle), register(builder, start, middle), register(builder, middle, limit)));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 557 */       StringTrieBuilder.ListBranchNode listNode = new StringTrieBuilder.ListBranchNode(length);
/*     */       do {
/* 559 */         char c = this.chars.charAt(start);
/* 560 */         StringTrieBuilder.Node node = (StringTrieBuilder.Node)this.equal.get(start);
/* 561 */         if (node.getClass() == StringTrieBuilder.ValueNode.class)
/*     */         {
/* 563 */           listNode.add(c, ((StringTrieBuilder.ValueNode)node).value);
/*     */         } else {
/* 565 */           listNode.add(c, node.register(builder));
/*     */         }
/* 567 */         start++; } while (start < limit);
/* 568 */       return builder.registerNode(listNode);
/*     */     }
/*     */     
/*     */     private int find(char c) {
/* 572 */       int start = 0;
/* 573 */       int limit = this.chars.length();
/* 574 */       while (start < limit) {
/* 575 */         int i = (start + limit) / 2;
/* 576 */         char middleChar = this.chars.charAt(i);
/* 577 */         if (c < middleChar) {
/* 578 */           limit = i;
/* 579 */         } else { if (c == middleChar) {
/* 580 */             return i;
/*     */           }
/* 582 */           start = i + 1;
/*     */         }
/*     */       }
/* 585 */       return start;
/*     */     }
/*     */     
/* 588 */     private StringBuilder chars = new StringBuilder();
/* 589 */     private ArrayList<StringTrieBuilder.Node> equal = new ArrayList();
/*     */   }
/*     */   
/*     */   private static abstract class BranchNode extends StringTrieBuilder.Node { protected int hash;
/*     */     protected int firstEdgeNumber;
/*     */     
/* 595 */     public int hashCode() { return this.hash; }
/*     */   }
/*     */   
/*     */   private static final class ListBranchNode extends StringTrieBuilder.BranchNode { private StringTrieBuilder.Node[] equal;
/*     */     private int length;
/*     */     private int[] values;
/*     */     private char[] units;
/*     */     
/* 603 */     public ListBranchNode(int capacity) { this.hash = (165535188 + capacity);
/* 604 */       this.equal = new StringTrieBuilder.Node[capacity];
/* 605 */       this.values = new int[capacity];
/* 606 */       this.units = new char[capacity];
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 610 */       if (this == other) {
/* 611 */         return true;
/*     */       }
/* 613 */       if (!super.equals(other)) {
/* 614 */         return false;
/*     */       }
/* 616 */       ListBranchNode o = (ListBranchNode)other;
/* 617 */       for (int i = 0; i < this.length; i++) {
/* 618 */         if ((this.units[i] != o.units[i]) || (this.values[i] != o.values[i]) || (this.equal[i] != o.equal[i])) {
/* 619 */           return false;
/*     */         }
/*     */       }
/* 622 */       return true;
/*     */     }
/*     */     
/*     */     public int markRightEdgesFirst(int edgeNumber) {
/* 626 */       if (this.offset == 0) {
/* 627 */         this.firstEdgeNumber = edgeNumber;
/* 628 */         int step = 0;
/* 629 */         int i = this.length;
/*     */         do {
/* 631 */           StringTrieBuilder.Node edge = this.equal[(--i)];
/* 632 */           if (edge != null) {
/* 633 */             edgeNumber = edge.markRightEdgesFirst(edgeNumber - step);
/*     */           }
/*     */           
/* 636 */           step = 1;
/* 637 */         } while (i > 0);
/* 638 */         this.offset = edgeNumber;
/*     */       }
/* 640 */       return edgeNumber;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void write(StringTrieBuilder builder)
/*     */     {
/* 648 */       int unitNumber = this.length - 1;
/* 649 */       StringTrieBuilder.Node rightEdge = this.equal[unitNumber];
/* 650 */       int rightEdgeNumber = rightEdge == null ? this.firstEdgeNumber : rightEdge.getOffset();
/*     */       do {
/* 652 */         unitNumber--;
/* 653 */         if (this.equal[unitNumber] != null) {
/* 654 */           this.equal[unitNumber].writeUnlessInsideRightEdge(this.firstEdgeNumber, rightEdgeNumber, builder);
/*     */         }
/* 656 */       } while (unitNumber > 0);
/*     */       
/*     */ 
/* 659 */       unitNumber = this.length - 1;
/* 660 */       if (rightEdge == null) {
/* 661 */         builder.writeValueAndFinal(this.values[unitNumber], true);
/*     */       } else {
/* 663 */         rightEdge.write(builder);
/*     */       }
/* 665 */       this.offset = builder.write(this.units[unitNumber]);
/*     */       for (;;) {
/* 667 */         unitNumber--; if (unitNumber < 0) break;
/*     */         boolean isFinal;
/*     */         int value;
/* 670 */         boolean isFinal; if (this.equal[unitNumber] == null)
/*     */         {
/* 672 */           int value = this.values[unitNumber];
/* 673 */           isFinal = true;
/*     */         }
/*     */         else {
/* 676 */           assert (this.equal[unitNumber].getOffset() > 0);
/* 677 */           value = this.offset - this.equal[unitNumber].getOffset();
/* 678 */           isFinal = false;
/*     */         }
/* 680 */         builder.writeValueAndFinal(value, isFinal);
/* 681 */         this.offset = builder.write(this.units[unitNumber]);
/*     */       }
/*     */     }
/*     */     
/*     */     public void add(int c, int value) {
/* 686 */       this.units[this.length] = ((char)c);
/* 687 */       this.equal[this.length] = null;
/* 688 */       this.values[this.length] = value;
/* 689 */       this.length += 1;
/* 690 */       this.hash = ((this.hash * 37 + c) * 37 + value);
/*     */     }
/*     */     
/*     */     public void add(int c, StringTrieBuilder.Node node) {
/* 694 */       this.units[this.length] = ((char)c);
/* 695 */       this.equal[this.length] = node;
/* 696 */       this.values[this.length] = 0;
/* 697 */       this.length += 1;
/* 698 */       this.hash = ((this.hash * 37 + c) * 37 + node.hashCode());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class SplitBranchNode
/*     */     extends StringTrieBuilder.BranchNode
/*     */   {
/*     */     private char unit;
/*     */     
/*     */     private StringTrieBuilder.Node lessThan;
/*     */     private StringTrieBuilder.Node greaterOrEqual;
/*     */     
/*     */     public SplitBranchNode(char middleUnit, StringTrieBuilder.Node lessThanNode, StringTrieBuilder.Node greaterOrEqualNode)
/*     */     {
/* 713 */       this.hash = (((206918985 + middleUnit) * 37 + lessThanNode.hashCode()) * 37 + greaterOrEqualNode.hashCode());
/*     */       
/* 715 */       this.unit = middleUnit;
/* 716 */       this.lessThan = lessThanNode;
/* 717 */       this.greaterOrEqual = greaterOrEqualNode;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 721 */       if (this == other) {
/* 722 */         return true;
/*     */       }
/* 724 */       if (!super.equals(other)) {
/* 725 */         return false;
/*     */       }
/* 727 */       SplitBranchNode o = (SplitBranchNode)other;
/* 728 */       return (this.unit == o.unit) && (this.lessThan == o.lessThan) && (this.greaterOrEqual == o.greaterOrEqual);
/*     */     }
/*     */     
/*     */     public int markRightEdgesFirst(int edgeNumber) {
/* 732 */       if (this.offset == 0) {
/* 733 */         this.firstEdgeNumber = edgeNumber;
/* 734 */         edgeNumber = this.greaterOrEqual.markRightEdgesFirst(edgeNumber);
/* 735 */         this.offset = (edgeNumber = this.lessThan.markRightEdgesFirst(edgeNumber - 1));
/*     */       }
/* 737 */       return edgeNumber;
/*     */     }
/*     */     
/*     */     public void write(StringTrieBuilder builder)
/*     */     {
/* 742 */       this.lessThan.writeUnlessInsideRightEdge(this.firstEdgeNumber, this.greaterOrEqual.getOffset(), builder);
/*     */       
/* 744 */       this.greaterOrEqual.write(builder);
/*     */       
/* 746 */       assert (this.lessThan.getOffset() > 0);
/* 747 */       builder.writeDeltaTo(this.lessThan.getOffset());
/* 748 */       this.offset = builder.write(this.unit);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class BranchHeadNode extends StringTrieBuilder.ValueNode
/*     */   {
/*     */     private int length;
/*     */     private StringTrieBuilder.Node next;
/*     */     
/*     */     public BranchHeadNode(int len, StringTrieBuilder.Node subNode)
/*     */     {
/* 759 */       this.length = len;
/* 760 */       this.next = subNode;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 764 */       return (248302782 + this.length) * 37 + this.next.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 768 */       if (this == other) {
/* 769 */         return true;
/*     */       }
/* 771 */       if (!super.equals(other)) {
/* 772 */         return false;
/*     */       }
/* 774 */       BranchHeadNode o = (BranchHeadNode)other;
/* 775 */       return (this.length == o.length) && (this.next == o.next);
/*     */     }
/*     */     
/*     */     public int markRightEdgesFirst(int edgeNumber) {
/* 779 */       if (this.offset == 0) {
/* 780 */         this.offset = (edgeNumber = this.next.markRightEdgesFirst(edgeNumber));
/*     */       }
/* 782 */       return edgeNumber;
/*     */     }
/*     */     
/*     */     public void write(StringTrieBuilder builder) {
/* 786 */       this.next.write(builder);
/* 787 */       if (this.length <= builder.getMinLinearMatch()) {
/* 788 */         this.offset = builder.writeValueAndType(this.hasValue, this.value, this.length - 1);
/*     */       } else {
/* 790 */         builder.write(this.length - 1);
/* 791 */         this.offset = builder.writeValueAndType(this.hasValue, this.value, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ValueNode createSuffixNode(CharSequence s, int start, int sValue)
/*     */   {
/* 800 */     ValueNode node = registerFinalValue(sValue);
/* 801 */     if (start < s.length()) {
/* 802 */       int offset = this.strings.length();
/* 803 */       this.strings.append(s, start, s.length());
/* 804 */       node = new LinearMatchNode(this.strings, offset, s.length() - start, node);
/*     */     }
/* 806 */     return node;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract boolean matchNodesCanHaveValues();
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int getMaxBranchLinearSubNodeLength();
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int getMinLinearMatch();
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int getMaxLinearMatchLength();
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int write(int paramInt);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int write(int paramInt1, int paramInt2);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int writeValueAndFinal(int paramInt, boolean paramBoolean);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int writeValueAndType(boolean paramBoolean, int paramInt1, int paramInt2);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected abstract int writeDeltaTo(int paramInt);
/*     */   
/*     */   private static enum State
/*     */   {
/* 858 */     ADDING,  BUILDING_FAST,  BUILDING_SMALL,  BUILT;
/*     */     private State() {} }
/* 860 */   private State state = State.ADDING;
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/* 867 */   protected StringBuilder strings = new StringBuilder();
/*     */   
/*     */   private Node root;
/*     */   
/* 871 */   private HashMap<Node, Node> nodes = new HashMap();
/* 872 */   private ValueNode lookupFinalValueNode = new ValueNode();
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\StringTrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */