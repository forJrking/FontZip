package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;

public final class CMapFormat14
  extends CMap
{
  protected CMapFormat14(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format14.value, paramCMapId);
  }
  
  public int glyphId(int paramInt)
  {
    return 0;
  }
  
  public int language()
  {
    return 0;
  }
  
  public Iterator<Integer> iterator()
  {
    return null;
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat14>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format14, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format14, paramCMapId);
    }
    
    protected CMapFormat14 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat14(paramReadableFontData, cmapId());
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat14.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */