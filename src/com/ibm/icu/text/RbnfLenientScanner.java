package com.ibm.icu.text;

public abstract interface RbnfLenientScanner
{
  public abstract boolean allIgnorable(String paramString);
  
  public abstract int prefixLength(String paramString1, String paramString2);
  
  public abstract int[] findText(String paramString1, String paramString2, int paramInt);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RbnfLenientScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */