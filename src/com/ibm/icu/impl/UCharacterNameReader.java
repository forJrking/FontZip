/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
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
/*     */ final class UCharacterNameReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/*     */   private DataInputStream m_dataInputStream_;
/*     */   private static final int GROUP_INFO_SIZE_ = 3;
/*     */   private int m_tokenstringindex_;
/*     */   private int m_groupindex_;
/*     */   private int m_groupstringindex_;
/*     */   private int m_algnamesindex_;
/*     */   private static final int ALG_INFO_SIZE_ = 12;
/*     */   
/*     */   public boolean isDataVersionAcceptable(byte[] version)
/*     */   {
/*  33 */     return version[0] == DATA_FORMAT_VERSION_[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UCharacterNameReader(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/*  46 */     ICUBinary.readHeader(inputStream, DATA_FORMAT_ID_, this);
/*  47 */     this.m_dataInputStream_ = new DataInputStream(inputStream);
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
/*     */   protected void read(UCharacterName data)
/*     */     throws IOException
/*     */   {
/*  62 */     this.m_tokenstringindex_ = this.m_dataInputStream_.readInt();
/*  63 */     this.m_groupindex_ = this.m_dataInputStream_.readInt();
/*  64 */     this.m_groupstringindex_ = this.m_dataInputStream_.readInt();
/*  65 */     this.m_algnamesindex_ = this.m_dataInputStream_.readInt();
/*     */     
/*     */ 
/*  68 */     int count = this.m_dataInputStream_.readChar();
/*  69 */     char[] token = new char[count];
/*  70 */     for (char i = '\000'; i < count; i = (char)(i + '\001')) {
/*  71 */       token[i] = this.m_dataInputStream_.readChar();
/*     */     }
/*  73 */     int size = this.m_groupindex_ - this.m_tokenstringindex_;
/*  74 */     byte[] tokenstr = new byte[size];
/*  75 */     this.m_dataInputStream_.readFully(tokenstr);
/*  76 */     data.setToken(token, tokenstr);
/*     */     
/*     */ 
/*  79 */     count = this.m_dataInputStream_.readChar();
/*  80 */     data.setGroupCountSize(count, 3);
/*  81 */     count *= 3;
/*  82 */     char[] group = new char[count];
/*  83 */     for (int i = 0; i < count; i++) {
/*  84 */       group[i] = this.m_dataInputStream_.readChar();
/*     */     }
/*     */     
/*  87 */     size = this.m_algnamesindex_ - this.m_groupstringindex_;
/*  88 */     byte[] groupstring = new byte[size];
/*  89 */     this.m_dataInputStream_.readFully(groupstring);
/*     */     
/*  91 */     data.setGroup(group, groupstring);
/*     */     
/*  93 */     count = this.m_dataInputStream_.readInt();
/*  94 */     UCharacterName.AlgorithmName[] alg = new UCharacterName.AlgorithmName[count];
/*     */     
/*     */ 
/*  97 */     for (int i = 0; i < count; i++)
/*     */     {
/*  99 */       UCharacterName.AlgorithmName an = readAlg();
/* 100 */       if (an == null) {
/* 101 */         throw new IOException("unames.icu read error: Algorithmic names creation error");
/*     */       }
/* 103 */       alg[i] = an;
/*     */     }
/* 105 */     data.setAlgorithm(alg);
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
/*     */   protected boolean authenticate(byte[] dataformatid, byte[] dataformatversion)
/*     */   {
/* 118 */     return (Arrays.equals(DATA_FORMAT_ID_, dataformatid)) && (Arrays.equals(DATA_FORMAT_VERSION_, dataformatversion));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */   private static final byte[] DATA_FORMAT_VERSION_ = { 1, 0, 0, 0 };
/*     */   
/* 155 */   private static final byte[] DATA_FORMAT_ID_ = { 117, 110, 97, 109 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private UCharacterName.AlgorithmName readAlg()
/*     */     throws IOException
/*     */   {
/* 167 */     UCharacterName.AlgorithmName result = new UCharacterName.AlgorithmName();
/*     */     
/* 169 */     int rangestart = this.m_dataInputStream_.readInt();
/* 170 */     int rangeend = this.m_dataInputStream_.readInt();
/* 171 */     byte type = this.m_dataInputStream_.readByte();
/* 172 */     byte variant = this.m_dataInputStream_.readByte();
/* 173 */     if (!result.setInfo(rangestart, rangeend, type, variant)) {
/* 174 */       return null;
/*     */     }
/*     */     
/* 177 */     int size = this.m_dataInputStream_.readChar();
/* 178 */     if (type == 1)
/*     */     {
/* 180 */       char[] factor = new char[variant];
/* 181 */       for (int j = 0; j < variant; j++) {
/* 182 */         factor[j] = this.m_dataInputStream_.readChar();
/*     */       }
/*     */       
/* 185 */       result.setFactor(factor);
/* 186 */       size -= (variant << 1);
/*     */     }
/*     */     
/* 189 */     StringBuilder prefix = new StringBuilder();
/* 190 */     char c = (char)(this.m_dataInputStream_.readByte() & 0xFF);
/* 191 */     while (c != 0)
/*     */     {
/* 193 */       prefix.append(c);
/* 194 */       c = (char)(this.m_dataInputStream_.readByte() & 0xFF);
/*     */     }
/*     */     
/* 197 */     result.setPrefix(prefix.toString());
/*     */     
/* 199 */     size -= 12 + prefix.length() + 1;
/*     */     
/* 201 */     if (size > 0)
/*     */     {
/* 203 */       byte[] string = new byte[size];
/* 204 */       this.m_dataInputStream_.readFully(string);
/* 205 */       result.setFactorString(string);
/*     */     }
/* 207 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCharacterNameReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */