package com.google.typography.font.sfntly.data;

public abstract class FontData
{
  protected static final int GROWABLE_SIZE = Integer.MAX_VALUE;
  protected final ByteArray<? extends ByteArray<?>> array;
  private int boundOffset;
  private int boundLength = Integer.MAX_VALUE;
  
  protected FontData(ByteArray<? extends ByteArray<?>> paramByteArray)
  {
    this.array = paramByteArray;
  }
  
  protected FontData(FontData paramFontData, int paramInt1, int paramInt2)
  {
    this(paramFontData.array);
    bound(paramFontData.boundOffset + paramInt1, paramInt2);
  }
  
  protected FontData(FontData paramFontData, int paramInt)
  {
    this(paramFontData.array);
    bound(paramFontData.boundOffset + paramInt, paramFontData.boundLength == Integer.MAX_VALUE ? Integer.MAX_VALUE : paramFontData.boundLength - paramInt);
  }
  
  public boolean bound(int paramInt1, int paramInt2)
  {
    if ((paramInt1 + paramInt2 > size()) || (paramInt1 < 0) || (paramInt2 < 0)) {
      return false;
    }
    this.boundOffset += paramInt1;
    this.boundLength = paramInt2;
    return true;
  }
  
  public boolean bound(int paramInt)
  {
    if ((paramInt > size()) || (paramInt < 0)) {
      return false;
    }
    this.boundOffset += paramInt;
    return true;
  }
  
  public abstract FontData slice(int paramInt1, int paramInt2);
  
  public abstract FontData slice(int paramInt);
  
  public int length()
  {
    return Math.min(this.array.length() - this.boundOffset, this.boundLength);
  }
  
  public int size()
  {
    return Math.min(this.array.size() - this.boundOffset, this.boundLength);
  }
  
  protected final int boundOffset(int paramInt)
  {
    return paramInt + this.boundOffset;
  }
  
  protected final int boundLength(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt2, this.boundLength - paramInt1);
  }
  
  protected final boolean boundsCheck(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= this.boundLength)) {
      return false;
    }
    return (paramInt2 >= 0) && (paramInt2 + paramInt1 <= this.boundLength);
  }
  
  public static enum DataSize
  {
    BYTE(1),  CHAR(1),  USHORT(2),  SHORT(2),  UINT24(3),  ULONG(4),  LONG(4),  Fixed(4),  FUNIT(4),  FWORD(2),  UFWORD(2),  F2DOT14(2),  LONGDATETIME(8),  Tag(4),  GlyphID(2),  Offset(2);
    
    private final int size;
    
    private DataSize(int paramInt)
    {
      this.size = paramInt;
    }
    
    public int size()
    {
      return this.size;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\FontData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */