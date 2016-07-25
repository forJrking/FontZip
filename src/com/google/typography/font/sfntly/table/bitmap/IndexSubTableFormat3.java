package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class IndexSubTableFormat3
  extends IndexSubTable
{
  private IndexSubTableFormat3(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
  }
  
  public int numGlyphs()
  {
    return lastGlyphIndex() - firstGlyphIndex() + 1;
  }
  
  public int glyphStartOffset(int paramInt)
  {
    int i = checkGlyphRange(paramInt);
    return loca(i);
  }
  
  public int glyphLength(int paramInt)
  {
    int i = checkGlyphRange(paramInt);
    return loca(i + 1) - loca(i);
  }
  
  private int loca(int paramInt)
  {
    int i = EblcTable.Offset.indexSubTable3_offsetArray.offset + paramInt * FontData.DataSize.USHORT.size();
    int j = this.data.readUShort(EblcTable.Offset.indexSubTable3_offsetArray.offset + paramInt * FontData.DataSize.USHORT.size());
    return j;
  }
  
  public static final class Builder
    extends IndexSubTable.Builder<IndexSubTableFormat3>
  {
    private List<Integer> offsetArray;
    
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
      return EblcTable.Offset.indexSubHeaderLength.offset + (paramInt3 - paramInt2 + 1 + 1) * FontData.DataSize.USHORT.size();
    }
    
    private Builder()
    {
      super(3);
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
      return getOffsetArray().size() - 1;
    }
    
    public int glyphLength(int paramInt)
    {
      int i = checkGlyphRange(paramInt);
      List localList = getOffsetArray();
      return ((Integer)localList.get(i + 1)).intValue() - ((Integer)localList.get(i)).intValue();
    }
    
    public int glyphStartOffset(int paramInt)
    {
      int i = checkGlyphRange(paramInt);
      List localList = getOffsetArray();
      return ((Integer)localList.get(i)).intValue();
    }
    
    public List<Integer> offsetArray()
    {
      return getOffsetArray();
    }
    
    private List<Integer> getOffsetArray()
    {
      if (this.offsetArray == null)
      {
        initialize(super.internalReadData());
        super.setModelChanged();
      }
      return this.offsetArray;
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      if (this.offsetArray == null) {
        this.offsetArray = new ArrayList();
      } else {
        this.offsetArray.clear();
      }
      if (paramReadableFontData != null)
      {
        int i = lastGlyphIndex() - firstGlyphIndex() + 1 + 1;
        for (int j = 0; j < i; j++) {
          this.offsetArray.add(Integer.valueOf(paramReadableFontData.readUShort(EblcTable.Offset.indexSubTable3_offsetArray.offset + j * FontData.DataSize.USHORT.size())));
        }
      }
    }
    
    public void setOffsetArray(List<Integer> paramList)
    {
      this.offsetArray = paramList;
      setModelChanged();
    }
    
    Iterator<BitmapGlyphInfo> iterator()
    {
      return new BitmapGlyphInfoIterator();
    }
    
    protected void revert()
    {
      super.revert();
      this.offsetArray = null;
    }
    
    protected IndexSubTableFormat3 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new IndexSubTableFormat3(paramReadableFontData, firstGlyphIndex(), lastGlyphIndex(), null);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if (this.offsetArray == null) {
        return internalReadData().length();
      }
      return EblcTable.Offset.indexSubHeaderLength.offset + this.offsetArray.size() * FontData.DataSize.ULONG.size();
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.offsetArray != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = super.serializeIndexSubHeader(paramWritableFontData);
      if (!modelChanged())
      {
        i += internalReadData().slice(EblcTable.Offset.indexSubTable3_offsetArray.offset).copyTo(paramWritableFontData.slice(EblcTable.Offset.indexSubTable3_offsetArray.offset));
      }
      else
      {
        Iterator localIterator = this.offsetArray.iterator();
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
      private int glyphId = IndexSubTableFormat3.Builder.this.firstGlyphIndex();
      
      public BitmapGlyphInfoIterator() {}
      
      public boolean hasNext()
      {
        return this.glyphId <= IndexSubTableFormat3.Builder.this.lastGlyphIndex();
      }
      
      public BitmapGlyphInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException("No more characters to iterate.");
        }
        BitmapGlyphInfo localBitmapGlyphInfo = new BitmapGlyphInfo(this.glyphId, IndexSubTableFormat3.Builder.this.imageDataOffset(), IndexSubTableFormat3.Builder.this.glyphStartOffset(this.glyphId), IndexSubTableFormat3.Builder.this.glyphLength(this.glyphId), IndexSubTableFormat3.Builder.this.imageFormat());
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


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\IndexSubTableFormat3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */