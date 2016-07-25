package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.GlyphTable;
import com.google.typography.font.sfntly.table.truetype.GlyphTable.Builder;
import com.google.typography.font.sfntly.table.truetype.LocaTable;
import com.google.typography.font.sfntly.table.truetype.LocaTable.Builder;
import java.io.IOException;
import java.util.List;

public class GlyphTableStripper
  extends TableSubsetterImpl
{
  public GlyphTableStripper()
  {
    super(new Integer[] { Integer.valueOf(Tag.glyf), Integer.valueOf(Tag.loca) });
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
    throws IOException
  {
    GlyphTable localGlyphTable = (GlyphTable)paramFont.getTable(Tag.glyf);
    LocaTable localLocaTable = (LocaTable)paramFont.getTable(Tag.loca);
    if ((localGlyphTable == null) || (localLocaTable == null)) {
      throw new RuntimeException("Font to subset is not valid.");
    }
    ReadableFontData localReadableFontData = localGlyphTable.readFontData();
    GlyphTable.Builder localBuilder = (GlyphTable.Builder)paramBuilder.newTableBuilder(Tag.glyf);
    LocaTable.Builder localBuilder1 = (LocaTable.Builder)paramBuilder.newTableBuilder(Tag.loca);
    List localList1 = localBuilder.glyphBuilders();
    GlyphStripper localGlyphStripper = new GlyphStripper(localBuilder);
    for (int i = 0; i < localLocaTable.numGlyphs(); i++)
    {
      int j = localLocaTable.glyphOffset(i);
      int k = localLocaTable.glyphLength(i);
      Glyph localGlyph = localGlyphTable.glyph(j, k);
      localList1.add(localGlyphStripper.stripGlyph(localGlyph));
    }
    List localList2 = localBuilder.generateLocaList();
    localBuilder1.setLocaList(localList2);
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\GlyphTableStripper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */