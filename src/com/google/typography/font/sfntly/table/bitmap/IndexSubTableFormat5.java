package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class IndexSubTableFormat5
  extends IndexSubTable
{
  private final int imageSize = this.data.readULongAsInt(EblcTable.Offset.indexSubTable5_imageSize.offset);
  
  private IndexSubTableFormat5(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
  }
  
  private static int numGlyphs(ReadableFontData paramReadableFontData, int paramInt)
  {
    int i = paramReadableFontData.readULongAsInt(paramInt + EblcTable.Offset.indexSubTable5_numGlyphs.offset);
    return i;
  }
  
  public int imageSize()
  {
    return this.data.readULongAsInt(EblcTable.Offset.indexSubTable5_imageSize.offset);
  }
  
  public BigGlyphMetrics bigMetrics()
  {
    return new BigGlyphMetrics(this.data.slice(EblcTable.Offset.indexSubTable5_bigGlyphMetrics.offset, BigGlyphMetrics.Offset.metricsLength.offset));
  }
  
  public int numGlyphs()
  {
    return numGlyphs(this.data, 0);
  }
  
  public int glyphStartOffset(int paramInt)
  {
    checkGlyphRange(paramInt);
    int i = readFontData().searchUShort(EblcTable.Offset.indexSubTable5_glyphArray.offset, FontData.DataSize.USHORT.size(), numGlyphs(), paramInt);
    if (i == -1) {
      return i;
    }
    return i * this.imageSize;
  }
  
  public int glyphLength(int paramInt)
  {
    checkGlyphRange(paramInt);
    return this.imageSize;
  }
  
  public static final class Builder
    extends IndexSubTable.Builder<IndexSubTableFormat5>
  {
    private List<Integer> glyphArray;
    private BigGlyphMetrics.Builder metrics;
    
    public static Builder createBuilder()
    {
      return new Builder();
    }
    
    static Builder createBuilder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2, int paramInt3)
    {
      int i = dataLength(paramReadableFontData, paramInt1, paramInt2, paramInt3);
      return new Builder(paramReadableFontData.slice(paramInt1, i), paramInt2, paramInt3);
    }
    
    static Builder createBuilder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2, int paramInt3)
    {
      int i = dataLength(paramWritableFontData, paramInt1, paramInt2, paramInt3);
      return new Builder(paramWritableFontData.slice(paramInt1, i), paramInt2, paramInt3);
    }
    
    private static int dataLength(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2, int paramInt3)
    {
      int i = IndexSubTableFormat5.numGlyphs(paramReadableFontData, paramInt1);
      return EblcTable.Offset.indexSubTable5_glyphArray.offset + i * FontData.DataSize.USHORT.size();
    }
    
    private Builder()
    {
      super(5);
      this.metrics = BigGlyphMetrics.Builder.createBuilder();
    }
    
    private Builder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
    }
    
    private Builder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
    }
    
    public int imageSize()
    {
      return internalReadData().readULongAsInt(EblcTable.Offset.indexSubTable5_imageSize.offset);
    }
    
    public void setImageSize(int paramInt)
    {
      internalWriteData().writeULong(EblcTable.Offset.indexSubTable5_imageSize.offset, paramInt);
    }
    
    public BigGlyphMetrics.Builder bigMetrics()
    {
      if (this.metrics == null)
      {
        WritableFontData localWritableFontData = internalWriteData().slice(EblcTable.Offset.indexSubTable5_bigGlyphMetrics.offset, BigGlyphMetrics.Offset.metricsLength.offset);
        this.metrics = new BigGlyphMetrics.Builder(localWritableFontData);
        setModelChanged();
      }
      return this.metrics;
    }
    
    public int numGlyphs()
    {
      return getGlyphArray().size();
    }
    
    public int glyphLength(int paramInt)
    {
      return imageSize();
    }
    
    public int glyphStartOffset(int paramInt)
    {
      checkGlyphRange(paramInt);
      List localList = getGlyphArray();
      int i = Collections.binarySearch(localList, Integer.valueOf(paramInt));
      if (i == -1) {
        return -1;
      }
      return i * imageSize();
    }
    
    public List<Integer> glyphArray()
    {
      return getGlyphArray();
    }
    
    private List<Integer> getGlyphArray()
    {
      if (this.glyphArray == null)
      {
        initialize(super.internalReadData());
        super.setModelChanged();
      }
      return this.glyphArray;
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      if (this.glyphArray == null) {
        this.glyphArray = new ArrayList();
      } else {
        this.glyphArray.clear();
      }
      if (paramReadableFontData != null)
      {
        int i = IndexSubTableFormat5.numGlyphs(paramReadableFontData, 0);
        for (int j = 0; j < i; j++) {
          this.glyphArray.add(Integer.valueOf(paramReadableFontData.readUShort(EblcTable.Offset.indexSubTable5_glyphArray.offset + j * FontData.DataSize.USHORT.size())));
        }
      }
    }
    
    public void setGlyphArray(List<Integer> paramList)
    {
      this.glyphArray = paramList;
      setModelChanged();
    }
    
    Iterator<BitmapGlyphInfo> iterator()
    {
      return new BitmapGlyphInfoIterator();
    }
    
    protected void revert()
    {
      super.revert();
      this.glyphArray = null;
    }
    
    protected IndexSubTableFormat5 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new IndexSubTableFormat5(paramReadableFontData, firstGlyphIndex(), lastGlyphIndex(), null);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if (this.glyphArray == null) {
        return internalReadData().length();
      }
      return EblcTable.Offset.indexSubTable5_builderDataSize.offset + this.glyphArray.size() * FontData.DataSize.USHORT.size();
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.glyphArray != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = super.serializeIndexSubHeader(paramWritableFontData);
      if (!modelChanged())
      {
        i += internalReadData().slice(EblcTable.Offset.indexSubTable5_imageSize.offset).copyTo(paramWritableFontData.slice(EblcTable.Offset.indexSubTable5_imageSize.offset));
      }
      else
      {
        i += paramWritableFontData.writeULong(EblcTable.Offset.indexSubTable5_imageSize.offset, imageSize());
        i += bigMetrics().subSerialize(paramWritableFontData.slice(i));
        i += paramWritableFontData.writeULong(i, this.glyphArray.size());
        Iterator localIterator = this.glyphArray.iterator();
        while (localIterator.hasNext())
        {
          Integer localInteger = (Integer)localIterator.next();
          i += paramWritableFontData.writeUShort(i, localInteger.intValue());
        }
      }
      return i;
    }
    
    private class BitmapGlyphInfoIterator
      implements Iterator<BitmapGlyphInfo>
    {
      private int offsetIndex;
      
      public BitmapGlyphInfoIterator() {}
      
      public boolean hasNext()
      {
        return this.offsetIndex < IndexSubTableFormat5.Builder.this.getGlyphArray().size();
      }
      
      public BitmapGlyphInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException("No more characters to iterate.");
        }
        BitmapGlyphInfo localBitmapGlyphInfo = new BitmapGlyphInfo(((Integer)IndexSubTableFormat5.Builder.this.getGlyphArray().get(this.offsetIndex)).intValue(), IndexSubTableFormat5.Builder.this.imageDataOffset(), this.offsetIndex * IndexSubTableFormat5.Builder.this.imageSize(), IndexSubTableFormat5.Builder.this.imageSize(), IndexSubTableFormat5.Builder.this.imageFormat());
        this.offsetIndex += 1;
        return localBitmapGlyphInfo;
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException("Unable to remove a glyph info.");
      }
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\IndexSubTableFormat5.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */