package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;
import java.util.Iterator;

public abstract class IndexSubTable
  extends SubTable
{
  private static final boolean DEBUG = false;
  private final int firstGlyphIndex;
  private final int lastGlyphIndex;
  private final int indexFormat;
  private final int imageFormat;
  private final int imageDataOffset;
  
  protected static IndexSubTable createIndexSubTable(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    Builder localBuilder = Builder.createBuilder(paramReadableFontData, paramInt1, paramInt2);
    if (localBuilder == null) {
      return null;
    }
    return (IndexSubTable)localBuilder.build();
  }
  
  protected IndexSubTable(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData);
    this.firstGlyphIndex = paramInt1;
    this.lastGlyphIndex = paramInt2;
    this.indexFormat = this.data.readUShort(EblcTable.Offset.indexSubHeader_indexFormat.offset);
    this.imageFormat = this.data.readUShort(EblcTable.Offset.indexSubHeader_imageFormat.offset);
    this.imageDataOffset = this.data.readULongAsInt(EblcTable.Offset.indexSubHeader_imageDataOffset.offset);
  }
  
  public int indexFormat()
  {
    return this.indexFormat;
  }
  
  public int firstGlyphIndex()
  {
    return this.firstGlyphIndex;
  }
  
  public int lastGlyphIndex()
  {
    return this.lastGlyphIndex;
  }
  
  public int imageFormat()
  {
    return this.imageFormat;
  }
  
  public int imageDataOffset()
  {
    return this.imageDataOffset;
  }
  
  public BitmapGlyphInfo glyphInfo(int paramInt)
  {
    int i = checkGlyphRange(paramInt);
    if (i == -1) {
      return null;
    }
    if (glyphStartOffset(paramInt) == -1) {
      return null;
    }
    return new BitmapGlyphInfo(paramInt, imageDataOffset(), glyphStartOffset(paramInt), glyphLength(paramInt), imageFormat());
  }
  
  public final int glyphOffset(int paramInt)
  {
    int i = glyphStartOffset(paramInt);
    if (i == -1) {
      return -1;
    }
    return imageDataOffset() + i;
  }
  
  public abstract int glyphStartOffset(int paramInt);
  
  public abstract int glyphLength(int paramInt);
  
  public abstract int numGlyphs();
  
  protected static int checkGlyphRange(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < paramInt2) || (paramInt1 > paramInt3)) {
      throw new IndexOutOfBoundsException("Glyph ID is outside of the allowed range.");
    }
    return paramInt1 - paramInt2;
  }
  
  protected int checkGlyphRange(int paramInt)
  {
    return checkGlyphRange(paramInt, firstGlyphIndex(), lastGlyphIndex());
  }
  
  public String toString()
  {
    String str = "IndexSubTable: [0x" + Integer.toHexString(firstGlyphIndex()) + " : Ox" + Integer.toHexString(lastGlyphIndex()) + "]" + ", format = " + this.indexFormat + ", image format = " + imageFormat() + ", imageOff = " + Integer.toHexString(imageDataOffset()) + "\n";
    return str;
  }
  
  public static abstract class Builder<T extends IndexSubTable>
    extends SubTable.Builder<T>
  {
    private int firstGlyphIndex;
    private int lastGlyphIndex;
    private int indexFormat;
    private int imageFormat;
    private int imageDataOffset;
    
    public static Builder<? extends IndexSubTable> createBuilder(int paramInt)
    {
      switch (paramInt)
      {
      case 1: 
        return IndexSubTableFormat1.Builder.createBuilder();
      case 2: 
        return IndexSubTableFormat2.Builder.createBuilder();
      case 3: 
        return IndexSubTableFormat3.Builder.createBuilder();
      case 4: 
        return IndexSubTableFormat4.Builder.createBuilder();
      case 5: 
        return IndexSubTableFormat5.Builder.createBuilder();
      }
      throw new IllegalArgumentException(String.format("Invalid Index SubTable Format %i%n", new Object[] { Integer.valueOf(paramInt) }));
    }
    
    static Builder<? extends IndexSubTable> createBuilder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      int i = paramInt1 + paramInt2 * EblcTable.Offset.indexSubTableEntryLength.offset;
      int j = paramReadableFontData.readUShort(i + EblcTable.Offset.indexSubTableEntry_firstGlyphIndex.offset);
      int k = paramReadableFontData.readUShort(i + EblcTable.Offset.indexSubTableEntry_lastGlyphIndex.offset);
      int m = paramReadableFontData.readULongAsInt(i + EblcTable.Offset.indexSubTableEntry_additionalOffsetToIndexSubtable.offset);
      int n = paramInt1 + m;
      int i1 = paramReadableFontData.readUShort(n);
      switch (i1)
      {
      case 1: 
        return IndexSubTableFormat1.Builder.createBuilder(paramReadableFontData, n, j, k);
      case 2: 
        return IndexSubTableFormat2.Builder.createBuilder(paramReadableFontData, n, j, k);
      case 3: 
        return IndexSubTableFormat3.Builder.createBuilder(paramReadableFontData, n, j, k);
      case 4: 
        return IndexSubTableFormat4.Builder.createBuilder(paramReadableFontData, n, j, k);
      case 5: 
        return IndexSubTableFormat5.Builder.createBuilder(paramReadableFontData, n, j, k);
      }
      throw new IllegalArgumentException(String.format("Invalid Index SubTable Foramt %i%n", new Object[] { Integer.valueOf(i1) }));
    }
    
    protected Builder(int paramInt1, int paramInt2)
    {
      super();
      this.indexFormat = paramInt2;
    }
    
    protected Builder(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this(paramInt4, paramInt1);
      this.imageFormat = paramInt2;
      this.imageDataOffset = paramInt3;
    }
    
    protected Builder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      super();
      this.firstGlyphIndex = paramInt1;
      this.lastGlyphIndex = paramInt2;
      initialize(paramWritableFontData);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      super();
      this.firstGlyphIndex = paramInt1;
      this.lastGlyphIndex = paramInt2;
      initialize(paramReadableFontData);
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      this.indexFormat = paramReadableFontData.readUShort(EblcTable.Offset.indexSubHeader_indexFormat.offset);
      this.imageFormat = paramReadableFontData.readUShort(EblcTable.Offset.indexSubHeader_imageFormat.offset);
      this.imageDataOffset = paramReadableFontData.readULongAsInt(EblcTable.Offset.indexSubHeader_imageDataOffset.offset);
    }
    
    protected void revert()
    {
      setModelChanged(false);
      initialize(internalReadData());
    }
    
    public int indexFormat()
    {
      return this.indexFormat;
    }
    
    public int firstGlyphIndex()
    {
      return this.firstGlyphIndex;
    }
    
    public void setFirstGlyphIndex(int paramInt)
    {
      this.firstGlyphIndex = paramInt;
    }
    
    public int lastGlyphIndex()
    {
      return this.lastGlyphIndex;
    }
    
    public void setLastGlyphIndex(int paramInt)
    {
      this.lastGlyphIndex = paramInt;
    }
    
    public int imageFormat()
    {
      return this.imageFormat;
    }
    
    public void setImageFormat(int paramInt)
    {
      this.imageFormat = paramInt;
    }
    
    public int imageDataOffset()
    {
      return this.imageDataOffset;
    }
    
    public void setImageDataOffset(int paramInt)
    {
      this.imageDataOffset = paramInt;
    }
    
    public abstract int numGlyphs();
    
    public BitmapGlyphInfo glyphInfo(int paramInt)
    {
      return new BitmapGlyphInfo(paramInt, imageDataOffset(), glyphStartOffset(paramInt), glyphLength(paramInt), imageFormat());
    }
    
    public final int glyphOffset(int paramInt)
    {
      return imageDataOffset() + glyphStartOffset(paramInt);
    }
    
    public abstract int glyphStartOffset(int paramInt);
    
    public abstract int glyphLength(int paramInt);
    
    protected int checkGlyphRange(int paramInt)
    {
      return IndexSubTable.checkGlyphRange(paramInt, firstGlyphIndex(), lastGlyphIndex());
    }
    
    protected int serializeIndexSubHeader(WritableFontData paramWritableFontData)
    {
      int i = paramWritableFontData.writeUShort(EblcTable.Offset.indexSubHeader_indexFormat.offset, this.indexFormat);
      i += paramWritableFontData.writeUShort(EblcTable.Offset.indexSubHeader_imageFormat.offset, this.imageFormat);
      i += paramWritableFontData.writeULong(EblcTable.Offset.indexSubHeader_imageDataOffset.offset, this.imageDataOffset);
      return i;
    }
    
    abstract Iterator<BitmapGlyphInfo> iterator();
    
    protected T subBuildTable(ReadableFontData paramReadableFontData)
    {
      return null;
    }
    
    protected void subDataSet() {}
    
    protected int subDataSizeToSerialize()
    {
      return 0;
    }
    
    protected boolean subReadyToSerialize()
    {
      return false;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      return 0;
    }
    
    public String toString()
    {
      String str = "IndexSubTable: [0x" + Integer.toHexString(firstGlyphIndex()) + " : Ox" + Integer.toHexString(lastGlyphIndex()) + "]" + ", format = " + this.indexFormat + ", image format = " + imageFormat() + ", imageOff = 0x" + Integer.toHexString(imageDataOffset()) + "\n";
      return str;
    }
  }
  
  public static final class Format
  {
    public static final int FORMAT_1 = 1;
    public static final int FORMAT_2 = 2;
    public static final int FORMAT_3 = 3;
    public static final int FORMAT_4 = 4;
    public static final int FORMAT_5 = 5;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\IndexSubTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */