/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.util.CompactByteArray;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
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
/*     */ class BreakDictionary
/*     */ {
/*     */   public static void main(String[] args)
/*     */     throws FileNotFoundException, UnsupportedEncodingException, IOException
/*     */   {
/*  40 */     String filename = args[0];
/*     */     
/*  42 */     BreakDictionary dictionary = new BreakDictionary(new FileInputStream(filename));
/*     */     
/*  44 */     PrintWriter out = null;
/*     */     
/*  46 */     if (args.length >= 2) {
/*  47 */       out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UnicodeLittle"));
/*     */     }
/*     */     
/*  50 */     dictionary.printWordList("", 0, out);
/*     */     
/*  52 */     if (out != null) {
/*  53 */       out.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void printWordList(String partialWord, int state, PrintWriter out)
/*     */     throws IOException
/*     */   {
/*  61 */     if (state == 65535) {
/*  62 */       System.out.println(partialWord);
/*  63 */       if (out != null) {
/*  64 */         out.println(partialWord);
/*     */       }
/*     */     }
/*     */     else {
/*  68 */       for (int i = 0; i < this.numCols; i++) {
/*  69 */         int newState = at(state, i) & 0xFFFF;
/*     */         
/*  71 */         if (newState != 0) {
/*  72 */           char newChar = this.reverseColumnMap[i];
/*  73 */           String newPartialWord = partialWord;
/*     */           
/*  75 */           if (newChar != 0) {
/*  76 */             newPartialWord = newPartialWord + newChar;
/*     */           }
/*     */           
/*  79 */           printWordList(newPartialWord, newState, out);
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
/*  90 */   private char[] reverseColumnMap = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private CompactByteArray columnMap = null;
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
/*     */   private int numCols;
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
/* 125 */   private short[] table = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 130 */   private short[] rowIndex = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */   private int[] rowIndexFlags = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   private short[] rowIndexFlagsIndex = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */   private byte[] rowIndexShifts = null;
/*     */   
/*     */ 
/*     */ 
/*     */   BreakDictionary(InputStream dictionaryStream)
/*     */     throws IOException
/*     */   {
/* 161 */     readDictionaryFile(new DataInputStream(dictionaryStream));
/*     */   }
/*     */   
/*     */ 
/*     */   void readDictionaryFile(DataInputStream in)
/*     */     throws IOException
/*     */   {
/* 168 */     in.readInt();
/*     */     
/*     */ 
/*     */ 
/* 172 */     int l = in.readInt();
/* 173 */     char[] temp = new char[l];
/* 174 */     for (int i = 0; i < temp.length; i++)
/* 175 */       temp[i] = ((char)in.readShort());
/* 176 */     l = in.readInt();
/* 177 */     byte[] temp2 = new byte[l];
/* 178 */     for (int i = 0; i < temp2.length; i++)
/* 179 */       temp2[i] = in.readByte();
/* 180 */     this.columnMap = new CompactByteArray(temp, temp2);
/*     */     
/*     */ 
/* 183 */     this.numCols = in.readInt();
/* 184 */     in.readInt();
/*     */     
/*     */ 
/* 187 */     l = in.readInt();
/* 188 */     this.rowIndex = new short[l];
/* 189 */     for (int i = 0; i < this.rowIndex.length; i++) {
/* 190 */       this.rowIndex[i] = in.readShort();
/*     */     }
/*     */     
/* 193 */     l = in.readInt();
/* 194 */     this.rowIndexFlagsIndex = new short[l];
/* 195 */     for (int i = 0; i < this.rowIndexFlagsIndex.length; i++)
/* 196 */       this.rowIndexFlagsIndex[i] = in.readShort();
/* 197 */     l = in.readInt();
/* 198 */     this.rowIndexFlags = new int[l];
/* 199 */     for (int i = 0; i < this.rowIndexFlags.length; i++) {
/* 200 */       this.rowIndexFlags[i] = in.readInt();
/*     */     }
/*     */     
/* 203 */     l = in.readInt();
/* 204 */     this.rowIndexShifts = new byte[l];
/* 205 */     for (int i = 0; i < this.rowIndexShifts.length; i++) {
/* 206 */       this.rowIndexShifts[i] = in.readByte();
/*     */     }
/*     */     
/* 209 */     l = in.readInt();
/* 210 */     this.table = new short[l];
/* 211 */     for (int i = 0; i < this.table.length; i++) {
/* 212 */       this.table[i] = in.readShort();
/*     */     }
/*     */     
/* 215 */     this.reverseColumnMap = new char[this.numCols];
/* 216 */     for (char c = '\000'; c < 65535; c = (char)(c + '\001')) {
/* 217 */       int col = this.columnMap.elementAt(c);
/* 218 */       if (col != 0) {
/* 219 */         this.reverseColumnMap[col] = c;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 224 */     in.close();
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
/*     */   final short at(int row, char ch)
/*     */   {
/* 239 */     int col = this.columnMap.elementAt(ch);
/* 240 */     return at(row, col);
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
/*     */   final short at(int row, int col)
/*     */   {
/* 255 */     if (cellIsPopulated(row, col))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */       return internalAt(this.rowIndex[row], col + this.rowIndexShifts[row]);
/*     */     }
/*     */     
/* 265 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean cellIsPopulated(int row, int col)
/*     */   {
/* 277 */     if (this.rowIndexFlagsIndex[row] < 0) {
/* 278 */       return col == -this.rowIndexFlagsIndex[row];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 288 */     int flags = this.rowIndexFlags[(this.rowIndexFlagsIndex[row] + (col >> 5))];
/* 289 */     return (flags & 1 << (col & 0x1F)) != 0;
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
/*     */   private final short internalAt(int row, int col)
/*     */   {
/* 303 */     return this.table[(row * this.numCols + col)];
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BreakDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */