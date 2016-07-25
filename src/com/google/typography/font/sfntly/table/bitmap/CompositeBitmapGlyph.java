package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public class CompositeBitmapGlyph
  extends BitmapGlyph
{
  private int numComponentsOffset;
  private int componentArrayOffset;
  
  protected CompositeBitmapGlyph(ReadableFontData paramReadableFontData, int paramInt)
  {
    super(paramReadableFontData, paramInt);
    initialize(paramInt);
  }
  
  private void initialize(int paramInt)
  {
    if (paramInt == 8)
    {
      this.numComponentsOffset = BitmapGlyph.Offset.glyphFormat8_numComponents.offset;
      this.componentArrayOffset = BitmapGlyph.Offset.glyphFormat8_componentArray.offset;
    }
    else if (paramInt == 9)
    {
      this.numComponentsOffset = BitmapGlyph.Offset.glyphFormat9_numComponents.offset;
      this.componentArrayOffset = BitmapGlyph.Offset.glyphFormat9_componentArray.offset;
    }
    else
    {
      throw new IllegalStateException("Attempt to create a Composite Bitmap Glyph with a non-composite format.");
    }
  }
  
  public int numComponents()
  {
    return this.data.readUShort(this.numComponentsOffset);
  }
  
  public Component component(int paramInt)
  {
    int i = this.componentArrayOffset + paramInt * BitmapGlyph.Offset.ebdtComponentLength.offset;
    return new Component(this.data.readUShort(i + BitmapGlyph.Offset.ebdtComponent_glyphCode.offset), this.data.readChar(i + BitmapGlyph.Offset.ebdtComponent_xOffset.offset), this.data.readChar(i + BitmapGlyph.Offset.ebdtComponent_yOffset.offset));
  }
  
  public static class Builder
    extends BitmapGlyph.Builder<CompositeBitmapGlyph>
  {
    protected Builder(WritableFontData paramWritableFontData, int paramInt)
    {
      super(paramInt);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt)
    {
      super(paramInt);
    }
    
    protected CompositeBitmapGlyph subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CompositeBitmapGlyph(paramReadableFontData, format());
    }
  }
  
  public static final class Component
  {
    private final int glyphCode;
    private int xOffset;
    private int yOffset;
    
    protected Component(int paramInt1, int paramInt2, int paramInt3)
    {
      this.glyphCode = paramInt1;
      this.xOffset = paramInt2;
      this.yOffset = paramInt3;
    }
    
    public int glyphCode()
    {
      return this.glyphCode;
    }
    
    public int xOffset()
    {
      return this.xOffset;
    }
    
    public int yOffset()
    {
      return this.yOffset;
    }
    
    public int hashCode()
    {
      int i = 1;
      i = 31 * i + this.glyphCode;
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
      if (!(paramObject instanceof Component)) {
        return false;
      }
      Component localComponent = (Component)paramObject;
      return this.glyphCode == localComponent.glyphCode;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\bitmap\CompositeBitmapGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */