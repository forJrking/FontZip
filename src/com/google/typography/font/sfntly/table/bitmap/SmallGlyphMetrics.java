package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public class SmallGlyphMetrics
  extends GlyphMetrics
{
  private SmallGlyphMetrics(ReadableFontData paramReadableFontData)
  {
    super(paramReadableFontData);
  }
  
  public int height()
  {
    return this.data.readByte(Offset.height.offset);
  }
  
  public int width()
  {
    return this.data.readByte(Offset.width.offset);
  }
  
  public int bearingX()
  {
    return this.data.readChar(Offset.BearingX.offset);
  }
  
  public int bearingY()
  {
    return this.data.readChar(Offset.BearingY.offset);
  }
  
  public int advance()
  {
    return this.data.readByte(Offset.Advance.offset);
  }
  
  public static class Builder
    extends GlyphMetrics.Builder<SmallGlyphMetrics>
  {
    protected Builder(WritableFontData paramWritableFontData)
    {
      super();
    }
    
    protected Builder(ReadableFontData paramReadableFontData)
    {
      super();
    }
    
    public int height()
    {
      return internalReadData().readByte(SmallGlyphMetrics.Offset.height.offset);
    }
    
    public void setHeight(byte paramByte)
    {
      internalWriteData().writeByte(SmallGlyphMetrics.Offset.height.offset, paramByte);
    }
    
    public int width()
    {
      return internalReadData().readByte(SmallGlyphMetrics.Offset.width.offset);
    }
    
    public void setWidth(byte paramByte)
    {
      internalWriteData().writeByte(SmallGlyphMetrics.Offset.width.offset, paramByte);
    }
    
    public int bearingX()
    {
      return internalReadData().readChar(SmallGlyphMetrics.Offset.BearingX.offset);
    }
    
    public void setBearingX(byte paramByte)
    {
      internalWriteData().writeChar(SmallGlyphMetrics.Offset.BearingX.offset, paramByte);
    }
    
    public int bearingY()
    {
      return internalReadData().readChar(SmallGlyphMetrics.Offset.BearingY.offset);
    }
    
    public void setBearingY(byte paramByte)
    {
      internalWriteData().writeChar(SmallGlyphMetrics.Offset.BearingY.offset, paramByte);
    }
    
    public int advance()
    {
      return internalReadData().readByte(SmallGlyphMetrics.Offset.Advance.offset);
    }
    
    public void setAdvance(byte paramByte)
    {
      internalWriteData().writeByte(SmallGlyphMetrics.Offset.Advance.offset, paramByte);
    }
    
    protected SmallGlyphMetrics subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new SmallGlyphMetrics(paramReadableFontData, null);
    }
    
    protected void subDataSet() {}
    
    protected int subDataSizeToSerialize()
    {
      return 0;
    }
    
    protected boolean subReadyToSerialize()
    {
      return false;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      return data().copyTo(paramWritableFontData);
    }
  }
  
  static enum Offset
  {
    metricsLength(5),  height(0),  width(1),  BearingX(2),  BearingY(3),  Advance(4);
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\SmallGlyphMetrics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */