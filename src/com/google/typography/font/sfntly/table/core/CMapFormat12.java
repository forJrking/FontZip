package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CMapFormat12
  extends CMap
{
  private final int numberOfGroups = this.data.readULongAsInt(CMapTable.Offset.format12nGroups.offset);
  
  protected CMapFormat12(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format12.value, paramCMapId);
  }
  
  private int groupStartChar(int paramInt)
  {
    return this.data.readULongAsInt(CMapTable.Offset.format12Groups.offset + paramInt * CMapTable.Offset.format12Groups_structLength.offset + CMapTable.Offset.format12_startCharCode.offset);
  }
  
  private int groupEndChar(int paramInt)
  {
    return this.data.readULongAsInt(CMapTable.Offset.format12Groups.offset + paramInt * CMapTable.Offset.format12Groups_structLength.offset + CMapTable.Offset.format12_endCharCode.offset);
  }
  
  private int groupStartGlyph(int paramInt)
  {
    return this.data.readULongAsInt(CMapTable.Offset.format12Groups.offset + paramInt * CMapTable.Offset.format12Groups_structLength.offset + CMapTable.Offset.format12_startGlyphId.offset);
  }
  
  public int glyphId(int paramInt)
  {
    int i = this.data.searchULong(CMapTable.Offset.format12Groups.offset + CMapTable.Offset.format12_startCharCode.offset, CMapTable.Offset.format12Groups_structLength.offset, CMapTable.Offset.format12Groups.offset + CMapTable.Offset.format12_endCharCode.offset, CMapTable.Offset.format12Groups_structLength.offset, this.numberOfGroups, paramInt);
    if (i == -1) {
      return 0;
    }
    return groupStartGlyph(i) + (paramInt - groupStartChar(i));
  }
  
  public int language()
  {
    return this.data.readULongAsInt(CMapTable.Offset.format12Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat12>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format12, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format12, paramCMapId);
    }
    
    protected CMapFormat12 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat12(paramReadableFontData, cmapId());
    }
  }
  
  private final class CharacterIterator
    implements Iterator<Integer>
  {
    private int groupIndex = 0;
    private int groupEndChar = CMapFormat12.this.groupEndChar(this.groupIndex);
    private boolean nextSet = false;
    private int nextChar = CMapFormat12.this.groupStartChar(this.groupIndex);
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      if (this.nextSet) {
        return true;
      }
      if (this.groupIndex >= CMapFormat12.this.numberOfGroups) {
        return false;
      }
      if (this.nextChar < this.groupEndChar)
      {
        this.nextChar += 1;
        this.nextSet = true;
        return true;
      }
      this.groupIndex += 1;
      if (this.groupIndex < CMapFormat12.this.numberOfGroups)
      {
        this.nextSet = true;
        this.nextChar = CMapFormat12.this.groupStartChar(this.groupIndex);
        this.groupEndChar = CMapFormat12.this.groupEndChar(this.groupIndex);
        return true;
      }
      return false;
    }
    
    public Integer next()
    {
      if ((!this.nextSet) && (!hasNext())) {
        throw new NoSuchElementException("No more characters to iterate.");
      }
      this.nextSet = false;
      return Integer.valueOf(this.nextChar);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Unable to remove a character from cmap.");
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat12.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */