/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.lang.UCharacter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class UnicodeNameTransliterator
/*    */   extends Transliterator
/*    */ {
/*    */   static final String _ID = "Any-Name";
/*    */   static final String OPEN_DELIM = "\\N{";
/*    */   static final char CLOSE_DELIM = '}';
/*    */   static final int OPEN_DELIM_LEN = 3;
/*    */   
/*    */   static void register()
/*    */   {
/* 25 */     Transliterator.registerFactory("Any-Name", new Transliterator.Factory() {
/*    */       public Transliterator getInstance(String ID) {
/* 27 */         return new UnicodeNameTransliterator(null);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UnicodeNameTransliterator(UnicodeFilter filter)
/*    */   {
/* 36 */     super("Any-Name", filter);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*    */   {
/* 44 */     int cursor = offsets.start;
/* 45 */     int limit = offsets.limit;
/*    */     
/* 47 */     StringBuilder str = new StringBuilder();
/* 48 */     str.append("\\N{");
/*    */     
/*    */ 
/*    */ 
/* 52 */     while (cursor < limit) {
/* 53 */       int c = text.char32At(cursor);
/* 54 */       String name; if ((name = UCharacter.getExtendedName(c)) != null)
/*    */       {
/* 56 */         str.setLength(3);
/* 57 */         str.append(name).append('}');
/*    */         
/* 59 */         int clen = UTF16.getCharCount(c);
/* 60 */         text.replace(cursor, cursor + clen, str.toString());
/* 61 */         int len = str.length();
/* 62 */         cursor += len;
/* 63 */         limit += len - clen;
/*    */       } else {
/* 65 */         cursor++;
/*    */       }
/*    */     }
/*    */     
/* 69 */     offsets.contextLimit += limit - offsets.limit;
/* 70 */     offsets.limit = limit;
/* 71 */     offsets.start = cursor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*    */   {
/* 79 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/* 80 */     if (myFilter.size() > 0) {
/* 81 */       sourceSet.addAll(myFilter);
/* 82 */       targetSet.addAll(48, 57).addAll(65, 90).add(45).add(32).addAll("\\N{").add(125).addAll(97, 122).add(60).add(62).add(40).add(41);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeNameTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */