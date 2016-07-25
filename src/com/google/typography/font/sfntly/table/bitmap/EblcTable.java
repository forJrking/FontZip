package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.math.FontMath;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTableContainerTable;
import com.google.typography.font.sfntly.table.SubTableContainerTable.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EblcTable
  extends SubTableContainerTable
{
  private static final boolean DEBUG = false;
  public static final int NOTDEF = -1;
  private final Object bitmapSizeTableLock = new Object();
  private volatile List<BitmapSizeTable> bitmapSizeTable;
  
  protected EblcTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int version()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public int numSizes()
  {
    return this.data.readULongAsInt(Offset.numSizes.offset);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    localStringBuilder.append("\nnum sizes = ");
    localStringBuilder.append(numSizes());
    localStringBuilder.append("\n");
    for (int i = 0; i < numSizes(); i++)
    {
      localStringBuilder.append(i);
      localStringBuilder.append(": ");
      BitmapSizeTable localBitmapSizeTable = bitmapSizeTable(i);
      localStringBuilder.append(localBitmapSizeTable.toString());
    }
    return localStringBuilder.toString();
  }
  
  public BitmapSizeTable bitmapSizeTable(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > numSizes())) {
      throw new IndexOutOfBoundsException("Size table index is outside of the range of tables.");
    }
    List localList = getBitmapSizeTableList();
    return (BitmapSizeTable)localList.get(paramInt);
  }
  
  private List<BitmapSizeTable> getBitmapSizeTableList()
  {
    if (this.bitmapSizeTable == null) {
      synchronized (this.bitmapSizeTableLock)
      {
        if (this.bitmapSizeTable == null) {
          this.bitmapSizeTable = createBitmapSizeTable(this.data, numSizes());
        }
      }
    }
    return this.bitmapSizeTable;
  }
  
  private static List<BitmapSizeTable> createBitmapSizeTable(ReadableFontData paramReadableFontData, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramInt; i++)
    {
      BitmapSizeTable.Builder localBuilder = BitmapSizeTable.Builder.createBuilder(paramReadableFontData.slice(Offset.bitmapSizeTableArrayStart.offset + i * Offset.bitmapSizeTableLength.offset, Offset.bitmapSizeTableLength.offset), paramReadableFontData);
      BitmapSizeTable localBitmapSizeTable = (BitmapSizeTable)localBuilder.build();
      localArrayList.add(localBitmapSizeTable);
    }
    return Collections.unmodifiableList(localArrayList);
  }
  
  public static final class Builder
    extends SubTableContainerTable.Builder<EblcTable>
  {
    private final int version = 131072;
    private List<BitmapSizeTable.Builder> sizeTableBuilders;
    
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
    
    public List<BitmapSizeTable.Builder> bitmapSizeBuilders()
    {
      return getSizeList();
    }
    
    protected void revert()
    {
      this.sizeTableBuilders = null;
      setModelChanged(false);
    }
    
    public List<Map<Integer, BitmapGlyphInfo>> generateLocaList()
    {
      List localList = getSizeList();
      ArrayList localArrayList = new ArrayList(localList.size());
      int i = 0;
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        BitmapSizeTable.Builder localBuilder = (BitmapSizeTable.Builder)localIterator.next();
        Map localMap = localBuilder.generateLocaMap();
        localArrayList.add(localMap);
      }
      return localArrayList;
    }
    
    private List<BitmapSizeTable.Builder> getSizeList()
    {
      if (this.sizeTableBuilders == null)
      {
        this.sizeTableBuilders = initialize(internalReadData());
        super.setModelChanged();
      }
      return this.sizeTableBuilders;
    }
    
    private List<BitmapSizeTable.Builder> initialize(ReadableFontData paramReadableFontData)
    {
      ArrayList localArrayList = new ArrayList();
      if (paramReadableFontData != null)
      {
        int i = paramReadableFontData.readULongAsInt(EblcTable.Offset.numSizes.offset);
        for (int j = 0; j < i; j++)
        {
          BitmapSizeTable.Builder localBuilder = BitmapSizeTable.Builder.createBuilder(paramReadableFontData.slice(EblcTable.Offset.bitmapSizeTableArrayStart.offset + j * EblcTable.Offset.bitmapSizeTableLength.offset, EblcTable.Offset.bitmapSizeTableLength.offset), paramReadableFontData);
          localArrayList.add(localBuilder);
        }
      }
      return localArrayList;
    }
    
    protected EblcTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new EblcTable(header(), paramReadableFontData);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if (this.sizeTableBuilders == null) {
        return 0;
      }
      int i = EblcTable.Offset.headerLength.offset;
      int j = 0;
      int k = 0;
      Iterator localIterator = this.sizeTableBuilders.iterator();
      while (localIterator.hasNext())
      {
        BitmapSizeTable.Builder localBuilder = (BitmapSizeTable.Builder)localIterator.next();
        int m = localBuilder.subDataSizeToSerialize();
        j = m > 0 ? j : 1;
        i += Math.abs(m);
      }
      return j != 0 ? -i : i;
    }
    
    protected boolean subReadyToSerialize()
    {
      if (this.sizeTableBuilders == null) {
        return false;
      }
      Iterator localIterator = this.sizeTableBuilders.iterator();
      while (localIterator.hasNext())
      {
        BitmapSizeTable.Builder localBuilder = (BitmapSizeTable.Builder)localIterator.next();
        if (!localBuilder.subReadyToSerialize()) {
          return false;
        }
      }
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      getClass();
      int i = paramWritableFontData.writeFixed(0, 131072);
      i += paramWritableFontData.writeULong(i, this.sizeTableBuilders.size());
      int j = i;
      int k = j;
      int m = k + this.sizeTableBuilders.size() * EblcTable.Offset.bitmapSizeTableLength.offset;
      int n = m;
      int i1 = 0;
      Iterator localIterator1 = this.sizeTableBuilders.iterator();
      while (localIterator1.hasNext())
      {
        BitmapSizeTable.Builder localBuilder = (BitmapSizeTable.Builder)localIterator1.next();
        localBuilder.setIndexSubTableArrayOffset(n);
        List localList = localBuilder.indexSubTableBuilders();
        int i2 = n;
        int i3 = i2 + localList.size() * EblcTable.Offset.indexSubHeaderLength.offset;
        int i4 = 0;
        Iterator localIterator2 = localList.iterator();
        while (localIterator2.hasNext())
        {
          IndexSubTable.Builder localBuilder1 = (IndexSubTable.Builder)localIterator2.next();
          i2 += paramWritableFontData.writeUShort(i2, localBuilder1.firstGlyphIndex());
          i2 += paramWritableFontData.writeUShort(i2, localBuilder1.lastGlyphIndex());
          i2 += paramWritableFontData.writeULong(i2, i3 - n);
          int i5 = localBuilder1.subSerialize(paramWritableFontData.slice(i3));
          int i6 = FontMath.paddingRequired(i5, FontData.DataSize.ULONG.size());
          i3 += i5;
          i3 += paramWritableFontData.writePadding(i3, i6);
        }
        localBuilder.setIndexTableSize(i3 - n);
        k += localBuilder.subSerialize(paramWritableFontData.slice(k));
        n = i3;
      }
      return i + n;
    }
  }
  
  static enum Offset
  {
    version(0),  numSizes(4),  headerLength(numSizes.offset + FontData.DataSize.ULONG.size()),  bitmapSizeTableArrayStart(headerLength.offset),  bitmapSizeTableLength(48),  bitmapSizeTable_indexSubTableArrayOffset(0),  bitmapSizeTable_indexTableSize(4),  bitmapSizeTable_numberOfIndexSubTables(8),  bitmapSizeTable_colorRef(12),  bitmapSizeTable_hori(16),  bitmapSizeTable_vert(28),  bitmapSizeTable_startGlyphIndex(40),  bitmapSizeTable_endGlyphIndex(42),  bitmapSizeTable_ppemX(44),  bitmapSizeTable_ppemY(45),  bitmapSizeTable_bitDepth(46),  bitmapSizeTable_flags(47),  sbitLineMetricsLength(12),  sbitLineMetrics_ascender(0),  sbitLineMetrics_descender(1),  sbitLineMetrics_widthMax(2),  sbitLineMetrics_caretSlopeNumerator(3),  sbitLineMetrics__caretSlopeDenominator(4),  sbitLineMetrics_caretOffset(5),  sbitLineMetrics_minOriginSB(6),  sbitLineMetrics_minAdvanceSB(7),  sbitLineMetrics_maxBeforeBL(8),  sbitLineMetrics_minAfterBL(9),  sbitLineMetrics_pad1(10),  sbitLineMetrics_pad2(11),  indexSubTableEntryLength(8),  indexSubTableEntry_firstGlyphIndex(0),  indexSubTableEntry_lastGlyphIndex(2),  indexSubTableEntry_additionalOffsetToIndexSubtable(4),  indexSubHeaderLength(8),  indexSubHeader_indexFormat(0),  indexSubHeader_imageFormat(2),  indexSubHeader_imageDataOffset(4),  indexSubTable1_offsetArray(indexSubHeaderLength.offset),  indexSubTable1_builderDataSize(indexSubHeaderLength.offset),  indexSubTable2Length(indexSubHeaderLength.offset + FontData.DataSize.ULONG.size() + BitmapGlyph.Offset.bigGlyphMetricsLength.offset),  indexSubTable2_imageSize(indexSubHeaderLength.offset),  indexSubTable2_bigGlyphMetrics(indexSubTable2_imageSize.offset + FontData.DataSize.ULONG.size()),  indexSubTable2_builderDataSize(indexSubTable2_bigGlyphMetrics.offset + BigGlyphMetrics.Offset.metricsLength.offset),  indexSubTable3_offsetArray(indexSubHeaderLength.offset),  indexSubTable3_builderDataSize(indexSubTable3_offsetArray.offset),  indexSubTable4_numGlyphs(indexSubHeaderLength.offset),  indexSubTable4_glyphArray(indexSubTable4_numGlyphs.offset + FontData.DataSize.ULONG.size()),  indexSubTable4_codeOffsetPairLength(2 * FontData.DataSize.USHORT.size()),  indexSubTable4_codeOffsetPair_glyphCode(0),  indexSubTable4_codeOffsetPair_offset(FontData.DataSize.USHORT.size()),  indexSubTable4_builderDataSize(indexSubTable4_glyphArray.offset),  indexSubTable5_imageSize(indexSubHeaderLength.offset),  indexSubTable5_bigGlyphMetrics(indexSubTable5_imageSize.offset + FontData.DataSize.ULONG.size()),  indexSubTable5_numGlyphs(indexSubTable5_bigGlyphMetrics.offset + BitmapGlyph.Offset.bigGlyphMetricsLength.offset),  indexSubTable5_glyphArray(indexSubTable5_numGlyphs.offset + FontData.DataSize.ULONG.size()),  indexSubTable5_builderDataSize(indexSubTable5_glyphArray.offset),  codeOffsetPairLength(2 * FontData.DataSize.USHORT.size()),  codeOffsetPair_glyphCode(0),  codeOffsetPair_offset(FontData.DataSize.USHORT.size());
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\EblcTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */