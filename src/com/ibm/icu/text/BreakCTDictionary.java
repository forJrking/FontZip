/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUBinary;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BreakCTDictionary
/*     */ {
/*     */   private CompactTrieHeader fData;
/*     */   private CompactTrieNodes[] nodes;
/*     */   
/*     */   static class CompactTrieHeader
/*     */   {
/*     */     int size;
/*     */     int magic;
/*     */     int nodeCount;
/*     */     int root;
/*     */     int[] offset;
/*     */     
/*     */     CompactTrieHeader()
/*     */     {
/*  35 */       this.size = 0;
/*  36 */       this.magic = 0;
/*  37 */       this.nodeCount = 0;
/*  38 */       this.root = 0;
/*  39 */       this.offset = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class CompactTrieNodeFlags
/*     */   {
/*     */     static final int kVerticalNode = 4096;
/*     */     
/*     */     static final int kParentEndsWord = 8192;
/*     */     
/*     */     static final int kReservedFlag1 = 16384;
/*     */     
/*     */     static final int kReservedFlag2 = 32768;
/*     */     
/*     */     static final int kCountMask = 4095;
/*     */     
/*     */     static final int kFlagMask = 61440;
/*     */   }
/*     */   
/*     */ 
/*     */   static class CompactTrieHorizontalNode
/*     */   {
/*     */     char ch;
/*     */     
/*     */     int equal;
/*     */     
/*     */     CompactTrieHorizontalNode(char newCh, int newEqual)
/*     */     {
/*  68 */       this.ch = newCh;
/*  69 */       this.equal = newEqual;
/*     */     }
/*     */   }
/*     */   
/*     */   static class CompactTrieVerticalNode
/*     */   {
/*     */     int equal;
/*     */     char[] chars;
/*     */     
/*     */     CompactTrieVerticalNode() {
/*  79 */       this.equal = 0;
/*  80 */       this.chars = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private CompactTrieNodes getCompactTrieNode(int node) {
/*  85 */     return this.nodes[node];
/*     */   }
/*     */   
/*     */ 
/*     */   static class CompactTrieNodes
/*     */   {
/*     */     short flagscount;
/*     */     BreakCTDictionary.CompactTrieHorizontalNode[] hnode;
/*     */     BreakCTDictionary.CompactTrieVerticalNode vnode;
/*     */     
/*     */     CompactTrieNodes()
/*     */     {
/*  97 */       this.flagscount = 0;
/*  98 */       this.hnode = null;
/*  99 */       this.vnode = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public BreakCTDictionary(InputStream is)
/*     */     throws IOException
/*     */   {
/* 107 */     ICUBinary.readHeader(is, DATA_FORMAT_ID, null);
/*     */     
/* 109 */     DataInputStream in = new DataInputStream(is);
/*     */     
/* 111 */     this.fData = new CompactTrieHeader();
/* 112 */     this.fData.size = in.readInt();
/* 113 */     this.fData.magic = in.readInt();
/* 114 */     this.fData.nodeCount = in.readShort();
/* 115 */     this.fData.root = in.readShort();
/*     */     
/* 117 */     loadBreakCTDictionary(in);
/*     */   }
/*     */   
/*     */   private void loadBreakCTDictionary(DataInputStream in)
/*     */     throws IOException
/*     */   {
/* 123 */     for (int i = 0; i < this.fData.nodeCount; i++) {
/* 124 */       in.readInt();
/*     */     }
/*     */     
/*     */ 
/* 128 */     this.nodes = new CompactTrieNodes[this.fData.nodeCount];
/* 129 */     this.nodes[0] = new CompactTrieNodes();
/*     */     
/*     */ 
/* 132 */     for (int j = 1; j < this.fData.nodeCount; j++) {
/* 133 */       this.nodes[j] = new CompactTrieNodes();
/* 134 */       this.nodes[j].flagscount = in.readShort();
/*     */       
/* 136 */       int count = this.nodes[j].flagscount & 0xFFF;
/*     */       
/* 138 */       if (count != 0) {
/* 139 */         boolean isVerticalNode = (this.nodes[j].flagscount & 0x1000) != 0;
/*     */         
/*     */ 
/* 142 */         if (isVerticalNode) {
/* 143 */           this.nodes[j].vnode = new CompactTrieVerticalNode();
/* 144 */           this.nodes[j].vnode.equal = in.readShort();
/*     */           
/* 146 */           this.nodes[j].vnode.chars = new char[count];
/* 147 */           for (int l = 0; l < count; l++) {
/* 148 */             this.nodes[j].vnode.chars[l] = in.readChar();
/*     */           }
/*     */         } else {
/* 151 */           this.nodes[j].hnode = new CompactTrieHorizontalNode[count];
/* 152 */           for (int n = 0; n < count; n++) {
/* 153 */             this.nodes[j].hnode[n] = new CompactTrieHorizontalNode(in.readChar(), in.readShort());
/*     */           }
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
/*     */   public int matches(CharacterIterator text, int maxLength, int[] lengths, int[] count, int limit)
/*     */   {
/* 175 */     CompactTrieNodes node = getCompactTrieNode(this.fData.root);
/* 176 */     int mycount = 0;
/*     */     
/* 178 */     char uc = text.current();
/* 179 */     int i = 0;
/* 180 */     boolean exitFlag = false;
/*     */     
/* 182 */     while (node != null)
/*     */     {
/* 184 */       if ((limit > 0) && ((node.flagscount & 0x2000) != 0))
/*     */       {
/* 186 */         lengths[(mycount++)] = i;
/* 187 */         limit--;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */       if (i >= maxLength) {
/*     */         break;
/*     */       }
/*     */       
/* 198 */       int nodeCount = node.flagscount & 0xFFF;
/* 199 */       if (nodeCount == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 203 */       if ((node.flagscount & 0x1000) != 0)
/*     */       {
/* 205 */         CompactTrieVerticalNode vnode = node.vnode;
/* 206 */         for (int j = 0; (j < nodeCount) && (i < maxLength); j++) {
/* 207 */           if (uc != vnode.chars[j])
/*     */           {
/* 209 */             exitFlag = true;
/* 210 */             break;
/*     */           }
/* 212 */           text.next();
/* 213 */           uc = text.current();
/* 214 */           i++;
/*     */         }
/* 216 */         if (exitFlag) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 222 */         node = getCompactTrieNode(vnode.equal);
/*     */       }
/*     */       else {
/* 225 */         CompactTrieHorizontalNode[] hnode = node.hnode;
/* 226 */         int low = 0;
/* 227 */         int high = nodeCount - 1;
/*     */         
/* 229 */         node = null;
/* 230 */         while (high >= low) {
/* 231 */           int middle = (high + low) / 2;
/* 232 */           if (uc == hnode[middle].ch)
/*     */           {
/* 234 */             node = getCompactTrieNode(hnode[middle].equal);
/* 235 */             text.next();
/* 236 */             uc = text.current();
/* 237 */             i++;
/* 238 */             break; }
/* 239 */           if (uc < hnode[middle].ch) {
/* 240 */             high = middle - 1;
/*     */           } else {
/* 242 */             low = middle + 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 248 */     count[0] = mycount;
/* 249 */     return i;
/*     */   }
/*     */   
/*     */ 
/* 253 */   private static final byte[] DATA_FORMAT_ID = { 84, 114, 68, 99 };
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BreakCTDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */