package com.google.typography.font.tools.subsetter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class TableSubsetterImpl
  implements TableSubsetter
{
  protected final Set<Integer> tags;
  
  protected TableSubsetterImpl(Integer... paramVarArgs)
  {
    HashSet localHashSet = new HashSet(paramVarArgs.length);
    for (Integer localInteger : paramVarArgs) {
      localHashSet.add(localInteger);
    }
    this.tags = Collections.unmodifiableSet(localHashSet);
  }
  
  public boolean tagHandled(int paramInt)
  {
    return this.tags.contains(Integer.valueOf(paramInt));
  }
  
  public Set<Integer> tagsHandled()
  {
    return this.tags;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\TableSubsetterImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */