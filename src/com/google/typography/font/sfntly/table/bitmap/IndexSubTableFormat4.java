package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class IndexSubTableFormat4
  extends IndexSubTable
{
  public static final Comparator<CodeOffsetPair> CodeOffsetPairComparatorByGlyphCode = new CodeOffsetPairGlyphCodeComparator(null);
  
  private IndexSubTableFormat4(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
  }
  
  private static int numGlyphs(ReadableFontData paramReadableFontData, int paramInt)
  {
    int i = paramReadableFontData.readULongAsInt(paramInt + EblcTable.Offset.indexSubTable4_numGlyphs.offset);
    return i;
  }
  
  public int numGlyphs()
  {
    return numGlyphs(this.data, 0);
  }
  
  public int glyphStartOffset(int paramInt)
  {
    checkGlyphRange(paramInt);
    int i = findCodeOffsetPair(paramInt);
    if (i < 0) {
      return -1;
    }
    return this.data.readUShort(EblcTable.Offset.indexSubTable4_glyphArray.offset + i * EblcTable.Offset.codeOffsetPairLength.offset + EblcTable.Offset.codeOffsetPair_offset.offset);
  }
  
  public int glyphLength(int paramInt)
  {
    checkGlyphRange(paramInt);
    int i = findCodeOffsetPair(paramInt);
    if (i < 0) {
      return -1;
    }
    return this.data.readUShort(EblcTable.Offset.indexSubTable4_glyphArray.offset + (i + 1) * EblcTable.Offset.codeOffsetPairLength.offset + EblcTable.Offset.codeOffsetPair_offset.offset) - this.data.readUShort(EblcTable.Offset.indexSubTable4_glyphArray.offset + i * EblcTable.Offset.codeOffsetPairLength.offset + EblcTable.Offset.codeOffsetPair_offset.offset);
  }
  
  protected int findCodeOffsetPair(int paramInt)
  {
    return this.data.searchUShort(EblcTable.Offset.indexSubTable4_glyphArray.offset, EblcTable.Offset.codeOffsetPairLength.offset, numGlyphs(), paramInt);
  }
  
  public static final class Builder
    extends IndexSubTable.Builder<IndexSubTableFormat4>
  {
    private List<IndexSubTableFormat4.CodeOffsetPairBuilder> offsetPairArray;
    
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
      int i = IndexSubTableFormat4.numGlyphs(paramReadableFontData, paramInt1);
      return EblcTable.Offset.indexSubTable4_glyphArray.offset + i * EblcTable.Offset.indexSubTable4_codeOffsetPairLength.offset;
    }
    
    private Builder()
    {
      super(4);
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
      checkGlyphRange(paramInt);
      int i = findCodeOffsetPair(paramInt);
      if (i == -1) {
        return -1;
      }
      return ((IndexSubTableFormat4.CodeOffsetPairBuilder)getOffsetArray().get(i + 1)).offset() - ((IndexSubTableFormat4.CodeOffsetPairBuilder)getOffsetArray().get(i)).offset();
    }
    
    public int glyphStartOffset(int paramInt)
    {
      checkGlyphRange(paramInt);
      int i = findCodeOffsetPair(paramInt);
      if (i == -1) {
        return -1;
      }
      return ((IndexSubTableFormat4.CodeOffsetPairBuilder)getOffsetArray().get(i)).offset();
    }
    
    public List<IndexSubTableFormat4.CodeOffsetPairBuilder> offsetArray()
    {
      return getOffsetArray();
    }
    
    private List<IndexSubTableFormat4.CodeOffsetPairBuilder> getOffsetArray()
    {
      if (this.offsetPairArray == null)
      {
        initialize(super.internalReadData());
        super.setModelChanged();
      }
      return this.offsetPairArray;
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      if (this.offsetPairArray == null) {
        this.offsetPairArray = new ArrayList();
      } else {
        this.offsetPairArray.clear();
      }
      if (paramReadableFontData != null)
      {
        int i = IndexSubTableFormat4.numGlyphs(paramReadableFontData, 0) + 1;
        int j = EblcTable.Offset.indexSubTable4_glyphArray.offset;
        for (int k = 0; k < i; k++)
        {
          int m = paramReadableFontData.readUShort(j + EblcTable.Offset.indexSubTable4_codeOffsetPair_glyphCode.offset);
          int n = paramReadableFontData.readUShort(j + EblcTable.Offset.indexSubTable4_codeOffsetPair_offset.offset);
          j += EblcTable.Offset.indexSubTable4_codeOffsetPairLength.offset;
          IndexSubTableFormat4.CodeOffsetPairBuilder localCodeOffsetPairBuilder = new IndexSubTableFormat4.CodeOffsetPairBuilder(m, n, null);
          this.offsetPairArray.add(localCodeOffsetPairBuilder);
        }
      }
    }
    
    private int findCodeOffsetPair(int paramInt)
    {
      List localList = getOffsetArray();
      int i = 0;
      int j = 0;
      int k = localList.size();
      while (k != j)
      {
        i = (k + j) / 2;
        IndexSubTableFormat4.CodeOffsetPairBuilder localCodeOffsetPairBuilder = (IndexSubTableFormat4.CodeOffsetPairBuilder)localList.get(i);
        if (paramInt < localCodeOffsetPairBuilder.glyphCode()) {
          k = i;
        } else if (paramInt > localCodeOffsetPairBuilder.glyphCode()) {
          j = i + 1;
        } else {
          return i;
        }
      }
      return -1;
    }
    
    public void setOffsetArray(List<IndexSubTableFormat4.CodeOffsetPairBuilder> paramList)
    {
      this.offsetPairArray = paramList;
      setModelChanged();
    }
    
    Iterator<BitmapGlyphInfo> iterator()
    {
      return new BitmapGlyphInfoIterator();
    }
    
    protected void revert()
    {
      super.revert();
      this.offsetPairArray = null;
    }
    
    protected IndexSubTableFormat4 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new IndexSubTableFormat4(paramReadableFontData, firstGlyphIndex(), lastGlyphIndex(), null);
    }
    
    protected void subDataSet()
    {
      revert();
    }
    
    protected int subDataSizeToSerialize()
    {
      if (this.offsetPairArray == null) {
        return internalReadData().length();
      }
      return EblcTable.Offset.indexSubHeaderLength.offset + FontData.DataSize.ULONG.size() + this.offsetPairArray.size() * EblcTable.Offset.indexSubTable4_codeOffsetPairLength.offset;
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.offsetPairArray != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = super.serializeIndexSubHeader(paramWritableFontData);
      if (!modelChanged())
      {
        i += internalReadData().slice(EblcTable.Offset.indexSubTable4_numGlyphs.offset).copyTo(paramWritableFontData.slice(EblcTable.Offset.indexSubTable4_numGlyphs.offset));
      }
      else
      {
        i += paramWritableFontData.writeLong(i, this.offsetPairArray.size() - 1);
        Iterator localIterator = this.offsetPairArray.iterator();
        while (localIterator.hasNext())
        {
          IndexSubTableFormat4.CodeOffsetPairBuilder localCodeOffsetPairBuilder = (IndexSubTableFormat4.CodeOffsetPairBuilder)localIterator.next();
          i += paramWritableFontData.writeUShort(i, localCodeOffsetPairBuilder.glyphCode());
          i += paramWritableFontData.writeUShort(i, localCodeOffsetPairBuilder.offset());
        }
      }
      return i;
    }
    
    private class BitmapGlyphInfoIterator
      implements Iterator<BitmapGlyphInfo>
    {
      private int codeOffsetPairIndex;
      
      public BitmapGlyphInfoIterator() {}
      
      public boolean hasNext()
      {
        return this.codeOffsetPairIndex < IndexSubTableFormat4.Builder.this.getOffsetArray().size() - 1;
      }
      
      public BitmapGlyphInfo next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException("No more characters to iterate.");
        }
        List localList = IndexSubTableFormat4.Builder.this.getOffsetArray();
        IndexSubTableFormat4.CodeOffsetPair localCodeOffsetPair = (IndexSubTableFormat4.CodeOffsetPair)localList.get(this.codeOffsetPairIndex);
        BitmapGlyphInfo localBitmapGlyphInfo = new BitmapGlyphInfo(localCodeOffsetPair.glyphCode(), IndexSubTableFormat4.Builder.this.imageDataOffset(), localCodeOffsetPair.offset(), ((IndexSubTableFormat4.CodeOffsetPairBuilder)localList.get(this.codeOffsetPairIndex + 1)).offset() - localCodeOffsetPair.offset(), IndexSubTableFormat4.Builder.this.imageFormat());
        this.codeOffsetPairIndex += 1;
        return localBitmapGlyphInfo;
      }
      
      public void remove()
      {
        throw new UnsupportedOperationException("Unable to remove a glyph info.");
      }
    }
  }
  
  private static final class CodeOffsetPairGlyphCodeComparator
    implements Comparator<IndexSubTableFormat4.CodeOffsetPair>
  {
    public int compare(IndexSubTableFormat4.CodeOffsetPair paramCodeOffsetPair1, IndexSubTableFormat4.CodeOffsetPair paramCodeOffsetPair2)
    {
      return paramCodeOffsetPair1.glyphCode - paramCodeOffsetPair2.glyphCode;
    }
  }
  
  public static final class CodeOffsetPairBuilder
    extends IndexSubTableFormat4.CodeOffsetPair
  {
    private CodeOffsetPairBuilder(int paramInt1, int paramInt2)
    {
      super(paramInt2, null);
    }
    
    public void setGlyphCode(int paramInt)
    {
      this.glyphCode = paramInt;
    }
    
    public void setOffset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
  
  public static class CodeOffsetPair
  {
    protected int glyphCode;
    protected int offset;
    
    private CodeOffsetPair(int paramInt1, int paramInt2)
    {
      this.glyphCode = paramInt1;
      this.offset = paramInt2;
    }
    
    public int glyphCode()
    {
      return this.glyphCode;
    }
    
    public int offset()
    {
      return this.offset;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\IndexSubTableFormat4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */