package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import java.io.IOException;
import java.util.Set;

public abstract interface TableSubsetter
{
  public abstract Set<Integer> tagsHandled();
  
  public abstract boolean tagHandled(int paramInt);
  
  public abstract boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
    throws IOException;
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\TableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */