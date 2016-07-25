package com.ibm.icu.util;

public abstract interface RangeValueIterator
{
  public abstract boolean next(Element paramElement);
  
  public abstract void reset();
  
  public static class Element
  {
    public int start;
    public int limit;
    public int value;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\RangeValueIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */