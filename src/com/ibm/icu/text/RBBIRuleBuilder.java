/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import com.ibm.icu.impl.ICUDebug;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ class RBBIRuleBuilder
/*     */ {
/*     */   String fDebugEnv;
/*     */   String fRules;
/*     */   RBBIRuleScanner fScanner;
/*  36 */   RBBINode[] fTreeRoots = new RBBINode[4];
/*     */   static final int fForwardTree = 0;
/*     */   static final int fReverseTree = 1;
/*     */   static final int fSafeFwdTree = 2;
/*     */   static final int fSafeRevTree = 3;
/*  41 */   int fDefaultTree = 0;
/*     */   
/*     */ 
/*     */   boolean fChainRules;
/*     */   
/*     */ 
/*     */   boolean fLBCMNoChain;
/*     */   
/*     */ 
/*     */   boolean fLookAheadHardBreak;
/*     */   
/*     */   RBBISetBuilder fSetBuilder;
/*     */   
/*     */   List<RBBINode> fUSetNodes;
/*     */   
/*     */   RBBITableBuilder fForwardTables;
/*     */   
/*     */   RBBITableBuilder fReverseTables;
/*     */   
/*     */   RBBITableBuilder fSafeFwdTables;
/*     */   
/*     */   RBBITableBuilder fSafeRevTables;
/*     */   
/*  64 */   Map<Set<Integer>, Integer> fStatusSets = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   List<Integer> fRuleStatusVals;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_ERROR_START = 66048;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_INTERNAL_ERROR = 66049;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_HEX_DIGITS_EXPECTED = 66050;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_SEMICOLON_EXPECTED = 66051;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_RULE_SYNTAX = 66052;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_UNCLOSED_SET = 66053;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_ASSIGN_ERROR = 66054;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_VARIABLE_REDFINITION = 66055;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_MISMATCHED_PAREN = 66056;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_NEW_LINE_IN_QUOTED_STRING = 66057;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_UNDEFINED_VARIABLE = 66058;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_INIT_ERROR = 66059;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_RULE_EMPTY_SET = 66060;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int U_BRK_UNRECOGNIZED_OPTION = 66061;
/*     */   
/*     */ 
/*     */   static final int U_BRK_MALFORMED_RULE_TAG = 66062;
/*     */   
/*     */ 
/*     */   static final int U_BRK_MALFORMED_SET = 66063;
/*     */   
/*     */ 
/*     */   static final int U_BRK_ERROR_LIMIT = 66064;
/*     */   
/*     */ 
/*     */ 
/*     */   RBBIRuleBuilder(String rules)
/*     */   {
/* 139 */     this.fDebugEnv = (ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null);
/*     */     
/* 141 */     this.fRules = rules;
/* 142 */     this.fUSetNodes = new ArrayList();
/* 143 */     this.fRuleStatusVals = new ArrayList();
/* 144 */     this.fScanner = new RBBIRuleScanner(this);
/* 145 */     this.fSetBuilder = new RBBISetBuilder(this);
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
/*     */   static final int align8(int i)
/*     */   {
/* 158 */     return i + 7 & 0xFFFFFFF8;
/*     */   }
/*     */   
/*     */   void flattenData(OutputStream os) throws IOException {
/* 162 */     DataOutputStream dos = new DataOutputStream(os);
/*     */     
/*     */ 
/*     */ 
/* 166 */     String strippedRules = RBBIRuleScanner.stripRules(this.fRules);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */     int headerSize = 96;
/* 174 */     int forwardTableSize = align8(this.fForwardTables.getTableSize());
/* 175 */     int reverseTableSize = align8(this.fReverseTables.getTableSize());
/* 176 */     int safeFwdTableSize = align8(this.fSafeFwdTables.getTableSize());
/* 177 */     int safeRevTableSize = align8(this.fSafeRevTables.getTableSize());
/* 178 */     int trieSize = align8(this.fSetBuilder.getTrieSize());
/* 179 */     int statusTableSize = align8(this.fRuleStatusVals.size() * 4);
/* 180 */     int rulesSize = align8(strippedRules.length() * 2);
/* 181 */     int totalSize = headerSize + forwardTableSize + reverseTableSize + safeFwdTableSize + safeRevTableSize + statusTableSize + trieSize + rulesSize;
/*     */     
/*     */ 
/* 184 */     int outputPos = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */     byte[] ICUDataHeader = new byte[''];
/* 193 */     dos.write(ICUDataHeader);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 198 */     int[] header = new int[24];
/* 199 */     header[0] = 45472;
/* 200 */     header[1] = 50397184;
/* 201 */     header[2] = totalSize;
/* 202 */     header[3] = this.fSetBuilder.getNumCharCategories();
/* 203 */     header[4] = headerSize;
/* 204 */     header[5] = forwardTableSize;
/* 205 */     header[6] = (header[4] + forwardTableSize);
/* 206 */     header[7] = reverseTableSize;
/* 207 */     header[8] = (header[6] + reverseTableSize);
/*     */     
/* 209 */     header[9] = safeFwdTableSize;
/* 210 */     header[10] = (header[8] + safeFwdTableSize);
/*     */     
/* 212 */     header[11] = safeRevTableSize;
/* 213 */     header[12] = (header[10] + safeRevTableSize);
/*     */     
/* 215 */     header[13] = this.fSetBuilder.getTrieSize();
/* 216 */     header[16] = (header[12] + header[13]);
/*     */     
/* 218 */     header[17] = statusTableSize;
/* 219 */     header[14] = (header[16] + statusTableSize);
/*     */     
/* 221 */     header[15] = (strippedRules.length() * 2);
/* 222 */     for (int i = 0; i < header.length; i++) {
/* 223 */       dos.writeInt(header[i]);
/* 224 */       outputPos += 4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 229 */     short[] tableData = this.fForwardTables.exportTable();
/* 230 */     Assert.assrt(outputPos == header[4]);
/* 231 */     for (i = 0; i < tableData.length; i++) {
/* 232 */       dos.writeShort(tableData[i]);
/* 233 */       outputPos += 2;
/*     */     }
/*     */     
/* 236 */     tableData = this.fReverseTables.exportTable();
/* 237 */     Assert.assrt(outputPos == header[6]);
/* 238 */     for (i = 0; i < tableData.length; i++) {
/* 239 */       dos.writeShort(tableData[i]);
/* 240 */       outputPos += 2;
/*     */     }
/*     */     
/* 243 */     Assert.assrt(outputPos == header[8]);
/* 244 */     tableData = this.fSafeFwdTables.exportTable();
/* 245 */     for (i = 0; i < tableData.length; i++) {
/* 246 */       dos.writeShort(tableData[i]);
/* 247 */       outputPos += 2;
/*     */     }
/*     */     
/* 250 */     Assert.assrt(outputPos == header[10]);
/* 251 */     tableData = this.fSafeRevTables.exportTable();
/* 252 */     for (i = 0; i < tableData.length; i++) {
/* 253 */       dos.writeShort(tableData[i]);
/* 254 */       outputPos += 2;
/*     */     }
/*     */     
/*     */ 
/* 258 */     Assert.assrt(outputPos == header[12]);
/* 259 */     this.fSetBuilder.serializeTrie(os);
/* 260 */     outputPos += header[13];
/* 261 */     while (outputPos % 8 != 0) {
/* 262 */       dos.write(0);
/* 263 */       outputPos++;
/*     */     }
/*     */     
/*     */ 
/* 267 */     Assert.assrt(outputPos == header[16]);
/* 268 */     for (Integer val : this.fRuleStatusVals) {
/* 269 */       dos.writeInt(val.intValue());
/* 270 */       outputPos += 4;
/*     */     }
/*     */     
/* 273 */     while (outputPos % 8 != 0) {
/* 274 */       dos.write(0);
/* 275 */       outputPos++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 280 */     Assert.assrt(outputPos == header[14]);
/* 281 */     dos.writeChars(strippedRules);
/* 282 */     outputPos += strippedRules.length() * 2;
/* 283 */     while (outputPos % 8 != 0) {
/* 284 */       dos.write(0);
/* 285 */       outputPos++;
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
/*     */   static void compileRules(String rules, OutputStream os)
/*     */     throws IOException
/*     */   {
/* 301 */     RBBIRuleBuilder builder = new RBBIRuleBuilder(rules);
/* 302 */     builder.fScanner.parse();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */     builder.fSetBuilder.build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 315 */     builder.fForwardTables = new RBBITableBuilder(builder, 0);
/* 316 */     builder.fReverseTables = new RBBITableBuilder(builder, 1);
/* 317 */     builder.fSafeFwdTables = new RBBITableBuilder(builder, 2);
/* 318 */     builder.fSafeRevTables = new RBBITableBuilder(builder, 3);
/* 319 */     builder.fForwardTables.build();
/* 320 */     builder.fReverseTables.build();
/* 321 */     builder.fSafeFwdTables.build();
/* 322 */     builder.fSafeRevTables.build();
/* 323 */     if ((builder.fDebugEnv != null) && (builder.fDebugEnv.indexOf("states") >= 0))
/*     */     {
/* 325 */       builder.fForwardTables.printRuleStatusTable();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 332 */     builder.flattenData(os);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBIRuleBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */