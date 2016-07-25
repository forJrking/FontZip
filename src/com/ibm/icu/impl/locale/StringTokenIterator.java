/*    */ package com.ibm.icu.impl.locale;
/*    */ 
/*    */ 
/*    */ public class StringTokenIterator
/*    */ {
/*    */   private String _text;
/*    */   
/*    */   private String _dlms;
/*    */   
/*    */   private String _token;
/*    */   
/*    */   private int _start;
/*    */   
/*    */   private int _end;
/*    */   private boolean _done;
/*    */   
/*    */   public StringTokenIterator(String text, String dlms)
/*    */   {
/* 19 */     this._text = text;
/* 20 */     this._dlms = dlms;
/* 21 */     setStart(0);
/*    */   }
/*    */   
/*    */   public String first() {
/* 25 */     setStart(0);
/* 26 */     return this._token;
/*    */   }
/*    */   
/*    */   public String current() {
/* 30 */     return this._token;
/*    */   }
/*    */   
/*    */   public int currentStart() {
/* 34 */     return this._start;
/*    */   }
/*    */   
/*    */   public int currentEnd() {
/* 38 */     return this._end;
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 42 */     return this._done;
/*    */   }
/*    */   
/*    */   public String next() {
/* 46 */     if (hasNext()) {
/* 47 */       this._start = (this._end + 1);
/* 48 */       this._end = nextDelimiter(this._start);
/* 49 */       this._token = this._text.substring(this._start, this._end);
/*    */     } else {
/* 51 */       this._start = this._end;
/* 52 */       this._token = null;
/* 53 */       this._done = true;
/*    */     }
/* 55 */     return this._token;
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 59 */     return this._end < this._text.length();
/*    */   }
/*    */   
/*    */   public StringTokenIterator setStart(int offset) {
/* 63 */     if (offset > this._text.length()) {
/* 64 */       throw new IndexOutOfBoundsException();
/*    */     }
/* 66 */     this._start = offset;
/* 67 */     this._end = nextDelimiter(this._start);
/* 68 */     this._token = this._text.substring(this._start, this._end);
/* 69 */     this._done = false;
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public StringTokenIterator setText(String text) {
/* 74 */     this._text = text;
/* 75 */     setStart(0);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   private int nextDelimiter(int start) {
/* 80 */     int idx = start;
/* 81 */     while (idx < this._text.length()) {
/* 82 */       char c = this._text.charAt(idx);
/* 83 */       for (int i = 0; i < this._dlms.length(); i++) {
/* 84 */         if (c == this._dlms.charAt(i)) {
/*    */           return idx;
/*    */         }
/*    */       }
/* 88 */       idx++;
/*    */     }
/* 90 */     return idx;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\StringTokenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */