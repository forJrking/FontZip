package com.google.typography.font.sfntly.table.bitmap;

import java.util.Comparator;

public final class BitmapGlyphInfo
{
  private final int glyphId;
  private final boolean relative;
  private final int blockOffset;
  private final int startOffset;
  private final int length;
  private final int format;
  public static final Comparator<BitmapGlyphInfo> StartOffsetComparator = new StartOffsetComparatorClass(null);
  
  public BitmapGlyphInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.glyphId = paramInt1;
    this.relative = true;
    this.blockOffset = paramInt2;
    this.startOffset = paramInt3;
    this.length = paramInt4;
    this.format = paramInt5;
  }
  
  public BitmapGlyphInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.glyphId = paramInt1;
    this.relative = false;
    this.blockOffset = 0;
    this.startOffset = paramInt2;
    this.length = paramInt3;
    this.format = paramInt4;
  }
  
  public int glyphId()
  {
    return this.glyphId;
  }
  
  public boolean relative()
  {
    return this.relative;
  }
  
  public int blockOffset()
  {
    return this.blockOffset;
  }
  
  public int offset()
  {
    return blockOffset() + startOffset();
  }
  
  public int startOffset()
  {
    return this.startOffset;
  }
  
  public int length()
  {
    return this.length;
  }
  
  public int format()
  {
    return this.format;
  }
  
  public int hashCode()
  {
    int i = 1;
    i = 31 * i + this.blockOffset;
    i = 31 * i + this.format;
    i = 31 * i + this.glyphId;
    i = 31 * i + this.length;
    i = 31 * i + this.startOffset;
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof BitmapGlyphInfo)) {
      return false;
    }
    BitmapGlyphInfo localBitmapGlyphInfo = (BitmapGlyphInfo)paramObject;
    if (this.format != localBitmapGlyphInfo.format) {
      return false;
    }
    if (this.glyphId != localBitmapGlyphInfo.glyphId) {
      return false;
    }
    if (this.length != localBitmapGlyphInfo.length) {
      return false;
    }
    return offset() == localBitmapGlyphInfo.offset();
  }
  
  private static final class StartOffsetComparatorClass
    implements Comparator<BitmapGlyphInfo>
  {
    public int compare(BitmapGlyphInfo paramBitmapGlyphInfo1, BitmapGlyphInfo paramBitmapGlyphInfo2)
    {
      return paramBitmapGlyphInfo1.startOffset - paramBitmapGlyphInfo2.startOffset;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\BitmapGlyphInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */