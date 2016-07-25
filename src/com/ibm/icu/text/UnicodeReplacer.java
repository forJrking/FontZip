package com.ibm.icu.text;

abstract interface UnicodeReplacer
{
  public abstract int replace(Replaceable paramReplaceable, int paramInt1, int paramInt2, int[] paramArrayOfInt);
  
  public abstract String toReplacerPattern(boolean paramBoolean);
  
  public abstract void addReplacementSetTo(UnicodeSet paramUnicodeSet);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeReplacer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */