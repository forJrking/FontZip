package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMap.CMapFormat;
import com.google.typography.font.sfntly.table.core.CMapFormat4;
import com.google.typography.font.sfntly.table.core.CMapTable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenumberingCMapTableSubsetter
  extends TableSubsetterImpl
{
  public RenumberingCMapTableSubsetter()
  {
    super(new Integer[] { Integer.valueOf(Tag.cmap) });
  }
  
  private static CMapFormat4 getCMapFormat4(Font paramFont)
  {
    CMapTable localCMapTable = (CMapTable)paramFont.getTable(Tag.cmap);
    Iterator localIterator = localCMapTable.iterator();
    while (localIterator.hasNext())
    {
      CMap localCMap = (CMap)localIterator.next();
      if (localCMap.format() == CMap.CMapFormat.Format4.value()) {
        return (CMapFormat4)localCMap;
      }
    }
    return null;
  }
  
  static Map<Integer, Integer> computeMapping(Subsetter paramSubsetter, Font paramFont)
  {
    CMapFormat4 localCMapFormat4 = getCMapFormat4(paramFont);
    if (localCMapFormat4 == null) {
      throw new RuntimeException("CMap format 4 table in source font not found");
    }
    Map localMap = paramSubsetter.getInverseMapping();
    HashMap localHashMap = new HashMap();
    Iterator localIterator = localCMapFormat4.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      int i = localCMapFormat4.glyphId(localInteger.intValue());
      if (localMap.containsKey(Integer.valueOf(i))) {
        localHashMap.put(localInteger, localMap.get(Integer.valueOf(i)));
      }
    }
    return localHashMap;
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
    throws IOException
  {
    CMapTableBuilder localCMapTableBuilder = new CMapTableBuilder(paramBuilder, computeMapping(paramSubsetter, paramFont));
    localCMapTableBuilder.build();
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\RenumberingCMapTableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */