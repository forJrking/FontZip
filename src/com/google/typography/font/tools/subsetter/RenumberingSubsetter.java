package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.Table;
import java.util.HashSet;
import java.util.Set;

public class RenumberingSubsetter
  extends Subsetter
{
  public RenumberingSubsetter(Font paramFont, FontFactory paramFontFactory)
  {
    super(paramFont, paramFontFactory);
    HashSet localHashSet = new HashSet();
    localHashSet.add(new GlyphTableSubsetter());
    localHashSet.add(new RenumberingCMapTableSubsetter());
    localHashSet.add(new PostScriptTableSubsetter());
    localHashSet.add(new HorizontalMetricsTableSubsetter());
    this.tableSubsetters = localHashSet;
  }
  
  protected void setUpTables(Font.Builder paramBuilder)
  {
    paramBuilder.newTableBuilder(Tag.hhea, this.font.getTable(Tag.hhea).readFontData());
    paramBuilder.newTableBuilder(Tag.maxp, this.font.getTable(Tag.maxp).readFontData());
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\RenumberingSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */