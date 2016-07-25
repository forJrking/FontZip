package com.ibm.icu.util;

public abstract interface Freezable<T>
  extends Cloneable
{
  public abstract boolean isFrozen();
  
  public abstract T freeze();
  
  public abstract T cloneAsThawed();
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Freezable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */