/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class TextTrieMap<V>
/*     */ {
/*  22 */   private TextTrieMap<V>.Node _root = new Node(null);
/*     */   
/*     */ 
/*     */   boolean _ignoreCase;
/*     */   
/*     */ 
/*     */ 
/*     */   public TextTrieMap(boolean ignoreCase)
/*     */   {
/*  31 */     this._ignoreCase = ignoreCase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextTrieMap<V> put(CharSequence text, V val)
/*     */   {
/*  41 */     CharIterator chitr = new CharIterator(text, 0, this._ignoreCase);
/*  42 */     this._root.add(chitr, val);
/*  43 */     return this;
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
/*     */   public Iterator<V> get(String text)
/*     */   {
/*  56 */     return get(text, 0);
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
/*     */   public Iterator<V> get(String text, int start)
/*     */   {
/*  71 */     return get(text, start, null);
/*     */   }
/*     */   
/*     */   public Iterator<V> get(String text, int start, int[] matchLen) {
/*  75 */     LongestMatchHandler<V> handler = new LongestMatchHandler(null);
/*  76 */     find(text, start, handler);
/*  77 */     if ((matchLen != null) && (matchLen.length > 0)) {
/*  78 */       matchLen[0] = handler.getMatchLength();
/*     */     }
/*  80 */     return handler.getMatches();
/*     */   }
/*     */   
/*     */   public void find(String text, ResultHandler<V> handler) {
/*  84 */     find(text, 0, handler);
/*     */   }
/*     */   
/*     */   public void find(String text, int offset, ResultHandler<V> handler) {
/*  88 */     CharIterator chitr = new CharIterator(text, offset, this._ignoreCase);
/*  89 */     find(this._root, chitr, handler);
/*     */   }
/*     */   
/*     */   private synchronized void find(TextTrieMap<V>.Node node, CharIterator chitr, ResultHandler<V> handler) {
/*  93 */     Iterator<V> values = node.values();
/*  94 */     if ((values != null) && 
/*  95 */       (!handler.handlePrefixMatch(chitr.processedLength(), values))) {
/*  96 */       return;
/*     */     }
/*     */     
/*     */ 
/* 100 */     TextTrieMap<V>.Node nextMatch = node.findMatch(chitr);
/* 101 */     if (nextMatch != null) {
/* 102 */       find(nextMatch, chitr, handler);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CharIterator implements Iterator<Character>
/*     */   {
/*     */     private boolean _ignoreCase;
/*     */     private CharSequence _text;
/*     */     private int _nextIdx;
/*     */     private int _startIdx;
/*     */     private Character _remainingChar;
/*     */     
/*     */     CharIterator(CharSequence text, int offset, boolean ignoreCase) {
/* 115 */       this._text = text;
/* 116 */       this._nextIdx = (this._startIdx = offset);
/* 117 */       this._ignoreCase = ignoreCase;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 124 */       if ((this._nextIdx == this._text.length()) && (this._remainingChar == null)) {
/* 125 */         return false;
/*     */       }
/* 127 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Character next()
/*     */     {
/* 134 */       if ((this._nextIdx == this._text.length()) && (this._remainingChar == null)) {
/* 135 */         return null;
/*     */       }
/*     */       Character next;
/* 138 */       if (this._remainingChar != null) {
/* 139 */         Character next = this._remainingChar;
/* 140 */         this._remainingChar = null;
/*     */       }
/* 142 */       else if (this._ignoreCase) {
/* 143 */         int cp = UCharacter.foldCase(Character.codePointAt(this._text, this._nextIdx), true);
/* 144 */         this._nextIdx += Character.charCount(cp);
/*     */         
/* 146 */         char[] chars = Character.toChars(cp);
/* 147 */         Character next = Character.valueOf(chars[0]);
/* 148 */         if (chars.length == 2) {
/* 149 */           this._remainingChar = Character.valueOf(chars[1]);
/*     */         }
/*     */       } else {
/* 152 */         next = Character.valueOf(this._text.charAt(this._nextIdx));
/* 153 */         this._nextIdx += 1;
/*     */       }
/*     */       
/* 156 */       return next;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void remove()
/*     */     {
/* 163 */       throw new UnsupportedOperationException("remove() not supproted");
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 167 */       return this._nextIdx;
/*     */     }
/*     */     
/*     */     public int processedLength() {
/* 171 */       if (this._remainingChar != null) {
/* 172 */         throw new IllegalStateException("In the middle of surrogate pair");
/*     */       }
/* 174 */       return this._nextIdx - this._startIdx;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract interface ResultHandler<V>
/*     */   {
/*     */     public abstract boolean handlePrefixMatch(int paramInt, Iterator<V> paramIterator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LongestMatchHandler<V>
/*     */     implements TextTrieMap.ResultHandler<V>
/*     */   {
/* 194 */     private Iterator<V> matches = null;
/* 195 */     private int length = 0;
/*     */     
/*     */     public boolean handlePrefixMatch(int matchLength, Iterator<V> values) {
/* 198 */       if (matchLength > this.length) {
/* 199 */         this.length = matchLength;
/* 200 */         this.matches = values;
/*     */       }
/* 202 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<V> getMatches() {
/* 206 */       return this.matches;
/*     */     }
/*     */     
/*     */     public int getMatchLength() {
/* 210 */       return this.length;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class Node
/*     */   {
/*     */     private char[] _text;
/*     */     
/*     */     private List<V> _values;
/*     */     private List<TextTrieMap<V>.Node> _children;
/*     */     
/*     */     private Node() {}
/*     */     
/*     */     private Node(List<V> text, List<TextTrieMap<V>.Node> values)
/*     */     {
/* 226 */       this._text = text;
/* 227 */       this._values = values;
/* 228 */       this._children = children;
/*     */     }
/*     */     
/*     */     public Iterator<V> values() {
/* 232 */       if (this._values == null) {
/* 233 */         return null;
/*     */       }
/* 235 */       return this._values.iterator();
/*     */     }
/*     */     
/*     */     public void add(TextTrieMap.CharIterator chitr, V value) {
/* 239 */       StringBuilder buf = new StringBuilder();
/* 240 */       while (chitr.hasNext()) {
/* 241 */         buf.append(chitr.next());
/*     */       }
/* 243 */       add(TextTrieMap.toCharArray(buf), 0, value);
/*     */     }
/*     */     
/*     */     public TextTrieMap<V>.Node findMatch(TextTrieMap.CharIterator chitr) {
/* 247 */       if (this._children == null) {
/* 248 */         return null;
/*     */       }
/* 250 */       if (!chitr.hasNext()) {
/* 251 */         return null;
/*     */       }
/* 253 */       TextTrieMap<V>.Node match = null;
/* 254 */       Character ch = chitr.next();
/* 255 */       for (TextTrieMap<V>.Node child : this._children) {
/* 256 */         if (ch.charValue() < child._text[0]) {
/*     */           break;
/*     */         }
/* 259 */         if (ch.charValue() == child._text[0]) {
/* 260 */           if (!child.matchFollowing(chitr)) break;
/* 261 */           match = child; break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 266 */       return match;
/*     */     }
/*     */     
/*     */     private void add(char[] text, int offset, V value) {
/* 270 */       if (text.length == offset) {
/* 271 */         this._values = addValue(this._values, value);
/* 272 */         return;
/*     */       }
/*     */       
/* 275 */       if (this._children == null) {
/* 276 */         this._children = new LinkedList();
/* 277 */         TextTrieMap<V>.Node child = new Node(TextTrieMap.this, TextTrieMap.subArray(text, offset), addValue(null, value), null);
/* 278 */         this._children.add(child);
/* 279 */         return;
/*     */       }
/*     */       
/*     */ 
/* 283 */       ListIterator<TextTrieMap<V>.Node> litr = this._children.listIterator();
/* 284 */       while (litr.hasNext()) {
/* 285 */         TextTrieMap<V>.Node next = (Node)litr.next();
/* 286 */         if (text[offset] < next._text[0]) {
/* 287 */           litr.previous();
/* 288 */           break;
/*     */         }
/* 290 */         if (text[offset] == next._text[0]) {
/* 291 */           int matchLen = next.lenMatches(text, offset);
/* 292 */           if (matchLen == next._text.length)
/*     */           {
/* 294 */             next.add(text, offset + matchLen, value);
/*     */           }
/*     */           else {
/* 297 */             next.split(matchLen);
/* 298 */             next.add(text, offset + matchLen, value);
/*     */           }
/* 300 */           return;
/*     */         }
/*     */       }
/*     */       
/* 304 */       litr.add(new Node(TextTrieMap.this, TextTrieMap.subArray(text, offset), addValue(null, value), null));
/*     */     }
/*     */     
/*     */     private boolean matchFollowing(TextTrieMap.CharIterator chitr) {
/* 308 */       boolean matched = true;
/* 309 */       int idx = 1;
/* 310 */       while (idx < this._text.length) {
/* 311 */         if (!chitr.hasNext()) {
/* 312 */           matched = false;
/* 313 */           break;
/*     */         }
/* 315 */         Character ch = chitr.next();
/* 316 */         if (ch.charValue() != this._text[idx]) {
/* 317 */           matched = false;
/* 318 */           break;
/*     */         }
/* 320 */         idx++;
/*     */       }
/* 322 */       return matched;
/*     */     }
/*     */     
/*     */     private int lenMatches(char[] text, int offset) {
/* 326 */       int textLen = text.length - offset;
/* 327 */       int limit = this._text.length < textLen ? this._text.length : textLen;
/* 328 */       int len = 0;
/* 329 */       while ((len < limit) && 
/* 330 */         (this._text[len] == text[(offset + len)]))
/*     */       {
/*     */ 
/* 333 */         len++;
/*     */       }
/* 335 */       return len;
/*     */     }
/*     */     
/*     */     private void split(int offset)
/*     */     {
/* 340 */       char[] childText = TextTrieMap.subArray(this._text, offset);
/* 341 */       this._text = TextTrieMap.subArray(this._text, 0, offset);
/*     */       
/*     */ 
/* 344 */       TextTrieMap<V>.Node child = new Node(TextTrieMap.this, childText, this._values, this._children);
/* 345 */       this._values = null;
/*     */       
/* 347 */       this._children = new LinkedList();
/* 348 */       this._children.add(child);
/*     */     }
/*     */     
/*     */     private List<V> addValue(List<V> list, V value) {
/* 352 */       if (list == null) {
/* 353 */         list = new LinkedList();
/*     */       }
/* 355 */       list.add(value);
/* 356 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   private static char[] toCharArray(CharSequence text) {
/* 361 */     char[] array = new char[text.length()];
/* 362 */     for (int i = 0; i < array.length; i++) {
/* 363 */       array[i] = text.charAt(i);
/*     */     }
/* 365 */     return array;
/*     */   }
/*     */   
/*     */   private static char[] subArray(char[] array, int start) {
/* 369 */     if (start == 0) {
/* 370 */       return array;
/*     */     }
/* 372 */     char[] sub = new char[array.length - start];
/* 373 */     System.arraycopy(array, start, sub, 0, sub.length);
/* 374 */     return sub;
/*     */   }
/*     */   
/*     */   private static char[] subArray(char[] array, int start, int limit) {
/* 378 */     if ((start == 0) && (limit == array.length)) {
/* 379 */       return array;
/*     */     }
/* 381 */     char[] sub = new char[limit - start];
/* 382 */     System.arraycopy(array, start, sub, 0, limit - start);
/* 383 */     return sub;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TextTrieMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */