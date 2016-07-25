package com.ibm.icu.text;

public abstract interface UForwardCharacterIterator
{
  public static final int DONE = -1;
  
  public abstract int next();
  
  public abstract int nextCodePoint();
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UForwardCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */