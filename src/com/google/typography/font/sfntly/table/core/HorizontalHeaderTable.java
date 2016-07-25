package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;

public final class HorizontalHeaderTable
  extends Table
{
  private HorizontalHeaderTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int tableVersion()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public int ascender()
  {
    return this.data.readShort(Offset.Ascender.offset);
  }
  
  public int descender()
  {
    return this.data.readShort(Offset.Descender.offset);
  }
  
  public int lineGap()
  {
    return this.data.readShort(Offset.LineGap.offset);
  }
  
  public int advanceWidthMax()
  {
    return this.data.readUShort(Offset.advanceWidthMax.offset);
  }
  
  public int minLeftSideBearing()
  {
    return this.data.readShort(Offset.minLeftSideBearing.offset);
  }
  
  public int minRightSideBearing()
  {
    return this.data.readShort(Offset.minRightSideBearing.offset);
  }
  
  public int xMaxExtent()
  {
    return this.data.readShort(Offset.xMaxExtent.offset);
  }
  
  public int caretSlopeRise()
  {
    return this.data.readShort(Offset.caretSlopeRise.offset);
  }
  
  public int caretSlopeRun()
  {
    return this.data.readShort(Offset.caretSlopeRun.offset);
  }
  
  public int caretOffset()
  {
    return this.data.readShort(Offset.caretOffset.offset);
  }
  
  public int metricDataFormat()
  {
    return this.data.readShort(Offset.metricDataFormat.offset);
  }
  
  public int numberOfHMetrics()
  {
    return this.data.readUShort(Offset.numberOfHMetrics.offset);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<HorizontalHeaderTable>
  {
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    protected HorizontalHeaderTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new HorizontalHeaderTable(header(), paramReadableFontData, null);
    }
    
    public int tableVersion()
    {
      return internalReadData().readFixed(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.version));
    }
    
    public void setTableVersion(int paramInt)
    {
      internalWriteData().writeFixed(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.version), paramInt);
    }
    
    public int ascender()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.Ascender));
    }
    
    public void setAscender(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.Ascender), paramInt);
    }
    
    public int descender()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.Descender));
    }
    
    public void setDescender(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.Descender), paramInt);
    }
    
    public int lineGap()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.LineGap));
    }
    
    public void setLineGap(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.LineGap), paramInt);
    }
    
    public int advanceWidthMax()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.advanceWidthMax));
    }
    
    public void setAdvanceWidthMax(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.advanceWidthMax), paramInt);
    }
    
    public int minLeftSideBearing()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.minLeftSideBearing));
    }
    
    public void setMinLeftSideBearing(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.minLeftSideBearing), paramInt);
    }
    
    public int minRightSideBearing()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.minRightSideBearing));
    }
    
    public void setMinRightSideBearing(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.minRightSideBearing), paramInt);
    }
    
    public int xMaxExtent()
    {
      return internalReadData().readShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.xMaxExtent));
    }
    
    public void setXMaxExtent(int paramInt)
    {
      internalWriteData().writeShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.xMaxExtent), paramInt);
    }
    
    public int caretSlopeRise()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretSlopeRise));
    }
    
    public void setCaretSlopeRise(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretSlopeRise), paramInt);
    }
    
    public int caretSlopeRun()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretSlopeRun));
    }
    
    public void setCaretSlopeRun(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretSlopeRun), paramInt);
    }
    
    public int caretOffset()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretOffset));
    }
    
    public void setCaretOffset(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.caretOffset), paramInt);
    }
    
    public int metricDataFormat()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.metricDataFormat));
    }
    
    public void setMetricDataFormat(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.metricDataFormat), paramInt);
    }
    
    public int numberOfHMetrics()
    {
      return internalReadData().readUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.numberOfHMetrics));
    }
    
    public void setNumberOfHMetrics(int paramInt)
    {
      internalWriteData().writeUShort(HorizontalHeaderTable.Offset.access$000(HorizontalHeaderTable.Offset.numberOfHMetrics), paramInt);
    }
  }
  
  private static enum Offset
  {
    version(0),  Ascender(4),  Descender(6),  LineGap(8),  advanceWidthMax(10),  minLeftSideBearing(12),  minRightSideBearing(14),  xMaxExtent(16),  caretSlopeRise(18),  caretSlopeRun(20),  caretOffset(22),  metricDataFormat(32),  numberOfHMetrics(34);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\HorizontalHeaderTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */