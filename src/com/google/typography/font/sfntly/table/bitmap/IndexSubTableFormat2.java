package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IndexSubTableFormat2
  extends IndexSubTable
{
  private final int imageSize = this.data.readULongAsInt(EblcTable.Offset.indexSubTable2_imageSize.offset);
  
  private IndexSubTableFormat2(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
  }
  
  public int imageSize()
  {
    return this.data.readULongAsInt(EblcTable.Offset.indexSubTable2_imageSize.offset);
  }
  
  public BigGlyphMetrics bigMetrics()
  {
    return new BigGlyphMetrics(this.data.slice(EblcTable.Offset.indexSubTable2_bigGlyphMetrics.offset, BigGlyphMetrics.Offset.metricsLength.offset));
  }
  
  public int numGlyphs()
  {
    return lastGlyphIndex() - firstGlyphIndex() + 1;
  }
  
  public int glyphStartOffset(int paramInt)
  {
    int i = checkGlyphRange(paramInt);
    return i * this.imageSize;
  }
  
  public int glyphLength(int paramInt)
  {
    checkGlyphRange(paramInt);
    return this.imageSize;
  }
  
  public static final class Builder
    extends IndexSubTable.Builder<IndexSubTableFormat2>
  {
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
      return EblcTable.Offset.indexSubTable2Length.offset;
    }
    
    private Builder()
    {
      super(2);
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
    
    public int numGlyphs()
    {
      return lastGlyphIndex() - firstGlyphIndex() + 1;
    }
    
    public int glyphStartOffset(int paramInt)
    {
      int i = super.checkGlyphRange(paramInt);
      return i * imageSize();
    }
    
    public int glyphLength(int paramInt)
    {
      super.checkGlyphRange(paramInt);
      return imageSize();
    }
    
    public int imageSize()
    {
      return internalReadData().readULongAsInt(EblcTable.Offset.indexSubTable2_imageSize.offset);
    }
    
    public void setImageSize(int paramInt)
    {
      internalWriteData().writeULong(EblcTable.Offset.indexSubTable2_imageSize.offset, paramInt);
    }
    
    public BigGlyphMetrics.Builder bigMetrics()
    {
      if (this.metrics == null)
      {
        WritableFontData localWritableFontData = internalWriteData().slice(EblcTable.Offset.indexSubTable2_bigGlyphMetrics.offset, BigGlyphMetrics.Offset.metricsLength.offset);
        this.metrics = new BigGlyphMetrics.Builder(localWritableFontData);
      }
      return this.metrics;
    }
    
    Iterator<BitmapGlyphInfo> iterator()
    {
      return new BitmapGlyphInfoIterator();
    }
    
    protected IndexSubTableFormat2 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new IndexSubTableFormat2(paramReadableFontData, firstGlyphIndex(), lastGlyphIndex(), null);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      return EblcTable.Offset.indexSubTable2Length.offset;
    }
    
    protected boolean subReadyToSerialize()
    {
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = super.serializeIndexSubHeader(paramWritableFontData);
      if (this.metrics == null)
      {
        i += internalReadData().slice(i).copyTo(paramWritableFontData.slice(i));
      }
      else
      {
        i += paramWritableFontData.writeLong(EblcTable.Offset.indexSubTable2_imageSize.offset, imageSize());
        i += this.metrics.subSerialize(paramWritableFontData.slice(i));
      }
      return i;
    }
    
    private class BitmapGlyphInfoIterator
      implements Iterator<BitmapGlyphInfo>
    {
      private int glyphId = IndexSubTableFormat2.Builder.this.firstGlyphIndex();
      
      public BitmapGlyphInfoIterator() {}
      
      public boolean hasNext()
      {
        return this.glyphId <= IndexSubTableFormat2.Builder.this.lastGlyphIndex();
      }
      
      public BitmapGlyphInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException("No more characters to iterate.");
        }
        BitmapGlyphInfo localBitmapGlyphInfo = new BitmapGlyphInfo(this.glyphId, IndexSubTableFormat2.Builder.this.imageDataOffset(), IndexSubTableFormat2.Builder.this.glyphStartOffset(this.glyphId), IndexSubTableFormat2.Builder.this.glyphLength(this.glyphId), IndexSubTableFormat2.Builder.this.imageFormat());
        this.glyphId += 1;
        return localBitmapGlyphInfo;
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException("Unable to remove a glyph info.");
      }
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\IndexSubTableFormat2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */