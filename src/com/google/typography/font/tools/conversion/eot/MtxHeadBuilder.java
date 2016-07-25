package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.FontHeaderTable;

public class MtxHeadBuilder
{
  private static final int HEAD_TABLE_SIZE = 54;
  private final WritableFontData data = WritableFontData.createWritableFontData(54);
  
  public void initFrom(FontHeaderTable paramFontHeaderTable)
  {
    if (paramFontHeaderTable == null) {
      throw new IllegalArgumentException("source table must not be null");
    }
    paramFontHeaderTable.readFontData().slice(0, 54).copyTo(this.data);
  }
  
  public MtxHeadBuilder setIndexToLOCFormat(int paramInt)
  {
    this.data.writeUShort(Offset.indexToLocFormat.offset, paramInt);
    return this;
  }
  
  public ReadableFontData build()
  {
    return this.data;
  }
  
  private static enum Offset
  {
    tableVersion(0),  fontRevision(4),  checkSumAdjustment(8),  magicNumber(12),  flags(16),  unitsPerEm(18),  created(20),  modified(28),  xMin(36),  yMin(38),  xMax(40),  yMax(42),  macStyle(44),  lowestRecPPEM(46),  fontDirectionHint(48),  indexToLocFormat(50),  glyphDataFormat(52);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\MtxHeadBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */