package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CMapFormat8
  extends CMap
{
  private final int numberOfGroups = this.data.readULongAsInt(CMapTable.Offset.format8nGroups.offset);
  
  protected CMapFormat8(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format8.value, paramCMapId);
  }
  
  private int firstChar(int paramInt)
  {
    return readFontData().readULongAsInt(CMapTable.Offset.format8Groups.offset + paramInt * CMapTable.Offset.format8Group_structLength.offset + CMapTable.Offset.format8Group_startCharCode.offset);
  }
  
  private int endChar(int paramInt)
  {
    return readFontData().readULongAsInt(CMapTable.Offset.format8Groups.offset + paramInt * CMapTable.Offset.format8Group_structLength.offset + CMapTable.Offset.format8Group_endCharCode.offset);
  }
  
  public int glyphId(int paramInt)
  {
    return readFontData().searchULong(CMapTable.Offset.format8Groups.offset + CMapTable.Offset.format8Group_startCharCode.offset, CMapTable.Offset.format8Group_structLength.offset, CMapTable.Offset.format8Groups.offset + CMapTable.Offset.format8Group_endCharCode.offset, CMapTable.Offset.format8Group_structLength.offset, this.numberOfGroups, paramInt);
  }
  
  public int language()
  {
    return this.data.readULongAsInt(CMapTable.Offset.format8Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat8>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format8, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format8, paramCMapId);
    }
    
    protected CMapFormat8 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat8(paramReadableFontData, cmapId());
    }
  }
  
  private class CharacterIterator
    implements Iterator<Integer>
  {
    private int groupIndex = 0;
    private int firstCharInGroup = -1;
    private int endCharInGroup;
    private int nextChar;
    private boolean nextCharSet;
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      if (this.nextCharSet == true) {
        return true;
      }
      while (this.groupIndex < CMapFormat8.this.numberOfGroups)
      {
        if (this.firstCharInGroup < 0)
        {
          this.firstCharInGroup = CMapFormat8.this.firstChar(this.groupIndex);
          this.endCharInGroup = CMapFormat8.this.endChar(this.groupIndex);
          this.nextChar = this.firstCharInGroup;
          this.nextCharSet = true;
          return true;
        }
        if (this.nextChar < this.endCharInGroup)
        {
          this.nextChar += 1;
          this.nextCharSet = true;
          return true;
        }
        this.groupIndex += 1;
        this.firstCharInGroup = -1;
      }
      return false;
    }
    
    public Integer next()
    {
      if ((!this.nextCharSet) && (!hasNext())) {
        throw new NoSuchElementException("No more characters to iterate.");
      }
      this.nextCharSet = false;
      return Integer.valueOf(this.nextChar);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Unable to remove a character from cmap.");
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */