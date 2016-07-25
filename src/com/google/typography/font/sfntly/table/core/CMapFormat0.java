package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CMapFormat0
  extends CMap
{
  protected CMapFormat0(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format0.value, paramCMapId);
  }
  
  public int glyphId(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 255)) {
      return 0;
    }
    return this.data.readUByte(paramInt + CMapTable.Offset.format0GlyphIdArray.offset);
  }
  
  public int language()
  {
    return this.data.readUShort(CMapTable.Offset.format0Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat0>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format0, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format0, paramCMapId);
    }
    
    protected CMapFormat0 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat0(paramReadableFontData, cmapId());
    }
  }
  
  private class CharacterIterator
    implements Iterator<Integer>
  {
    int character = 0;
    protected static final int MAX_CHARACTER = 255;
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      return this.character <= 255;
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


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat0.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */