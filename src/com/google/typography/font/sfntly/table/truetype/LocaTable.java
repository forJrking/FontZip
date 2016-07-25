package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.Table.Builder;
import com.google.typography.font.sfntly.table.core.FontHeaderTable.IndexToLocFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class LocaTable
  extends Table
{
  private FontHeaderTable.IndexToLocFormat version;
  private int numGlyphs;
  
  private LocaTable(Header paramHeader, ReadableFontData paramReadableFontData, FontHeaderTable.IndexToLocFormat paramIndexToLocFormat, int paramInt)
  {
    super(paramHeader, paramReadableFontData);
    this.version = paramIndexToLocFormat;
    this.numGlyphs = paramInt;
  }
  
  public FontHeaderTable.IndexToLocFormat formatVersion()
  {
    return this.version;
  }
  
  public int numGlyphs()
  {
    return this.numGlyphs;
  }
  
  public int glyphOffset(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.numGlyphs)) {
      throw new IndexOutOfBoundsException("Glyph ID is out of bounds.");
    }
    return loca(paramInt);
  }
  
  public int glyphLength(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.numGlyphs)) {
      throw new IndexOutOfBoundsException("Glyph ID is out of bounds.");
    }
    return loca(paramInt + 1) - loca(paramInt);
  }
  
  public int numLocas()
  {
    return this.numGlyphs + 1;
  }
  
  public int loca(int paramInt)
  {
    if (paramInt > this.numGlyphs) {
      throw new IndexOutOfBoundsException();
    }
    if (this.version == FontHeaderTable.IndexToLocFormat.shortOffset) {
      return 2 * this.data.readUShort(paramInt * FontData.DataSize.USHORT.size());
    }
    return this.data.readULongAsInt(paramInt * FontData.DataSize.ULONG.size());
  }
  
  Iterator<Integer> iterator()
  {
    return new LocaIterator(null);
  }
  
  public static class Builder
    extends Table.Builder<LocaTable>
  {
    private FontHeaderTable.IndexToLocFormat formatVersion = FontHeaderTable.IndexToLocFormat.longOffset;
    private int numGlyphs = -1;
    private List<Integer> loca;
    
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    private Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    private Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      clearLoca(false);
      if (this.loca == null) {
        this.loca = new ArrayList();
      }
      if (paramReadableFontData != null)
      {
        if (this.numGlyphs < 0) {
          throw new IllegalStateException("numglyphs not set on LocaTable Builder.");
        }
        LocaTable localLocaTable = new LocaTable(header(), paramReadableFontData, this.formatVersion, this.numGlyphs, null);
        Iterator localIterator = localLocaTable.iterator();
        while (localIterator.hasNext()) {
          this.loca.add(localIterator.next());
        }
      }
    }
    
    private int checkGlyphRange(int paramInt)
    {
      if ((paramInt < 0) || (paramInt > lastGlyphIndex())) {
        throw new IndexOutOfBoundsException("Glyph ID is outside of the allowed range.");
      }
      return paramInt;
    }
    
    private int lastGlyphIndex()
    {
      return this.loca != null ? this.loca.size() - 2 : this.numGlyphs - 1;
    }
    
    private List<Integer> getLocaList()
    {
      if (this.loca == null)
      {
        initialize(internalReadData());
        setModelChanged();
      }
      return this.loca;
    }
    
    private void clearLoca(boolean paramBoolean)
    {
      if (this.loca != null) {
        this.loca.clear();
      }
      if (paramBoolean) {
        this.loca = null;
      }
      setModelChanged(false);
    }
    
    public FontHeaderTable.IndexToLocFormat formatVersion()
    {
      return this.formatVersion;
    }
    
    public void setFormatVersion(FontHeaderTable.IndexToLocFormat paramIndexToLocFormat)
    {
      this.formatVersion = paramIndexToLocFormat;
    }
    
    public List<Integer> locaList()
    {
      return getLocaList();
    }
    
    public void setLocaList(List<Integer> paramList)
    {
      this.loca = paramList;
      setModelChanged();
    }
    
    public int glyphOffset(int paramInt)
    {
      checkGlyphRange(paramInt);
      return ((Integer)getLocaList().get(paramInt)).intValue();
    }
    
    public int glyphLength(int paramInt)
    {
      checkGlyphRange(paramInt);
      return ((Integer)getLocaList().get(paramInt + 1)).intValue() - ((Integer)getLocaList().get(paramInt)).intValue();
    }
    
    public void setNumGlyphs(int paramInt)
    {
      this.numGlyphs = paramInt;
    }
    
    public int numGlyphs()
    {
      return lastGlyphIndex() + 1;
    }
    
    public void revert()
    {
      this.loca = null;
      setModelChanged(false);
    }
    
    public int numLocas()
    {
      return getLocaList().size();
    }
    
    public int loca(int paramInt)
    {
      return ((Integer)getLocaList().get(paramInt)).intValue();
    }
    
    protected LocaTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new LocaTable(header(), paramReadableFontData, this.formatVersion, this.numGlyphs, null);
    }
    
    protected void subDataSet()
    {
      initialize(internalReadData());
    }
    
    protected int subDataSizeToSerialize()
    {
      if (this.loca == null) {
        return 0;
      }
      if (this.formatVersion == FontHeaderTable.IndexToLocFormat.longOffset) {
        return this.loca.size() * FontData.DataSize.ULONG.size();
      }
      return this.loca.size() * FontData.DataSize.USHORT.size();
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.loca != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = 0;
      Iterator localIterator = this.loca.iterator();
      while (localIterator.hasNext())
      {
        int j = ((Integer)localIterator.next()).intValue();
        if (this.formatVersion == FontHeaderTable.IndexToLocFormat.longOffset) {
          i += paramWritableFontData.writeULong(i, j);
        } else {
          i += paramWritableFontData.writeUShort(i, j / 2);
        }
      }
      this.numGlyphs = (this.loca.size() - 1);
      return i;
    }
  }
  
  private final class LocaIterator
    implements Iterator<Integer>
  {
    int index;
    
    private LocaIterator() {}
    
    public boolean hasNext()
    {
      return this.index <= LocaTable.this.numGlyphs;
    }
    
    public Integer next()
    {
      return Integer.valueOf(LocaTable.this.loca(this.index++));
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\LocaTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */