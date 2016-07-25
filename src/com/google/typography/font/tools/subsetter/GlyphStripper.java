package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.truetype.CompositeGlyph;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.Glyph.Builder;
import com.google.typography.font.sfntly.table.truetype.GlyphTable.Builder;
import com.google.typography.font.sfntly.table.truetype.SimpleGlyph;

public class GlyphStripper
{
  private final GlyphTable.Builder glyphTableBuilder;
  
  public GlyphStripper(GlyphTable.Builder paramBuilder)
  {
    this.glyphTableBuilder = paramBuilder;
  }
  
  public Glyph.Builder<? extends Glyph> stripGlyph(Glyph paramGlyph)
  {
    WritableFontData localWritableFontData = null;
    if ((paramGlyph != null) && (paramGlyph.readFontData().length() > 0)) {
      switch (paramGlyph.glyphType())
      {
      case Simple: 
        localWritableFontData = stripSimpleGlyph(paramGlyph);
        break;
      case Composite: 
        localWritableFontData = stripCompositeGlyph(paramGlyph);
        break;
      }
    }
    if (localWritableFontData == null) {
      localWritableFontData = WritableFontData.createWritableFontData(0);
    }
    return this.glyphTableBuilder.glyphBuilder(localWritableFontData);
  }
  
  private WritableFontData stripSimpleGlyph(Glyph paramGlyph)
  {
    int i = computeSimpleStrippedGlyphSize(paramGlyph);
    int j = i + 1 & 0xFFFFFFFE;
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(j);
    SimpleGlyph localSimpleGlyph = (SimpleGlyph)paramGlyph;
    ReadableFontData localReadableFontData = paramGlyph.readFontData();
    int k = writeHeaderAndContoursSize(localWritableFontData, 0, localReadableFontData, 0, localSimpleGlyph);
    k += writeZeroInstructionLength(localWritableFontData, k);
    k += writeEndSimpleGlyph(localWritableFontData, k, localReadableFontData, k + localSimpleGlyph.instructionSize() * FontData.DataSize.BYTE.size(), i - k);
    return localWritableFontData;
  }
  
  private int writeHeaderAndContoursSize(WritableFontData paramWritableFontData, int paramInt1, ReadableFontData paramReadableFontData, int paramInt2, SimpleGlyph paramSimpleGlyph)
  {
    int i = FontData.DataSize.SHORT.size() * 5 + paramSimpleGlyph.numberOfContours() * FontData.DataSize.USHORT.size();
    WritableFontData localWritableFontData = paramWritableFontData.slice(paramInt1, i);
    paramReadableFontData.slice(paramInt2, i).copyTo(localWritableFontData);
    return i;
  }
  
  private int writeZeroInstructionLength(WritableFontData paramWritableFontData, int paramInt)
  {
    paramWritableFontData.writeUShort(paramInt, 0);
    return FontData.DataSize.USHORT.size();
  }
  
  private int writeEndSimpleGlyph(WritableFontData paramWritableFontData, int paramInt1, ReadableFontData paramReadableFontData, int paramInt2, int paramInt3)
  {
    ReadableFontData localReadableFontData = paramReadableFontData.slice(paramInt2, paramInt3);
    WritableFontData localWritableFontData = paramWritableFontData.slice(paramInt1, paramInt3);
    localReadableFontData.copyTo(localWritableFontData);
    return paramInt3;
  }
  
  private WritableFontData stripCompositeGlyph(Glyph paramGlyph)
  {
    int i = computeCompositeStrippedGlyphSize(paramGlyph);
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(i);
    CompositeGlyph localCompositeGlyph = (CompositeGlyph)paramGlyph;
    ReadableFontData localReadableFontData = paramGlyph.readFontData().slice(0, i);
    localReadableFontData.copyTo(localWritableFontData);
    if (localCompositeGlyph.instructionSize() > 0) {
      overrideCompositeGlyfFlags(localWritableFontData, i);
    }
    return localWritableFontData;
  }
  
  private void overrideCompositeGlyfFlags(WritableFontData paramWritableFontData, int paramInt)
  {
    int i = 5 * FontData.DataSize.USHORT.size();
    int j = 32;
    while ((j & 0x20) != 0)
    {
      j = paramWritableFontData.readUShort(i);
      j &= 0xFEFF;
      paramWritableFontData.writeUShort(i, j);
      i += 2 * FontData.DataSize.USHORT.size();
      if ((j & 0x1) != 0) {
        i += 2 * FontData.DataSize.SHORT.size();
      } else {
        i += 2 * FontData.DataSize.BYTE.size();
      }
      if ((j & 0x8) != 0) {
        i += FontData.DataSize.F2DOT14.size();
      } else if ((j & 0x40) != 0) {
        i += 2 * FontData.DataSize.F2DOT14.size();
      } else if ((j & 0x80) != 0) {
        i += 4 * FontData.DataSize.F2DOT14.size();
      }
    }
  }
  
  private int computeSimpleStrippedGlyphSize(Glyph paramGlyph)
  {
    SimpleGlyph localSimpleGlyph = (SimpleGlyph)paramGlyph;
    int i = localSimpleGlyph.instructionSize();
    int j = localSimpleGlyph.dataLength() - localSimpleGlyph.padding();
    if (i > 0) {
      return j - computeInstructionsSize(localSimpleGlyph);
    }
    return j;
  }
  
  private int computeInstructionsSize(SimpleGlyph paramSimpleGlyph)
  {
    return paramSimpleGlyph.instructionSize() * FontData.DataSize.BYTE.size();
  }
  
  private int computeCompositeStrippedGlyphSize(Glyph paramGlyph)
  {
    CompositeGlyph localCompositeGlyph = (CompositeGlyph)paramGlyph;
    int i = localCompositeGlyph.instructionSize();
    int j = localCompositeGlyph.dataLength() - localCompositeGlyph.padding();
    if (i > 0) {
      return j - i * FontData.DataSize.BYTE.size() - FontData.DataSize.USHORT.size();
    }
    return j;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\GlyphStripper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */