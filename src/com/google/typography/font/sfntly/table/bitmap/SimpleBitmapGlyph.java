package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public final class SimpleBitmapGlyph
  extends BitmapGlyph
{
  protected SimpleBitmapGlyph(ReadableFontData paramReadableFontData, int paramInt)
  {
    super(paramReadableFontData, paramInt);
  }
  
  public static class Builder
    extends BitmapGlyph.Builder<BitmapGlyph>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt)
    {
      super(paramInt);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt)
    {
      super(paramInt);
    }
    
    protected SimpleBitmapGlyph subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new SimpleBitmapGlyph(paramReadableFontData, format());
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\SimpleBitmapGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */