package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.Font.MacintoshEncodingId;
import com.google.typography.font.sfntly.Font.PlatformId;
import com.google.typography.font.sfntly.Font.WindowsEncodingId;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTableContainerTable;
import com.google.typography.font.sfntly.table.SubTableContainerTable.Builder;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class CMapTable
  extends SubTableContainerTable
  implements Iterable<CMap>
{
  public static final int NOTDEF = 0;
  
  private CMapTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int version()
  {
    return this.data.readUShort(Offset.version.offset);
  }
  
  public int numCMaps()
  {
    return this.data.readUShort(Offset.numTables.offset);
  }
  
  public int getCmapIndex(CMapId paramCMapId)
  {
    for (int i = 0; i < numCMaps(); i++) {
      if (paramCMapId.equals(cmapId(i))) {
        return i;
      }
    }
    return -1;
  }
  
  private static int offsetForEncodingRecord(int paramInt)
  {
    return Offset.encodingRecordStart.offset + paramInt * Offset.encodingRecordSize.offset;
  }
  
  public CMapId cmapId(int paramInt)
  {
    return CMapId.getInstance(platformId(paramInt), encodingId(paramInt));
  }
  
  public int platformId(int paramInt)
  {
    return this.data.readUShort(Offset.encodingRecordPlatformId.offset + offsetForEncodingRecord(paramInt));
  }
  
  public int encodingId(int paramInt)
  {
    return this.data.readUShort(Offset.encodingRecordEncodingId.offset + offsetForEncodingRecord(paramInt));
  }
  
  public int offset(int paramInt)
  {
    return this.data.readULongAsInt(Offset.encodingRecordOffset.offset + offsetForEncodingRecord(paramInt));
  }
  
  public Iterator<CMap> iterator()
  {
    return new CMapIterator(null);
  }
  
  public Iterator<CMap> iterator(CMapFilter paramCMapFilter)
  {
    return new CMapIterator(paramCMapFilter, null);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    localStringBuilder.append(" = { ");
    for (int i = 0; i < numCMaps(); i++)
    {
      CMap localCMap;
      try
      {
        localCMap = cmap(i);
      }
      catch (IOException localIOException)
      {
        continue;
      }
      localStringBuilder.append("[0x");
      localStringBuilder.append(Integer.toHexString(offset(i)));
      localStringBuilder.append(" = ");
      localStringBuilder.append(localCMap);
      if (i < numCMaps() - 1) {
        localStringBuilder.append("], ");
      } else {
        localStringBuilder.append("]");
      }
    }
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public CMap cmap(int paramInt)
    throws IOException
  {
    CMap.Builder localBuilder = Builder.cmapBuilder(readFontData(), paramInt);
    return (CMap)localBuilder.build();
  }
  
  public CMap cmap(int paramInt1, int paramInt2)
  {
    return cmap(CMapId.getInstance(paramInt1, paramInt2));
  }
  
  public CMap cmap(final CMapId paramCMapId)
  {
    Iterator localIterator = iterator(new CMapFilter()
    {
      public boolean accept(CMapTable.CMapId paramAnonymousCMapId)
      {
        return paramCMapId.equals(paramAnonymousCMapId);
      }
    });
    if (localIterator.hasNext()) {
      return (CMap)localIterator.next();
    }
    return null;
  }
  
  public static class Builder
    extends SubTableContainerTable.Builder<CMapTable>
  {
    private int version = 0;
    private Map<CMapTable.CMapId, CMap.Builder<? extends CMap>> cmapBuilders;
    
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    protected static CMap.Builder<? extends CMap> cmapBuilder(ReadableFontData paramReadableFontData, int paramInt)
    {
      if ((paramInt < 0) || (paramInt > numCMaps(paramReadableFontData))) {
        throw new IndexOutOfBoundsException("CMap table is outside the bounds of the known tables.");
      }
      int i = paramReadableFontData.readUShort(CMapTable.Offset.encodingRecordPlatformId.offset + CMapTable.offsetForEncodingRecord(paramInt));
      int j = paramReadableFontData.readUShort(CMapTable.Offset.encodingRecordEncodingId.offset + CMapTable.offsetForEncodingRecord(paramInt));
      int k = paramReadableFontData.readULongAsInt(CMapTable.Offset.encodingRecordOffset.offset + CMapTable.offsetForEncodingRecord(paramInt));
      CMapTable.CMapId localCMapId = CMapTable.CMapId.getInstance(i, j);
      CMap.Builder localBuilder = CMap.Builder.getBuilder(paramReadableFontData, k, localCMapId);
      return localBuilder;
    }
    
    protected void subDataSet()
    {
      this.cmapBuilders = null;
      super.setModelChanged(false);
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      this.cmapBuilders = new HashMap();
      int i = numCMaps(paramReadableFontData);
      for (int j = 0; j < i; j++)
      {
        CMap.Builder localBuilder = cmapBuilder(paramReadableFontData, j);
        this.cmapBuilders.put(localBuilder.cmapId(), localBuilder);
      }
    }
    
    private Map<CMapTable.CMapId, CMap.Builder<? extends CMap>> getCMapBuilders()
    {
      if (this.cmapBuilders != null) {
        return this.cmapBuilders;
      }
      initialize(internalReadData());
      setModelChanged();
      return this.cmapBuilders;
    }
    
    private static int numCMaps(ReadableFontData paramReadableFontData)
    {
      if (paramReadableFontData == null) {
        return 0;
      }
      return paramReadableFontData.readUShort(CMapTable.Offset.numTables.offset);
    }
    
    public int numCMaps()
    {
      return getCMapBuilders().size();
    }
    
    protected int subDataSizeToSerialize()
    {
      if ((this.cmapBuilders == null) || (this.cmapBuilders.size() == 0)) {
        return 0;
      }
      int i = 0;
      int j = CMapTable.Offset.encodingRecordStart.offset + this.cmapBuilders.size() * CMapTable.Offset.encodingRecordSize.offset;
      Iterator localIterator = this.cmapBuilders.values().iterator();
      while (localIterator.hasNext())
      {
        CMap.Builder localBuilder = (CMap.Builder)localIterator.next();
        int k = localBuilder.subDataSizeToSerialize();
        j += Math.abs(k);
        i |= (k <= 0 ? 1 : 0);
      }
      return i != 0 ? -j : j;
    }
    
    protected boolean subReadyToSerialize()
    {
      if (this.cmapBuilders == null) {
        return false;
      }
      Iterator localIterator = this.cmapBuilders.values().iterator();
      while (localIterator.hasNext())
      {
        CMap.Builder localBuilder = (CMap.Builder)localIterator.next();
        if (!localBuilder.subReadyToSerialize()) {
          return false;
        }
      }
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = paramWritableFontData.writeUShort(CMapTable.Offset.version.offset, version());
      i += paramWritableFontData.writeUShort(CMapTable.Offset.numTables.offset, this.cmapBuilders.size());
      int j = i;
      i += this.cmapBuilders.size() * CMapTable.Offset.encodingRecordSize.offset;
      Iterator localIterator = this.cmapBuilders.values().iterator();
      while (localIterator.hasNext())
      {
        CMap.Builder localBuilder = (CMap.Builder)localIterator.next();
        j += paramWritableFontData.writeUShort(j, localBuilder.platformId());
        j += paramWritableFontData.writeUShort(j, localBuilder.encodingId());
        j += paramWritableFontData.writeULong(j, i);
        i += localBuilder.subSerialize(paramWritableFontData.slice(i));
      }
      return i;
    }
    
    protected CMapTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapTable(header(), paramReadableFontData, null);
    }
    
    public Iterator<? extends CMap.Builder<? extends CMap>> iterator()
    {
      return getCMapBuilders().values().iterator();
    }
    
    public int version()
    {
      return this.version;
    }
    
    public void setVersion(int paramInt)
    {
      this.version = paramInt;
    }
    
    public CMap.Builder<? extends CMap> newCMapBuilder(CMapTable.CMapId paramCMapId, ReadableFontData paramReadableFontData)
      throws IOException
    {
      WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramReadableFontData.size());
      paramReadableFontData.copyTo(localWritableFontData);
      CMap.Builder localBuilder = CMap.Builder.getBuilder(localWritableFontData, 0, paramCMapId);
      Map localMap = getCMapBuilders();
      localMap.put(paramCMapId, localBuilder);
      return localBuilder;
    }
    
    public CMap.Builder<? extends CMap> newCMapBuilder(CMapTable.CMapId paramCMapId, CMap.CMapFormat paramCMapFormat)
    {
      CMap.Builder localBuilder = CMap.Builder.getBuilder(paramCMapFormat, paramCMapId);
      Map localMap = getCMapBuilders();
      localMap.put(paramCMapId, localBuilder);
      return localBuilder;
    }
    
    public CMap.Builder<? extends CMap> cmapBuilder(CMapTable.CMapId paramCMapId)
    {
      Map localMap = getCMapBuilders();
      return (CMap.Builder)localMap.get(paramCMapId);
    }
  }
  
  private class CMapIterator
    implements Iterator<CMap>
  {
    private int tableIndex = 0;
    private CMapTable.CMapFilter filter;
    
    private CMapIterator() {}
    
    private CMapIterator(CMapTable.CMapFilter paramCMapFilter)
    {
      this.filter = paramCMapFilter;
    }
    
    public boolean hasNext()
    {
      if (this.filter == null) {
        return this.tableIndex < CMapTable.this.numCMaps();
      }
      while (this.tableIndex < CMapTable.this.numCMaps())
      {
        if (this.filter.accept(CMapTable.this.cmapId(this.tableIndex))) {
          return true;
        }
        this.tableIndex += 1;
      }
      return false;
    }
    
    public CMap next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      try
      {
        return CMapTable.this.cmap(this.tableIndex++);
      }
      catch (IOException localIOException)
      {
        NoSuchElementException localNoSuchElementException = new NoSuchElementException("Error during the creation of the CMap.");
        localNoSuchElementException.initCause(localIOException);
        throw localNoSuchElementException;
      }
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Cannot remove a CMap table from an existing font.");
    }
  }
  
  public static abstract interface CMapFilter
  {
    public abstract boolean accept(CMapTable.CMapId paramCMapId);
  }
  
  public static final class CMapId
    implements Comparable<CMapId>
  {
    public static final CMapId WINDOWS_BMP = getInstance(Font.PlatformId.Windows.value(), Font.WindowsEncodingId.UnicodeUCS2.value());
    public static final CMapId WINDOWS_UCS4 = getInstance(Font.PlatformId.Windows.value(), Font.WindowsEncodingId.UnicodeUCS4.value());
    public static final CMapId MAC_ROMAN = getInstance(Font.PlatformId.Macintosh.value(), Font.MacintoshEncodingId.Roman.value());
    private final int platformId;
    private final int encodingId;
    
    public static CMapId getInstance(int paramInt1, int paramInt2)
    {
      return new CMapId(paramInt1, paramInt2);
    }
    
    private CMapId(int paramInt1, int paramInt2)
    {
      this.platformId = paramInt1;
      this.encodingId = paramInt2;
    }
    
    public int platformId()
    {
      return this.platformId;
    }
    
    public int encodingId()
    {
      return this.encodingId;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof CMapId)) {
        return false;
      }
      CMapId localCMapId = (CMapId)paramObject;
      return (localCMapId.platformId == this.platformId) && (localCMapId.encodingId == this.encodingId);
    }
    
    public int hashCode()
    {
      return this.platformId << 8 | this.encodingId;
    }
    
    public int compareTo(CMapId paramCMapId)
    {
      return hashCode() - paramCMapId.hashCode();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pid = ");
      localStringBuilder.append(this.platformId);
      localStringBuilder.append(", eid = ");
      localStringBuilder.append(this.encodingId);
      return localStringBuilder.toString();
    }
  }
  
  static enum Offset
  {
    version(0),  numTables(2),  encodingRecordStart(4),  encodingRecordPlatformId(0),  encodingRecordEncodingId(2),  encodingRecordOffset(4),  encodingRecordSize(8),  format(0),  format0Format(0),  format0Length(2),  format0Language(4),  format0GlyphIdArray(6),  format2Format(0),  format2Length(2),  format2Language(4),  format2SubHeaderKeys(6),  format2SubHeaders(518),  format2SubHeader_firstCode(0),  format2SubHeader_entryCount(2),  format2SubHeader_idDelta(4),  format2SubHeader_idRangeOffset(6),  format2SubHeader_structLength(8),  format4Format(0),  format4Length(2),  format4Language(4),  format4SegCountX2(6),  format4SearchRange(8),  format4EntrySelector(10),  format4RangeShift(12),  format4EndCount(14),  format4FixedSize(16),  format6Format(0),  format6Length(2),  format6Language(4),  format6FirstCode(6),  format6EntryCount(8),  format6GlyphIdArray(10),  format8Format(0),  format8Length(4),  format8Language(8),  format8Is32(12),  format8nGroups(8204),  format8Groups(8208),  format8Group_startCharCode(0),  format8Group_endCharCode(4),  format8Group_startGlyphId(8),  format8Group_structLength(12),  format10Format(0),  format10Length(4),  format10Language(8),  format10StartCharCode(12),  format10NumChars(16),  format10Glyphs(20),  format12Format(0),  format12Length(4),  format12Language(8),  format12nGroups(12),  format12Groups(16),  format12Groups_structLength(12),  format12_startCharCode(0),  format12_endCharCode(4),  format12_startGlyphId(8),  format13Format(0),  format13Length(4),  format13Language(8),  format13nGroups(12),  format13Groups(16),  format13Groups_structLength(12),  format13_startCharCode(0),  format13_endCharCode(4),  format13_glyphId(8),  format14Format(0),  format14Length(2);
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */