package com.ibm.icu.impl;

public abstract class CacheBase<K, V, D>
{
  public abstract V getInstance(K paramK, D paramD);
  
  protected abstract V createInstance(K paramK, D paramD);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CacheBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */