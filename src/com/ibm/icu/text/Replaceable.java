package com.ibm.icu.text;

public abstract interface Replaceable
{
  public abstract int length();
  
  public abstract char charAt(int paramInt);
  
  public abstract int char32At(int paramInt);
  
  public abstract void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3);
  
  public abstract void replace(int paramInt1, int paramInt2, String paramString);
  
  public abstract void replace(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4);
  
  public abstract void copy(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean hasMetaData();
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Replaceable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */