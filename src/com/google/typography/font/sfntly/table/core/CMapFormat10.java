package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CMapFormat10
  extends CMap
{
  private final int startCharCode = this.data.readULongAsInt(CMapTable.Offset.format10StartCharCode.offset);
  private final int numChars = this.data.readUShort(CMapTable.Offset.format10NumChars.offset);
  
  protected CMapFormat10(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format10.value, paramCMapId);
  }
  
  public int glyphId(int paramInt)
  {
    if ((paramInt < this.startCharCode) || (paramInt >= this.startCharCode + this.numChars)) {
      return 0;
    }
    return readFontData().readUShort(paramInt - this.startCharCode);
  }
  
  public int language()
  {
    return this.data.readULongAsInt(CMapTable.Offset.format10Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat10>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format10, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format10, paramCMapId);
    }
    
    protected CMapFormat10 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat10(paramReadableFontData, cmapId());
    }
  }
  
  private class CharacterIterator
    implements Iterator<Integer>
  {
    private int character = CMapFormat10.this.startCharCode;
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      return this.character < CMapFormat10.this.startCharCode + CMapFormat10.this.numChars;
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


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat10.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */