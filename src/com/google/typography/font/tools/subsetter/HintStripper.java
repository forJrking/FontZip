package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import java.util.HashSet;
import java.util.Set;

public class HintStripper
  extends Subsetter
{
  public HintStripper(Font paramFont, FontFactory paramFontFactory)
  {
    super(paramFont, paramFontFactory);
    HashSet localHashSet = new HashSet();
    localHashSet.add(new GlyphTableStripper());
    this.tableSubsetters = localHashSet;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\HintStripper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */