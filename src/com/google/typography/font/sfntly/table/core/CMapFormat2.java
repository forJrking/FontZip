package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;

public final class CMapFormat2
  extends CMap
{
  protected CMapFormat2(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format2.value, paramCMapId);
  }
  
  private int subHeaderOffset(int paramInt)
  {
    int i = this.data.readUShort(CMapTable.Offset.format2SubHeaderKeys.offset + paramInt * FontData.DataSize.USHORT.size());
    return i;
  }
  
  private int firstCode(int paramInt)
  {
    int i = subHeaderOffset(paramInt);
    int j = this.data.readUShort(i + CMapTable.Offset.format2SubHeaderKeys.offset + CMapTable.Offset.format2SubHeader_firstCode.offset);
    return j;
  }
  
  private int entryCount(int paramInt)
  {
    int i = subHeaderOffset(paramInt);
    int j = this.data.readUShort(i + CMapTable.Offset.format2SubHeaderKeys.offset + CMapTable.Offset.format2SubHeader_entryCount.offset);
    return j;
  }
  
  private int idRangeOffset(int paramInt)
  {
    int i = subHeaderOffset(paramInt);
    int j = this.data.readUShort(i + CMapTable.Offset.format2SubHeaderKeys.offset + CMapTable.Offset.format2SubHeader_idRangeOffset.offset);
    return j;
  }
  
  private int idDelta(int paramInt)
  {
    int i = subHeaderOffset(paramInt);
    int j = this.data.readShort(i + CMapTable.Offset.format2SubHeaderKeys.offset + CMapTable.Offset.format2SubHeader_idDelta.offset);
    return j;
  }
  
  public int bytesConsumed(int paramInt)
  {
    int i = paramInt >> 8 & 0xFF;
    int j = subHeaderOffset(i);
    if (j == 0) {
      return 1;
    }
    return 2;
  }
  
  public int glyphId(int paramInt)
  {
    if (paramInt > 65535) {
      return 0;
    }
    int i = paramInt >> 8 & 0xFF;
    int j = paramInt & 0xFF;
    int k = subHeaderOffset(i);
    if (k == 0)
    {
      j = i;
      i = 0;
    }
    int m = firstCode(i);
    int n = entryCount(i);
    if ((j < m) || (j >= m + n)) {
      return 0;
    }
    int i1 = idRangeOffset(i);
    int i2 = k + CMapTable.Offset.format2SubHeader_idRangeOffset.offset + i1 + (j - m) * FontData.DataSize.USHORT.size();
    int i3 = this.data.readUShort(i2);
    if (i3 == 0) {
      return 0;
    }
    if (k == 0) {
      return i3;
    }
    int i4 = idDelta(i);
    return (i3 + i4) % 65536;
  }
  
  public int language()
  {
    return this.data.readUShort(CMapTable.Offset.format2Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CMap.CharacterIterator(this, 0, 65535);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat2>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format2, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format2, paramCMapId);
    }
    
    protected CMapFormat2 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat2(paramReadableFontData, cmapId());
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */