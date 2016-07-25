package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;

public final class HorizontalMetricsTable
  extends Table
{
  private int numHMetrics;
  private int numGlyphs;
  
  private HorizontalMetricsTable(Header paramHeader, ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramHeader, paramReadableFontData);
    this.numHMetrics = paramInt1;
    this.numGlyphs = paramInt2;
  }
  
  public int numberOfHMetrics()
  {
    return this.numHMetrics;
  }
  
  public int numberOfLSBs()
  {
    return this.numGlyphs - this.numHMetrics;
  }
  
  public int hMetricAdvanceWidth(int paramInt)
  {
    if (paramInt > this.numHMetrics) {
      throw new IndexOutOfBoundsException();
    }
    int i = Offset.hMetricsStart.offset + paramInt * Offset.hMetricsSize.offset + Offset.hMetricsAdvanceWidth.offset;
    return this.data.readUShort(i);
  }
  
  public int hMetricLSB(int paramInt)
  {
    if (paramInt > this.numHMetrics) {
      throw new IndexOutOfBoundsException();
    }
    int i = Offset.hMetricsStart.offset + paramInt * Offset.hMetricsSize.offset + Offset.hMetricsLeftSideBearing.offset;
    return this.data.readShort(i);
  }
  
  public int lsbTableEntry(int paramInt)
  {
    if (paramInt > numberOfLSBs()) {
      throw new IndexOutOfBoundsException();
    }
    int i = Offset.hMetricsStart.offset + this.numHMetrics * Offset.hMetricsSize.offset + paramInt * Offset.LeftSideBearingSize.offset;
    return this.data.readShort(i);
  }
  
  public int advanceWidth(int paramInt)
  {
    if (paramInt < this.numHMetrics) {
      return hMetricAdvanceWidth(paramInt);
    }
    return hMetricAdvanceWidth(this.numHMetrics - 1);
  }
  
  public int leftSideBearing(int paramInt)
  {
    if (paramInt < this.numHMetrics) {
      return hMetricLSB(paramInt);
    }
    return lsbTableEntry(paramInt - this.numHMetrics);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<HorizontalMetricsTable>
  {
    private int numHMetrics = -1;
    private int numGlyphs = -1;
    
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
    
    protected HorizontalMetricsTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new HorizontalMetricsTable(header(), paramReadableFontData, this.numHMetrics, this.numGlyphs, null);
    }
    
    public void setNumberOfHMetrics(int paramInt)
    {
      if (paramInt < 0) {
        throw new IllegalArgumentException("Number of metrics can't be negative.");
      }
      this.numHMetrics = paramInt;
      ((HorizontalMetricsTable)table()).numHMetrics = paramInt;
    }
    
    public void setNumGlyphs(int paramInt)
    {
      if (paramInt < 0) {
        throw new IllegalArgumentException("Number of glyphs can't be negative.");
      }
      this.numGlyphs = paramInt;
      ((HorizontalMetricsTable)table()).numGlyphs = paramInt;
    }
  }
  
  private static enum Offset
  {
    hMetricsStart(0),  hMetricsSize(4),  hMetricsAdvanceWidth(0),  hMetricsLeftSideBearing(2),  LeftSideBearingSize(2);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\HorizontalMetricsTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */