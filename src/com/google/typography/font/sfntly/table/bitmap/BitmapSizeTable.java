package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.math.FontMath;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class BitmapSizeTable
  extends SubTable
{
  private static final boolean USE_BINARY_SEARCH = false;
  private final Object indexSubTablesLock = new Object();
  private volatile List<IndexSubTable> indexSubTables = null;
  
  protected BitmapSizeTable(ReadableFontData paramReadableFontData1, ReadableFontData paramReadableFontData2)
  {
    super(paramReadableFontData1, paramReadableFontData2);
  }
  
  public int indexSubTableArrayOffset()
  {
    return this.data.readULongAsInt(EblcTable.Offset.bitmapSizeTable_indexSubTableArrayOffset.offset);
  }
  
  public int indexTableSize()
  {
    return this.data.readULongAsInt(EblcTable.Offset.bitmapSizeTable_indexTableSize.offset);
  }
  
  private static int numberOfIndexSubTables(ReadableFontData paramReadableFontData, int paramInt)
  {
    return paramReadableFontData.readULongAsInt(paramInt + EblcTable.Offset.bitmapSizeTable_numberOfIndexSubTables.offset);
  }
  
  public int numberOfIndexSubTables()
  {
    return numberOfIndexSubTables(this.data, 0);
  }
  
  public int colorRef()
  {
    return this.data.readULongAsInt(EblcTable.Offset.bitmapSizeTable_colorRef.offset);
  }
  
  public void hori() {}
  
  public void vert() {}
  
  public int startGlyphIndex()
  {
    return this.data.readUShort(EblcTable.Offset.bitmapSizeTable_startGlyphIndex.offset);
  }
  
  public int endGlyphIndex()
  {
    return this.data.readUShort(EblcTable.Offset.bitmapSizeTable_endGlyphIndex.offset);
  }
  
  public int ppemX()
  {
    return this.data.readByte(EblcTable.Offset.bitmapSizeTable_ppemX.offset);
  }
  
  public int ppemY()
  {
    return this.data.readByte(EblcTable.Offset.bitmapSizeTable_ppemY.offset);
  }
  
  public int bitDepth()
  {
    return this.data.readByte(EblcTable.Offset.bitmapSizeTable_bitDepth.offset);
  }
  
  public int flagsAsInt()
  {
    return this.data.readChar(EblcTable.Offset.bitmapSizeTable_flags.offset);
  }
  
  public IndexSubTable indexSubTable(int paramInt)
  {
    List localList = getIndexSubTableList();
    return (IndexSubTable)localList.get(paramInt);
  }
  
  public BitmapGlyphInfo glyphInfo(int paramInt)
  {
    IndexSubTable localIndexSubTable = searchIndexSubTables(paramInt);
    if (localIndexSubTable == null) {
      return null;
    }
    return localIndexSubTable.glyphInfo(paramInt);
  }
  
  public int glyphOffset(int paramInt)
  {
    IndexSubTable localIndexSubTable = searchIndexSubTables(paramInt);
    if (localIndexSubTable == null) {
      return -1;
    }
    return localIndexSubTable.glyphOffset(paramInt);
  }
  
  public int glyphLength(int paramInt)
  {
    IndexSubTable localIndexSubTable = searchIndexSubTables(paramInt);
    if (localIndexSubTable == null) {
      return -1;
    }
    return localIndexSubTable.glyphLength(paramInt);
  }
  
  public int glyphFormat(int paramInt)
  {
    IndexSubTable localIndexSubTable = searchIndexSubTables(paramInt);
    if (localIndexSubTable == null) {
      return -1;
    }
    return localIndexSubTable.imageFormat();
  }
  
  private IndexSubTable searchIndexSubTables(int paramInt)
  {
    return linearSearchIndexSubTables(paramInt);
  }
  
  private IndexSubTable linearSearchIndexSubTables(int paramInt)
  {
    Iterator localIterator = getIndexSubTableList().iterator();
    while (localIterator.hasNext())
    {
      IndexSubTable localIndexSubTable = (IndexSubTable)localIterator.next();
      if ((localIndexSubTable.firstGlyphIndex() <= paramInt) && (localIndexSubTable.lastGlyphIndex() >= paramInt)) {
        return localIndexSubTable;
      }
    }
    return null;
  }
  
  private IndexSubTable binarySearchIndexSubTables(int paramInt)
  {
    List localList = getIndexSubTableList();
    int i = 0;
    int j = 0;
    int k = localList.size();
    while (k != j)
    {
      i = (k + j) / 2;
      IndexSubTable localIndexSubTable = (IndexSubTable)localList.get(i);
      if (paramInt < localIndexSubTable.firstGlyphIndex())
      {
        k = i;
      }
      else
      {
        if (paramInt <= localIndexSubTable.lastGlyphIndex()) {
          return localIndexSubTable;
        }
        j = i + 1;
      }
    }
    return null;
  }
  
  private IndexSubTable createIndexSubTable(int paramInt)
  {
    return IndexSubTable.createIndexSubTable(masterReadData(), indexSubTableArrayOffset(), paramInt);
  }
  
  private List<IndexSubTable> getIndexSubTableList()
  {
    if (this.indexSubTables == null) {
      synchronized (this.indexSubTablesLock)
      {
        if (this.indexSubTables == null)
        {
          ArrayList localArrayList = new ArrayList(numberOfIndexSubTables());
          for (int i = 0; i < numberOfIndexSubTables(); i++) {
            localArrayList.add(createIndexSubTable(i));
          }
          this.indexSubTables = localArrayList;
        }
      }
    }
    return this.indexSubTables;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("BitmapSizeTable: ");
    List localList = getIndexSubTableList();
    localStringBuilder.append("[s=0x");
    localStringBuilder.append(Integer.toHexString(startGlyphIndex()));
    localStringBuilder.append(", e=0x");
    localStringBuilder.append(Integer.toHexString(endGlyphIndex()));
    localStringBuilder.append(", ppemx=");
    localStringBuilder.append(ppemX());
    localStringBuilder.append(", index subtables count=");
    localStringBuilder.append(numberOfIndexSubTables());
    localStringBuilder.append("]");
    for (int i = 0; i < localList.size(); i++)
    {
      localStringBuilder.append("\n\t");
      localStringBuilder.append(i);
      localStringBuilder.append(": ");
      localStringBuilder.append(localList.get(i));
      localStringBuilder.append(", ");
    }
    localStringBuilder.append("\n");
    return localStringBuilder.toString();
  }
  
  public static final class Builder
    extends SubTable.Builder<BitmapSizeTable>
  {
    List<IndexSubTable.Builder<? extends IndexSubTable>> indexSubTables;
    
    static Builder createBuilder(WritableFontData paramWritableFontData, ReadableFontData paramReadableFontData)
    {
      return new Builder(paramWritableFontData, paramReadableFontData);
    }
    
    static Builder createBuilder(ReadableFontData paramReadableFontData1, ReadableFontData paramReadableFontData2)
    {
      return new Builder(paramReadableFontData1, paramReadableFontData2);
    }
    
    private Builder(WritableFontData paramWritableFontData, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    private Builder(ReadableFontData paramReadableFontData1, ReadableFontData paramReadableFontData2)
    {
      super(paramReadableFontData2);
    }
    
    public int indexSubTableArrayOffset()
    {
      return internalReadData().readULongAsInt(EblcTable.Offset.bitmapSizeTable_indexSubTableArrayOffset.offset);
    }
    
    void setIndexSubTableArrayOffset(int paramInt)
    {
      internalWriteData().writeULong(EblcTable.Offset.bitmapSizeTable_indexSubTableArrayOffset.offset, paramInt);
    }
    
    public int indexTableSize()
    {
      return internalReadData().readULongAsInt(EblcTable.Offset.bitmapSizeTable_indexTableSize.offset);
    }
    
    void setIndexTableSize(int paramInt)
    {
      internalWriteData().writeULong(EblcTable.Offset.bitmapSizeTable_indexTableSize.offset, paramInt);
    }
    
    public int numberOfIndexSubTables()
    {
      return getIndexSubTableBuilders().size();
    }
    
    private void setNumberOfIndexSubTables(int paramInt)
    {
      internalWriteData().writeULong(EblcTable.Offset.bitmapSizeTable_numberOfIndexSubTables.offset, paramInt);
    }
    
    public int colorRef()
    {
      return internalReadData().readULongAsInt(EblcTable.Offset.bitmapSizeTable_colorRef.offset);
    }
    
    public void hori() {}
    
    public void vert() {}
    
    public int startGlyphIndex()
    {
      return internalReadData().readUShort(EblcTable.Offset.bitmapSizeTable_startGlyphIndex.offset);
    }
    
    public int endGlyphIndex()
    {
      return internalReadData().readUShort(EblcTable.Offset.bitmapSizeTable_endGlyphIndex.offset);
    }
    
    public int ppemX()
    {
      return internalReadData().readByte(EblcTable.Offset.bitmapSizeTable_ppemX.offset);
    }
    
    public int ppemY()
    {
      return internalReadData().readByte(EblcTable.Offset.bitmapSizeTable_ppemY.offset);
    }
    
    public int bitDepth()
    {
      return internalReadData().readByte(EblcTable.Offset.bitmapSizeTable_bitDepth.offset);
    }
    
    public int flagsAsInt()
    {
      return internalReadData().readChar(EblcTable.Offset.bitmapSizeTable_flags.offset);
    }
    
    public IndexSubTable.Builder<? extends IndexSubTable> indexSubTableBuilder(int paramInt)
    {
      List localList = getIndexSubTableBuilders();
      return (IndexSubTable.Builder)localList.get(paramInt);
    }
    
    public BitmapGlyphInfo glyphInfo(int paramInt)
    {
      IndexSubTable.Builder localBuilder = searchIndexSubTables(paramInt);
      if (localBuilder == null) {
        return null;
      }
      return localBuilder.glyphInfo(paramInt);
    }
    
    public int glyphOffset(int paramInt)
    {
      IndexSubTable.Builder localBuilder = searchIndexSubTables(paramInt);
      if (localBuilder == null) {
        return -1;
      }
      return localBuilder.glyphOffset(paramInt);
    }
    
    public int glyphLength(int paramInt)
    {
      IndexSubTable.Builder localBuilder = searchIndexSubTables(paramInt);
      if (localBuilder == null) {
        return -1;
      }
      return localBuilder.glyphLength(paramInt);
    }
    
    public int glyphFormat(int paramInt)
    {
      IndexSubTable.Builder localBuilder = searchIndexSubTables(paramInt);
      if (localBuilder == null) {
        return -1;
      }
      return localBuilder.imageFormat();
    }
    
    public List<IndexSubTable.Builder<? extends IndexSubTable>> indexSubTableBuilders()
    {
      return getIndexSubTableBuilders();
    }
    
    Iterator<BitmapGlyphInfo> iterator()
    {
      return new BitmapGlyphInfoIterator();
    }
    
    protected void revert()
    {
      this.indexSubTables = null;
      setModelChanged(false);
    }
    
    public Map<Integer, BitmapGlyphInfo> generateLocaMap()
    {
      HashMap localHashMap = new HashMap();
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
      {
        BitmapGlyphInfo localBitmapGlyphInfo = (BitmapGlyphInfo)localIterator.next();
        localHashMap.put(Integer.valueOf(localBitmapGlyphInfo.glyphId()), localBitmapGlyphInfo);
      }
      return localHashMap;
    }
    
    private IndexSubTable.Builder<? extends IndexSubTable> searchIndexSubTables(int paramInt)
    {
      return linearSearchIndexSubTables(paramInt);
    }
    
    private IndexSubTable.Builder<? extends IndexSubTable> linearSearchIndexSubTables(int paramInt)
    {
      List localList = getIndexSubTableBuilders();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        IndexSubTable.Builder localBuilder = (IndexSubTable.Builder)localIterator.next();
        if ((localBuilder.firstGlyphIndex() <= paramInt) && (localBuilder.lastGlyphIndex() >= paramInt)) {
          return localBuilder;
        }
      }
      return null;
    }
    
    private IndexSubTable.Builder<? extends IndexSubTable> binarySearchIndexSubTables(int paramInt)
    {
      List localList = getIndexSubTableBuilders();
      int i = 0;
      int j = 0;
      int k = localList.size();
      while (k != j)
      {
        i = (k + j) / 2;
        IndexSubTable.Builder localBuilder = (IndexSubTable.Builder)localList.get(i);
        if (paramInt < localBuilder.firstGlyphIndex())
        {
          k = i;
        }
        else
        {
          if (paramInt <= localBuilder.lastGlyphIndex()) {
            return localBuilder;
          }
          j = i + 1;
        }
      }
      return null;
    }
    
    private List<IndexSubTable.Builder<? extends IndexSubTable>> getIndexSubTableBuilders()
    {
      if (this.indexSubTables == null)
      {
        initialize(internalReadData());
        setModelChanged();
      }
      return this.indexSubTables;
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      if (this.indexSubTables == null) {
        this.indexSubTables = new ArrayList();
      } else {
        this.indexSubTables.clear();
      }
      if (paramReadableFontData != null)
      {
        int i = BitmapSizeTable.numberOfIndexSubTables(paramReadableFontData, 0);
        for (int j = 0; j < i; j++) {
          this.indexSubTables.add(createIndexSubTableBuilder(j));
        }
      }
    }
    
    private IndexSubTable.Builder<? extends IndexSubTable> createIndexSubTableBuilder(int paramInt)
    {
      return IndexSubTable.Builder.createBuilder(masterReadData(), indexSubTableArrayOffset(), paramInt);
    }
    
    protected BitmapSizeTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new BitmapSizeTable(paramReadableFontData, masterReadData());
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if (indexSubTableBuilders() == null) {
        return 0;
      }
      int i = EblcTable.Offset.bitmapSizeTableLength.offset;
      int j = 0;
      Iterator localIterator = this.indexSubTables.iterator();
      while (localIterator.hasNext())
      {
        IndexSubTable.Builder localBuilder = (IndexSubTable.Builder)localIterator.next();
        i += EblcTable.Offset.indexSubTableEntryLength.offset;
        int k = localBuilder.subDataSizeToSerialize();
        int m = FontMath.paddingRequired(Math.abs(k), FontData.DataSize.ULONG.size());
        j = k > 0 ? j : 1;
        i += Math.abs(k) + m;
      }
      return j != 0 ? -i : i;
    }
    
    protected boolean subReadyToSerialize()
    {
      return indexSubTableBuilders() != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      setNumberOfIndexSubTables(indexSubTableBuilders().size());
      int i = internalReadData().copyTo(paramWritableFontData);
      return i;
    }
    
    private class BitmapGlyphInfoIterator
      implements Iterator<BitmapGlyphInfo>
    {
      Iterator<IndexSubTable.Builder<? extends IndexSubTable>> subTableIter = BitmapSizeTable.Builder.this.getIndexSubTableBuilders().iterator();
      Iterator<BitmapGlyphInfo> subTableGlyphInfoIter;
      
      public BitmapGlyphInfoIterator() {}
      
      public boolean hasNext()
      {
        if ((this.subTableGlyphInfoIter != null) && (this.subTableGlyphInfoIter.hasNext())) {
          return true;
        }
        while (this.subTableIter.hasNext())
        {
          IndexSubTable.Builder localBuilder = (IndexSubTable.Builder)this.subTableIter.next();
          this.subTableGlyphInfoIter = localBuilder.iterator();
          if (this.subTableGlyphInfoIter.hasNext()) {
            return true;
          }
        }
        return false;
      }
      
      public BitmapGlyphInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException("No more characters to iterate.");
        }
        return (BitmapGlyphInfo)this.subTableGlyphInfoIter.next();
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException("Unable to remove a glyph info.");
      }
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\BitmapSizeTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */