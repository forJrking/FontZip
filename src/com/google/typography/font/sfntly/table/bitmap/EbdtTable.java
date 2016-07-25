package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTableContainerTable;
import com.google.typography.font.sfntly.table.SubTableContainerTable.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public final class EbdtTable
  extends SubTableContainerTable
{
  protected EbdtTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int version()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public BitmapGlyph glyph(int paramInt1, int paramInt2, int paramInt3)
  {
    ReadableFontData localReadableFontData = this.data.slice(paramInt1, paramInt2);
    return BitmapGlyph.createGlyph(localReadableFontData, paramInt3);
  }
  
  public static class Builder
    extends SubTableContainerTable.Builder<EbdtTable>
  {
    private final int version = 131072;
    private List<Map<Integer, BitmapGlyphInfo>> glyphLoca;
    private List<Map<Integer, BitmapGlyph.Builder<? extends BitmapGlyph>>> glyphBuilders;
    
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    public static Builder createBuilder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      return new Builder(paramHeader, paramReadableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    public void setLoca(List<Map<Integer, BitmapGlyphInfo>> paramList)
    {
      revert();
      this.glyphLoca = paramList;
    }
    
    public List<Map<Integer, BitmapGlyphInfo>> generateLocaList()
    {
      if (this.glyphBuilders == null)
      {
        if (this.glyphLoca == null) {
          return new ArrayList(0);
        }
        return this.glyphLoca;
      }
      ArrayList localArrayList = new ArrayList(this.glyphBuilders.size());
      int i = EbdtTable.Offset.headerLength.offset;
      Iterator localIterator1 = this.glyphBuilders.iterator();
      while (localIterator1.hasNext())
      {
        Map localMap = (Map)localIterator1.next();
        TreeMap localTreeMap = new TreeMap();
        int j = 0;
        Iterator localIterator2 = localMap.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator2.next();
          BitmapGlyph.Builder localBuilder = (BitmapGlyph.Builder)localEntry.getValue();
          int k = localBuilder.subDataSizeToSerialize();
          BitmapGlyphInfo localBitmapGlyphInfo = new BitmapGlyphInfo(((Integer)localEntry.getKey()).intValue(), i + j, k, localBuilder.format());
          localTreeMap.put(localEntry.getKey(), localBitmapGlyphInfo);
          j += k;
        }
        i += j;
        localArrayList.add(Collections.unmodifiableMap(localTreeMap));
      }
      return Collections.unmodifiableList(localArrayList);
    }
    
    public List<Map<Integer, BitmapGlyph.Builder<? extends BitmapGlyph>>> glyphBuilders()
    {
      return getGlyphBuilders();
    }
    
    public void setGlyphBuilders(List<Map<Integer, BitmapGlyph.Builder<? extends BitmapGlyph>>> paramList)
    {
      this.glyphBuilders = paramList;
      setModelChanged();
    }
    
    private List<Map<Integer, BitmapGlyph.Builder<? extends BitmapGlyph>>> getGlyphBuilders()
    {
      if (this.glyphBuilders == null)
      {
        if (this.glyphLoca == null) {
          throw new IllegalStateException("Loca values not set - unable to parse glyph data.");
        }
        this.glyphBuilders = initialize(internalReadData(), this.glyphLoca);
        setModelChanged();
      }
      return this.glyphBuilders;
    }
    
    public void revert()
    {
      this.glyphLoca = null;
      this.glyphBuilders = null;
      setModelChanged(false);
    }
    
    private static List<Map<Integer, BitmapGlyph.Builder<? extends BitmapGlyph>>> initialize(ReadableFontData paramReadableFontData, List<Map<Integer, BitmapGlyphInfo>> paramList)
    {
      ArrayList localArrayList = new ArrayList(paramList.size());
      if (paramReadableFontData != null)
      {
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext())
        {
          Map localMap = (Map)localIterator1.next();
          TreeMap localTreeMap = new TreeMap();
          Iterator localIterator2 = localMap.entrySet().iterator();
          while (localIterator2.hasNext())
          {
            Map.Entry localEntry = (Map.Entry)localIterator2.next();
            BitmapGlyphInfo localBitmapGlyphInfo = (BitmapGlyphInfo)localEntry.getValue();
            BitmapGlyph.Builder localBuilder = BitmapGlyph.Builder.createGlyphBuilder(paramReadableFontData.slice(localBitmapGlyphInfo.offset(), localBitmapGlyphInfo.length()), localBitmapGlyphInfo.format());
            localTreeMap.put(localEntry.getKey(), localBuilder);
          }
          localArrayList.add(localTreeMap);
        }
      }
      return localArrayList;
    }
    
    protected EbdtTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new EbdtTable(header(), paramReadableFontData);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if ((this.glyphBuilders == null) || (this.glyphBuilders.size() == 0)) {
        return 0;
      }
      int i = 1;
      int j = EbdtTable.Offset.headerLength.offset;
      Iterator localIterator1 = this.glyphBuilders.iterator();
      while (localIterator1.hasNext())
      {
        Map localMap = (Map)localIterator1.next();
        TreeMap localTreeMap = new TreeMap();
        Iterator localIterator2 = localMap.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator2.next();
          BitmapGlyph.Builder localBuilder = (BitmapGlyph.Builder)localEntry.getValue();
          int k = localBuilder.subDataSizeToSerialize();
          j += Math.abs(k);
          i = k <= 0 ? 0 : i;
        }
      }
      return (i != 0 ? 1 : -1) * j;
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.glyphBuilders != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = 0;
      getClass();
      i += paramWritableFontData.writeFixed(EbdtTable.Offset.version.offset, 131072);
      Iterator localIterator1 = this.glyphBuilders.iterator();
      while (localIterator1.hasNext())
      {
        Map localMap = (Map)localIterator1.next();
        TreeMap localTreeMap = new TreeMap();
        Iterator localIterator2 = localMap.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator2.next();
          BitmapGlyph.Builder localBuilder = (BitmapGlyph.Builder)localEntry.getValue();
          i += localBuilder.subSerialize(paramWritableFontData.slice(i));
        }
      }
      return i;
    }
  }
  
  protected static enum Offset
  {
    version(0),  headerLength(FontData.DataSize.Fixed.size());
    
    protected final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\EbdtTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */