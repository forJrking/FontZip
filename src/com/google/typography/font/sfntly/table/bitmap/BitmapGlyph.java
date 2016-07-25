package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;

public abstract class BitmapGlyph
  extends SubTable
{
  private int format;
  
  public static BitmapGlyph createGlyph(ReadableFontData paramReadableFontData, int paramInt)
  {
    BitmapGlyph localBitmapGlyph = null;
    Builder localBuilder = Builder.createGlyphBuilder(paramReadableFontData, paramInt);
    if (localBuilder != null) {
      localBitmapGlyph = (BitmapGlyph)localBuilder.build();
    }
    return localBitmapGlyph;
  }
  
  protected BitmapGlyph(ReadableFontData paramReadableFontData, int paramInt)
  {
    super(paramReadableFontData);
    this.format = paramInt;
  }
  
  protected BitmapGlyph(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
    this.format = paramInt3;
  }
  
  public int format()
  {
    return this.format;
  }
  
  public String toString()
  {
    return "BitmapGlyph [format=" + this.format + ", data = " + super.toString() + "]";
  }
  
  public static abstract class Builder<T extends BitmapGlyph>
    extends SubTable.Builder<T>
  {
    private final int format;
    
    public static Builder<? extends BitmapGlyph> createGlyphBuilder(ReadableFontData paramReadableFontData, int paramInt)
    {
      switch (paramInt)
      {
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
        return new SimpleBitmapGlyph.Builder(paramReadableFontData, paramInt);
      case 8: 
      case 9: 
        return new CompositeBitmapGlyph.Builder(paramReadableFontData, paramInt);
      }
      return null;
    }
    
    protected Builder(WritableFontData paramWritableFontData, int paramInt)
    {
      super();
      this.format = paramInt;
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt)
    {
      super();
      this.format = paramInt;
    }
    
    public int format()
    {
      return this.format;
    }
    
    protected void subDataSet() {}
    
    protected int subDataSizeToSerialize()
    {
      return internalReadData().length();
    }
    
    protected boolean subReadyToSerialize()
    {
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      return internalReadData().copyTo(paramWritableFontData);
    }
  }
  
  protected static enum Offset
  {
    version(0),  smallGlyphMetricsLength(5),  bigGlyphMetricsLength(8),  glyphFormat1_imageData(smallGlyphMetricsLength.offset),  glyphFormat2_imageData(smallGlyphMetricsLength.offset),  glyphFormat5_imageData(0),  glyphFormat6_imageData(bigGlyphMetricsLength.offset),  glyphFormat7_imageData(bigGlyphMetricsLength.offset),  glyphFormat8_numComponents(smallGlyphMetricsLength.offset + 1),  glyphFormat8_componentArray(glyphFormat8_numComponents.offset + FontData.DataSize.USHORT.size()),  glyphFormat9_numComponents(bigGlyphMetricsLength.offset),  glyphFormat9_componentArray(glyphFormat9_numComponents.offset + FontData.DataSize.USHORT.size()),  ebdtComponentLength(FontData.DataSize.USHORT.size() + 2 * FontData.DataSize.CHAR.size()),  ebdtComponent_glyphCode(0),  ebdtComponent_xOffset(2),  ebdtComponent_yOffset(3);
    
    protected final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\BitmapGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */