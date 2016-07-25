package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.core.CMapTable.Builder;
import com.google.typography.font.sfntly.table.core.CMapTable.CMapId;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CMapTableSubsetter
  extends TableSubsetterImpl
{
  public CMapTableSubsetter()
  {
    super(new Integer[] { Integer.valueOf(Tag.cmap) });
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
    throws IOException
  {
    CMapTable localCMapTable = (CMapTable)paramFont.getTable(Tag.cmap);
    if (localCMapTable == null) {
      throw new RuntimeException("Font to subset is not valid.");
    }
    CMapTable.Builder localBuilder = (CMapTable.Builder)paramBuilder.newTableBuilder(Tag.cmap);
    Iterator localIterator = paramSubsetter.cmapId().iterator();
    while (localIterator.hasNext())
    {
      CMapTable.CMapId localCMapId = (CMapTable.CMapId)localIterator.next();
      CMap localCMap = localCMapTable.cmap(localCMapId);
      if (localCMap != null) {
        localBuilder.newCMapBuilder(localCMapId, localCMap.readFontData());
      }
    }
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\CMapTableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */