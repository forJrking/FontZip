package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.FontHeaderTable;
import com.google.typography.font.sfntly.table.core.HorizontalDeviceMetricsTable;
import com.google.typography.font.sfntly.table.core.HorizontalMetricsTable;
import com.google.typography.font.sfntly.table.core.MaximumProfileTable;

public class HdmxEncoder
{
  private static int HEADER_SIZE = 8;
  private static int RECORD_SIZE = 2;
  
  public WritableFontData encode(Font paramFont)
  {
    HorizontalDeviceMetricsTable localHorizontalDeviceMetricsTable = (HorizontalDeviceMetricsTable)paramFont.getTable(Tag.hdmx);
    HorizontalMetricsTable localHorizontalMetricsTable = (HorizontalMetricsTable)paramFont.getTable(Tag.hmtx);
    MaximumProfileTable localMaximumProfileTable = (MaximumProfileTable)paramFont.getTable(Tag.maxp);
    FontHeaderTable localFontHeaderTable = (FontHeaderTable)paramFont.getTable(Tag.head);
    int i = localFontHeaderTable.unitsPerEm();
    int j = localHorizontalDeviceMetricsTable.numRecords();
    int k = localMaximumProfileTable.numGlyphs();
    MagnitudeDependentWriter localMagnitudeDependentWriter = new MagnitudeDependentWriter();
    for (int m = 0; m < j; m++)
    {
      n = localHorizontalDeviceMetricsTable.pixelSize(m);
      for (int i1 = 0; i1 < k; i1++)
      {
        i2 = ((64 * n * localHorizontalMetricsTable.advanceWidth(i1) + i / 2) / i + 32) / 64;
        int i3 = localHorizontalDeviceMetricsTable.width(m, i1) - i2;
        localMagnitudeDependentWriter.writeValue(i3);
      }
    }
    localMagnitudeDependentWriter.flush();
    byte[] arrayOfByte = localMagnitudeDependentWriter.toByteArray();
    int n = arrayOfByte.length + HEADER_SIZE + RECORD_SIZE * j;
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(n);
    localWritableFontData.writeUShort(0, 0);
    localWritableFontData.writeUShort(2, j);
    localWritableFontData.writeLong(4, localHorizontalDeviceMetricsTable.recordSize());
    for (int i2 = 0; i2 < j; i2++)
    {
      localWritableFontData.writeByte(HEADER_SIZE + RECORD_SIZE * i2, (byte)localHorizontalDeviceMetricsTable.pixelSize(i2));
      localWritableFontData.writeByte(HEADER_SIZE + RECORD_SIZE * i2 + 1, (byte)localHorizontalDeviceMetricsTable.maxWidth(i2));
    }
    localWritableFontData.writeBytes(HEADER_SIZE + RECORD_SIZE * j, arrayOfByte);
    return localWritableFontData;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\HdmxEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */