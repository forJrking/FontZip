package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Map;

public class GlyphRenumberer
{
  public static ReadableFontData renumberGlyph(ReadableFontData paramReadableFontData, Map<Integer, Integer> paramMap)
  {
    return isCompositeGlyph(paramReadableFontData) ? renumberCompositeGlyph(paramReadableFontData, paramMap) : paramReadableFontData;
  }
  
  private static boolean isCompositeGlyph(ReadableFontData paramReadableFontData)
  {
    return (paramReadableFontData.length() > 0) && (paramReadableFontData.readShort(Offset.numberOfContours.offset) < 0);
  }
  
  private static ReadableFontData renumberCompositeGlyph(ReadableFontData paramReadableFontData, Map<Integer, Integer> paramMap)
  {
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramReadableFontData.length());
    paramReadableFontData.copyTo(localWritableFontData);
    int i = 32;
    int j = Offset.headerEnd.offset;
    while ((i & 0x20) != 0)
    {
      i = paramReadableFontData.readUShort(j + Offset.compositeFlags.offset);
      int k = paramReadableFontData.readUShort(j + Offset.compositeGlyphIndex.offset);
      int m = ((Integer)paramMap.get(Integer.valueOf(k))).intValue();
      localWritableFontData.writeUShort(j + Offset.compositeGlyphIndex.offset, m);
      j += compositeReferenceSize(i);
    }
    return localWritableFontData;
  }
  
  private static int compositeReferenceSize(int paramInt)
  {
    int i = 6;
    if ((paramInt & 0x1) != 0) {
      i += 2;
    }
    if ((paramInt & 0x8) != 0) {
      i += 2;
    } else if ((paramInt & 0x40) != 0) {
      i += 4;
    } else if ((paramInt & 0x80) != 0) {
      i += 8;
    }
    return i;
  }
  
  private static enum Offset
  {
    numberOfContours(0),  headerEnd(10),  compositeFlags(0),  compositeGlyphIndex(2);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\GlyphRenumberer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */