package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public class BigGlyphMetrics
  extends GlyphMetrics
{
  BigGlyphMetrics(ReadableFontData paramReadableFontData)
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
  
  public int horiBearingX()
  {
    return this.data.readChar(Offset.horiBearingX.offset);
  }
  
  public int horiBearingY()
  {
    return this.data.readChar(Offset.horiBearingY.offset);
  }
  
  public int horiAdvance()
  {
    return this.data.readByte(Offset.horiAdvance.offset);
  }
  
  public int vertBearingX()
  {
    return this.data.readChar(Offset.vertBearingX.offset);
  }
  
  public int vertBearingY()
  {
    return this.data.readChar(Offset.vertBearingY.offset);
  }
  
  public int vertAdvance()
  {
    return this.data.readByte(Offset.vertAdvance.offset);
  }
  
  public static class Builder
    extends GlyphMetrics.Builder<BigGlyphMetrics>
  {
    public static Builder createBuilder()
    {
      WritableFontData localWritableFontData = WritableFontData.createWritableFontData(BigGlyphMetrics.Offset.metricsLength.offset);
      return new Builder(localWritableFontData);
    }
    
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
      return internalReadData().readByte(BigGlyphMetrics.Offset.height.offset);
    }
    
    public void setHeight(byte paramByte)
    {
      internalWriteData().writeByte(BigGlyphMetrics.Offset.height.offset, paramByte);
    }
    
    public int width()
    {
      return internalReadData().readByte(BigGlyphMetrics.Offset.width.offset);
    }
    
    public void setWidth(byte paramByte)
    {
      internalWriteData().writeByte(BigGlyphMetrics.Offset.width.offset, paramByte);
    }
    
    public int horiBearingX()
    {
      return internalReadData().readChar(BigGlyphMetrics.Offset.horiBearingX.offset);
    }
    
    public void setHoriBearingX(byte paramByte)
    {
      internalWriteData().writeChar(BigGlyphMetrics.Offset.horiBearingX.offset, paramByte);
    }
    
    public int horiBearingY()
    {
      return internalReadData().readChar(BigGlyphMetrics.Offset.horiBearingY.offset);
    }
    
    public void setHoriBearingY(byte paramByte)
    {
      internalWriteData().writeChar(BigGlyphMetrics.Offset.horiBearingY.offset, paramByte);
    }
    
    public int horiAdvance()
    {
      return internalReadData().readByte(BigGlyphMetrics.Offset.horiAdvance.offset);
    }
    
    public void setHoriAdvance(byte paramByte)
    {
      internalWriteData().writeByte(BigGlyphMetrics.Offset.horiAdvance.offset, paramByte);
    }
    
    public int vertBearingX()
    {
      return internalReadData().readChar(BigGlyphMetrics.Offset.vertBearingX.offset);
    }
    
    public void setVertBearingX(byte paramByte)
    {
      internalWriteData().writeChar(BigGlyphMetrics.Offset.vertBearingX.offset, paramByte);
    }
    
    public int vertBearingY()
    {
      return internalReadData().readChar(BigGlyphMetrics.Offset.vertBearingY.offset);
    }
    
    public void setVertBearingY(byte paramByte)
    {
      internalWriteData().writeChar(BigGlyphMetrics.Offset.vertBearingY.offset, paramByte);
    }
    
    public int vertAdvance()
    {
      return internalReadData().readByte(BigGlyphMetrics.Offset.vertAdvance.offset);
    }
    
    public void setVertAdvance(byte paramByte)
    {
      internalWriteData().writeByte(BigGlyphMetrics.Offset.vertAdvance.offset, paramByte);
    }
    
    protected BigGlyphMetrics subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new BigGlyphMetrics(paramReadableFontData);
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
    metricsLength(8),  height(0),  width(1),  horiBearingX(2),  horiBearingY(3),  horiAdvance(4),  vertBearingX(5),  vertBearingY(6),  vertAdvance(7);
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\BigGlyphMetrics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */