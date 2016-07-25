package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;

public class HorizontalDeviceMetricsTable
  extends Table
{
  private int numGlyphs;
  
  private HorizontalDeviceMetricsTable(Header paramHeader, ReadableFontData paramReadableFontData, int paramInt)
  {
    super(paramHeader, paramReadableFontData);
    this.numGlyphs = paramInt;
  }
  
  public int version()
  {
    return this.data.readUShort(Offset.version.offset);
  }
  
  public int numRecords()
  {
    return this.data.readShort(Offset.numRecords.offset);
  }
  
  public int recordSize()
  {
    return this.data.readLong(Offset.sizeDeviceRecord.offset);
  }
  
  public int pixelSize(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= numRecords())) {
      throw new IndexOutOfBoundsException();
    }
    return this.data.readUByte(Offset.records.offset + paramInt * recordSize() + Offset.deviceRecordPixelSize.offset);
  }
  
  public int maxWidth(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= numRecords())) {
      throw new IndexOutOfBoundsException();
    }
    return this.data.readUByte(Offset.records.offset + paramInt * recordSize() + Offset.deviceRecordMaxWidth.offset);
  }
  
  public int width(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= numRecords()) || (paramInt2 < 0) || (paramInt2 >= this.numGlyphs)) {
      throw new IndexOutOfBoundsException();
    }
    return this.data.readUByte(Offset.records.offset + paramInt1 * recordSize() + Offset.deviceRecordWidths.offset + paramInt2);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<HorizontalDeviceMetricsTable>
  {
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
    
    protected HorizontalDeviceMetricsTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new HorizontalDeviceMetricsTable(header(), paramReadableFontData, this.numGlyphs, null);
    }
    
    public void setNumGlyphs(int paramInt)
    {
      if (paramInt < 0) {
        throw new IllegalArgumentException("Number of glyphs can't be negative.");
      }
      this.numGlyphs = paramInt;
      ((HorizontalDeviceMetricsTable)table()).numGlyphs = paramInt;
    }
  }
  
  private static enum Offset
  {
    version(0),  numRecords(2),  sizeDeviceRecord(4),  records(8),  deviceRecordPixelSize(0),  deviceRecordMaxWidth(1),  deviceRecordWidths(2);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\HorizontalDeviceMetricsTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */