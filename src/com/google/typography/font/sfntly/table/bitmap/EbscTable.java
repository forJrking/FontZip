package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.Table.Builder;

public class EbscTable
  extends Table
{
  private EbscTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int version()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public int numSizes()
  {
    return this.data.readULongAsInt(Offset.numSizes.offset);
  }
  
  public BitmapScaleTable bitmapScaleTable(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > numSizes() - 1)) {
      throw new IndexOutOfBoundsException("BitmapScaleTable index is outside the bounds of available tables.");
    }
    return new BitmapScaleTable(this.data, Offset.bitmapScaleTableStart.offset + paramInt * Offset.bitmapScaleTableLength.offset);
  }
  
  public static class Builder
    extends Table.Builder<EbscTable>
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
    
    protected EbscTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new EbscTable(header(), paramReadableFontData, null);
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
      return 0;
    }
  }
  
  public static class BitmapScaleTable
    extends SubTable
  {
    protected BitmapScaleTable(ReadableFontData paramReadableFontData, int paramInt)
    {
      super(paramInt, EbscTable.Offset.bitmapScaleTableLength.offset);
    }
    
    public int ppemX()
    {
      return this.data.readByte(EbscTable.Offset.bitmapScaleTable_ppemX.offset);
    }
    
    public int ppemY()
    {
      return this.data.readByte(EbscTable.Offset.bitmapScaleTable_ppemY.offset);
    }
    
    public int substitutePpemX()
    {
      return this.data.readByte(EbscTable.Offset.bitmapScaleTable_substitutePpemX.offset);
    }
    
    public int substitutePpemY()
    {
      return this.data.readByte(EbscTable.Offset.bitmapScaleTable_substitutePpemY.offset);
    }
  }
  
  static enum Offset
  {
    version(0),  numSizes(FontData.DataSize.Fixed.size()),  headerLength(numSizes.offset + FontData.DataSize.ULONG.size()),  bitmapScaleTableStart(headerLength.offset),  bitmapScaleTable_hori(0),  bitmapScaleTable_vert(EblcTable.Offset.sbitLineMetricsLength.offset),  bitmapScaleTable_ppemX(bitmapScaleTable_vert.offset + EblcTable.Offset.sbitLineMetricsLength.offset),  bitmapScaleTable_ppemY(bitmapScaleTable_ppemX.offset + FontData.DataSize.BYTE.size()),  bitmapScaleTable_substitutePpemX(bitmapScaleTable_ppemY.offset + FontData.DataSize.BYTE.size()),  bitmapScaleTable_substitutePpemY(bitmapScaleTable_substitutePpemX.offset + FontData.DataSize.BYTE.size()),  bitmapScaleTableLength(bitmapScaleTable_substitutePpemY.offset + FontData.DataSize.BYTE.size());
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\EbscTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */