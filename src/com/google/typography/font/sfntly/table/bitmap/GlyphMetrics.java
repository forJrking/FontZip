package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;

abstract class GlyphMetrics
  extends SubTable
{
  protected GlyphMetrics(ReadableFontData paramReadableFontData)
  {
    super(paramReadableFontData);
  }
  
  static abstract class Builder<T extends GlyphMetrics>
    extends SubTable.Builder<T>
  {
    protected Builder(WritableFontData paramWritableFontData)
    {
      super();
    }
    
    protected Builder(ReadableFontData paramReadableFontData)
    {
      super();
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\GlyphMetrics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */