package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.table.core.MaximumProfileTable.Builder;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.Glyph.Builder;
import com.google.typography.font.sfntly.table.truetype.GlyphTable;
import com.google.typography.font.sfntly.table.truetype.GlyphTable.Builder;
import com.google.typography.font.sfntly.table.truetype.LocaTable;
import com.google.typography.font.sfntly.table.truetype.LocaTable.Builder;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlyphTableSubsetter
  extends TableSubsetterImpl
{
  private static final boolean DEBUG = false;
  
  protected GlyphTableSubsetter()
  {
    super(new Integer[] { Integer.valueOf(Tag.glyf), Integer.valueOf(Tag.loca), Integer.valueOf(Tag.maxp) });
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
    throws IOException
  {
    List localList1 = paramSubsetter.glyphMappingTable();
    if (localList1 == null) {
      return false;
    }
    GlyphTable localGlyphTable = (GlyphTable)paramFont.getTable(Tag.glyf);
    LocaTable localLocaTable = (LocaTable)paramFont.getTable(Tag.loca);
    if ((localGlyphTable == null) || (localLocaTable == null)) {
      throw new RuntimeException("Font to subset is not valid.");
    }
    GlyphTable.Builder localBuilder = (GlyphTable.Builder)paramBuilder.newTableBuilder(Tag.glyf);
    LocaTable.Builder localBuilder1 = (LocaTable.Builder)paramBuilder.newTableBuilder(Tag.loca);
    if ((localBuilder == null) || (localBuilder1 == null)) {
      throw new RuntimeException("Builder for subset is not valid.");
    }
    Map localMap = paramSubsetter.getInverseMapping();
    List localList2 = localBuilder.glyphBuilders();
    Object localObject = localList1.iterator();
    while (((Iterator)localObject).hasNext())
    {
      int i = ((Integer)((Iterator)localObject).next()).intValue();
      int j = localLocaTable.glyphOffset(i);
      int k = localLocaTable.glyphLength(i);
      Glyph localGlyph = localGlyphTable.glyph(j, k);
      ReadableFontData localReadableFontData1 = localGlyph.readFontData();
      ReadableFontData localReadableFontData2 = GlyphRenumberer.renumberGlyph(localReadableFontData1, localMap);
      Glyph.Builder localBuilder3 = localBuilder.glyphBuilder(localReadableFontData2);
      localList2.add(localBuilder3);
    }
    localObject = localBuilder.generateLocaList();
    localBuilder1.setLocaList((List)localObject);
    MaximumProfileTable.Builder localBuilder2 = (MaximumProfileTable.Builder)paramBuilder.getTableBuilder(Tag.maxp);
    localBuilder2.setNumGlyphs(localBuilder1.numGlyphs());
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\GlyphTableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */