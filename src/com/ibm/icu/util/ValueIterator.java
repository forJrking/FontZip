package com.ibm.icu.util;

public abstract interface ValueIterator
{
  public abstract boolean next(Element paramElement);
  
  public abstract void reset();
  
  public abstract void setRange(int paramInt1, int paramInt2);
  
  public static final class Element
  {
    public int integer;
    public Object value;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\ValueIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */