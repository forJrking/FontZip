package com.ibm.icu.impl.duration.impl;

abstract interface RecordWriter
{
  public abstract boolean open(String paramString);
  
  public abstract boolean close();
  
  public abstract void bool(String paramString, boolean paramBoolean);
  
  public abstract void boolArray(String paramString, boolean[] paramArrayOfBoolean);
  
  public abstract void character(String paramString, char paramChar);
  
  public abstract void characterArray(String paramString, char[] paramArrayOfChar);
  
  public abstract void namedIndex(String paramString, String[] paramArrayOfString, int paramInt);
  
  public abstract void namedIndexArray(String paramString, String[] paramArrayOfString, byte[] paramArrayOfByte);
  
  public abstract void string(String paramString1, String paramString2);
  
  public abstract void stringArray(String paramString, String[] paramArrayOfString);
  
  public abstract void stringTable(String paramString, String[][] paramArrayOfString);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\RecordWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */