package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CMapFormat6
  extends CMap
{
  private final int firstCode = this.data.readUShort(CMapTable.Offset.format6FirstCode.offset);
  private final int entryCount = this.data.readUShort(CMapTable.Offset.format6EntryCount.offset);
  
  protected CMapFormat6(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format6.value, paramCMapId);
  }
  
  public int glyphId(int paramInt)
  {
    if ((paramInt < this.firstCode) || (paramInt >= this.firstCode + this.entryCount)) {
      return 0;
    }
    return this.data.readUShort(CMapTable.Offset.format6GlyphIdArray.offset + (paramInt - this.firstCode) * FontData.DataSize.USHORT.size());
  }
  
  public int language()
  {
    return this.data.readUShort(CMapTable.Offset.format6Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat6>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format6, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format6, paramCMapId);
    }
    
    protected CMapFormat6 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat6(paramReadableFontData, cmapId());
    }
  }
  
  private class CharacterIterator
    implements Iterator<Integer>
  {
    private int character = CMapFormat6.this.firstCode;
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      return this.character < CMapFormat6.this.firstCode + CMapFormat6.this.entryCount;
    }
    
    public Integer next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException("No more characters to iterate.");
      }
      return Integer.valueOf(this.character++);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Unable to remove a character from cmap.");
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat6.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */