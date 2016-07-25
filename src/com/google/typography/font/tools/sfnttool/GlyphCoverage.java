package com.google.typography.font.tools.sfnttool;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMap.CMapFormat;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.truetype.CompositeGlyph;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.Glyph.GlyphType;
import com.google.typography.font.sfntly.table.truetype.GlyphTable;
import com.google.typography.font.sfntly.table.truetype.LocaTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GlyphCoverage
{
  public static List<Integer> getGlyphCoverage(Font paramFont, String paramString)
  {
    CMapTable localCMapTable = (CMapTable)paramFont.getTable(Tag.cmap);
    CMap localCMap = getBestCMap(localCMapTable);
    HashSet localHashSet = new HashSet();
    localHashSet.add(Integer.valueOf(0));
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i) & 0xFFFF;
      int k = localCMap.glyphId(j);
      touchGlyph(paramFont, localHashSet, k);
    }
    ArrayList localArrayList = new ArrayList(localHashSet);
    Collections.sort(localArrayList);
    return localArrayList;
  }
  
  private static void touchGlyph(Font paramFont, Set<Integer> paramSet, int paramInt)
  {
    if (!paramSet.contains(Integer.valueOf(paramInt)))
    {
      paramSet.add(Integer.valueOf(paramInt));
      Glyph localGlyph = getGlyph(paramFont, paramInt);
      if ((localGlyph != null) && (localGlyph.glyphType() == Glyph.GlyphType.Composite))
      {
        CompositeGlyph localCompositeGlyph = (CompositeGlyph)localGlyph;
        for (int i = 0; i < localCompositeGlyph.numGlyphs(); i++) {
          touchGlyph(paramFont, paramSet, localCompositeGlyph.glyphIndex(i));
        }
      }
    }
  }
  
  private static CMap getBestCMap(CMapTable paramCMapTable)
  {
    Iterator localIterator = paramCMapTable.iterator();
    CMap localCMap;
    while (localIterator.hasNext())
    {
      localCMap = (CMap)localIterator.next();
      if (localCMap.format() == CMap.CMapFormat.Format12.value()) {
        return localCMap;
      }
    }
    localIterator = paramCMapTable.iterator();
    while (localIterator.hasNext())
    {
      localCMap = (CMap)localIterator.next();
      if (localCMap.format() == CMap.CMapFormat.Format4.value()) {
        return localCMap;
      }
    }
    return null;
  }
  
  private static Glyph getGlyph(Font paramFont, int paramInt)
  {
    LocaTable localLocaTable = (LocaTable)paramFont.getTable(Tag.loca);
    GlyphTable localGlyphTable = (GlyphTable)paramFont.getTable(Tag.glyf);
    int i = localLocaTable.glyphOffset(paramInt);
    int j = localLocaTable.glyphLength(paramInt);
    return localGlyphTable.glyph(i, j);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\sfnttool\GlyphCoverage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */