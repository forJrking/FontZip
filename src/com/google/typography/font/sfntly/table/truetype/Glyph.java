package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;

public abstract class Glyph
  extends SubTable
{
  protected volatile boolean initialized = false;
  protected final Object initializationLock = new Object();
  private final GlyphType glyphType;
  private final int numberOfContours;
  
  protected Glyph(ReadableFontData paramReadableFontData, GlyphType paramGlyphType)
  {
    super(paramReadableFontData);
    this.glyphType = paramGlyphType;
    if (this.data.length() == 0) {
      this.numberOfContours = 0;
    } else {
      this.numberOfContours = this.data.readShort(GlyphTable.Offset.numberOfContours.offset);
    }
  }
  
  protected Glyph(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2, GlyphType paramGlyphType)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
    this.glyphType = paramGlyphType;
    if (this.data.length() == 0) {
      this.numberOfContours = 0;
    } else {
      this.numberOfContours = this.data.readShort(GlyphTable.Offset.numberOfContours.offset);
    }
  }
  
  private static GlyphType glyphType(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramReadableFontData.length()) {
      throw new IndexOutOfBoundsException();
    }
    if (paramInt2 == 0) {
      return GlyphType.Simple;
    }
    int i = paramReadableFontData.readShort(paramInt1);
    if (i >= 0) {
      return GlyphType.Simple;
    }
    return GlyphType.Composite;
  }
  
  static Glyph getGlyph(GlyphTable paramGlyphTable, ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    GlyphType localGlyphType = glyphType(paramReadableFontData, paramInt1, paramInt2);
    if (localGlyphType == GlyphType.Simple) {
      return new SimpleGlyph(paramReadableFontData, paramInt1, paramInt2);
    }
    return new CompositeGlyph(paramReadableFontData, paramInt1, paramInt2);
  }
  
  protected abstract void initialize();
  
  public int padding()
  {
    initialize();
    return super.padding();
  }
  
  public GlyphType glyphType()
  {
    return this.glyphType;
  }
  
  public int numberOfContours()
  {
    return this.numberOfContours;
  }
  
  public int xMin()
  {
    return this.data.readShort(GlyphTable.Offset.xMin.offset);
  }
  
  public int xMax()
  {
    return this.data.readShort(GlyphTable.Offset.xMax.offset);
  }
  
  public int yMin()
  {
    return this.data.readShort(GlyphTable.Offset.yMin.offset);
  }
  
  public int yMax()
  {
    return this.data.readShort(GlyphTable.Offset.yMax.offset);
  }
  
  public abstract int instructionSize();
  
  public abstract ReadableFontData instructions();
  
  public String toString()
  {
    return toString(0);
  }
  
  public String toString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(glyphType());
    localStringBuilder.append(", contours=");
    localStringBuilder.append(numberOfContours());
    localStringBuilder.append(", [xmin=");
    localStringBuilder.append(xMin());
    localStringBuilder.append(", ymin=");
    localStringBuilder.append(yMin());
    localStringBuilder.append(", xmax=");
    localStringBuilder.append(xMax());
    localStringBuilder.append(", ymax=");
    localStringBuilder.append(yMax());
    localStringBuilder.append("]");
    localStringBuilder.append("\n");
    return localStringBuilder.toString();
  }
  
  public static abstract class Builder<T extends Glyph>
    extends SubTable.Builder<T>
  {
    protected int format;
    
    protected Builder(WritableFontData paramWritableFontData)
    {
      super();
    }
    
    protected Builder(ReadableFontData paramReadableFontData)
    {
      super();
    }
    
    protected Builder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      this(paramWritableFontData.slice(paramInt1, paramInt2));
    }
    
    static Builder<? extends Glyph> getBuilder(GlyphTable.Builder paramBuilder, ReadableFontData paramReadableFontData)
    {
      return getBuilder(paramBuilder, paramReadableFontData, 0, paramReadableFontData.length());
    }
    
    static Builder<? extends Glyph> getBuilder(GlyphTable.Builder paramBuilder, ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      Glyph.GlyphType localGlyphType = Glyph.glyphType(paramReadableFontData, paramInt1, paramInt2);
      if (localGlyphType == Glyph.GlyphType.Simple) {
        return new SimpleGlyph.SimpleGlyphBuilder(paramReadableFontData, paramInt1, paramInt2);
      }
      return new CompositeGlyph.CompositeGlyphBuilder(paramReadableFontData, paramInt1, paramInt2);
    }
    
    protected void subDataSet() {}
    
    protected int subDataSizeToSerialize()
    {
      return internalReadData().length();
    }
    
    protected boolean subReadyToSerialize()
    {
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      return internalReadData().copyTo(paramWritableFontData);
    }
  }
  
  public static abstract class Contour {}
  
  public static enum GlyphType
  {
    Simple,  Composite;
    
    private GlyphType() {}
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\Glyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */