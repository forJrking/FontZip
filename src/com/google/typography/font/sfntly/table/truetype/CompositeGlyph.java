package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.util.LinkedList;
import java.util.List;

public final class CompositeGlyph
  extends Glyph
{
  public static final int FLAG_ARG_1_AND_2_ARE_WORDS = 1;
  public static final int FLAG_ARGS_ARE_XY_VALUES = 2;
  public static final int FLAG_ROUND_XY_TO_GRID = 4;
  public static final int FLAG_WE_HAVE_A_SCALE = 8;
  public static final int FLAG_RESERVED = 16;
  public static final int FLAG_MORE_COMPONENTS = 32;
  public static final int FLAG_WE_HAVE_AN_X_AND_Y_SCALE = 64;
  public static final int FLAG_WE_HAVE_A_TWO_BY_TWO = 128;
  public static final int FLAG_WE_HAVE_INSTRUCTIONS = 256;
  public static final int FLAG_USE_MY_METRICS = 512;
  public static final int FLAG_OVERLAP_COMPOUND = 1024;
  public static final int FLAG_SCALED_COMPONENT_OFFSET = 2048;
  public static final int FLAG_UNSCALED_COMPONENT_OFFSET = 4096;
  private final List<Integer> contourIndex = new LinkedList();
  private int instructionsOffset;
  private int instructionSize;
  
  protected CompositeGlyph(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2, Glyph.GlyphType.Composite);
    initialize();
  }
  
  protected CompositeGlyph(ReadableFontData paramReadableFontData)
  {
    super(paramReadableFontData, Glyph.GlyphType.Composite);
    initialize();
  }
  
  protected void initialize()
  {
    if (this.initialized) {
      return;
    }
    synchronized (this.initializationLock)
    {
      if (this.initialized) {
        return;
      }
      int i = 5 * FontData.DataSize.USHORT.size();
      int j = 32;
      while ((j & 0x20) == 32)
      {
        this.contourIndex.add(Integer.valueOf(i));
        j = this.data.readUShort(i);
        i += 2 * FontData.DataSize.USHORT.size();
        if ((j & 0x1) == 1) {
          i += 2 * FontData.DataSize.SHORT.size();
        } else {
          i += 2 * FontData.DataSize.BYTE.size();
        }
        if ((j & 0x8) == 8) {
          i += FontData.DataSize.F2DOT14.size();
        } else if ((j & 0x40) == 64) {
          i += 2 * FontData.DataSize.F2DOT14.size();
        } else if ((j & 0x80) == 128) {
          i += 4 * FontData.DataSize.F2DOT14.size();
        }
      }
      int k = i;
      if ((j & 0x100) == 256)
      {
        this.instructionSize = this.data.readUShort(i);
        i += FontData.DataSize.USHORT.size();
        this.instructionsOffset = i;
        k = i + this.instructionSize * FontData.DataSize.BYTE.size();
      }
      setPadding(dataLength() - k);
    }
  }
  
  public int flags(int paramInt)
  {
    return this.data.readUShort(((Integer)this.contourIndex.get(paramInt)).intValue());
  }
  
  public int numGlyphs()
  {
    return this.contourIndex.size();
  }
  
  public int glyphIndex(int paramInt)
  {
    return this.data.readUShort(FontData.DataSize.USHORT.size() + ((Integer)this.contourIndex.get(paramInt)).intValue());
  }
  
  public int argument1(int paramInt)
  {
    int i = 2 * FontData.DataSize.USHORT.size() + ((Integer)this.contourIndex.get(paramInt)).intValue();
    int j = flags(paramInt);
    if ((j & 0x1) == 1) {
      return this.data.readUShort(i);
    }
    return this.data.readByte(i);
  }
  
  public int argument2(int paramInt)
  {
    int i = 2 * FontData.DataSize.USHORT.size() + ((Integer)this.contourIndex.get(paramInt)).intValue();
    int j = flags(paramInt);
    if ((j & 0x1) == 1) {
      return this.data.readUShort(i + FontData.DataSize.USHORT.size());
    }
    return this.data.readByte(i + FontData.DataSize.BYTE.size());
  }
  
  public int transformationSize(int paramInt)
  {
    int i = flags(paramInt);
    if ((i & 0x8) == 8) {
      return FontData.DataSize.F2DOT14.size();
    }
    if ((i & 0x40) == 64) {
      return 2 * FontData.DataSize.F2DOT14.size();
    }
    if ((i & 0x80) == 128) {
      return 4 * FontData.DataSize.F2DOT14.size();
    }
    return 0;
  }
  
  public byte[] transformation(int paramInt)
  {
    int i = flags(paramInt);
    int j = ((Integer)this.contourIndex.get(paramInt)).intValue() + 2 * FontData.DataSize.USHORT.size();
    if ((i & 0x1) == 1) {
      j += 2 * FontData.DataSize.SHORT.size();
    } else {
      j += 2 * FontData.DataSize.BYTE.size();
    }
    int k = transformationSize(paramInt);
    byte[] arrayOfByte = new byte[k];
    this.data.readBytes(j, arrayOfByte, 0, k);
    return arrayOfByte;
  }
  
  public int instructionSize()
  {
    return this.instructionSize;
  }
  
  public ReadableFontData instructions()
  {
    return this.data.slice(this.instructionsOffset, instructionSize());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    localStringBuilder.append("\ncontourOffset.length = ");
    localStringBuilder.append(this.contourIndex.size());
    localStringBuilder.append("\ninstructionSize = ");
    localStringBuilder.append(this.instructionSize);
    localStringBuilder.append("\n\tcontour index = [");
    for (int i = 0; i < this.contourIndex.size(); i++)
    {
      if (i != 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(this.contourIndex.get(i));
    }
    localStringBuilder.append("]\n");
    for (i = 0; i < this.contourIndex.size(); i++) {
      localStringBuilder.append("\t" + i + " = [gid = " + glyphIndex(i) + ", arg1 = " + argument1(i) + ", arg2 = " + argument2(i) + "]\n");
    }
    return localStringBuilder.toString();
  }
  
  public static class CompositeGlyphBuilder
    extends Glyph.Builder<CompositeGlyph>
  {
    protected CompositeGlyphBuilder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      super();
    }
    
    protected CompositeGlyphBuilder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      super();
    }
    
    protected CompositeGlyph subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CompositeGlyph(paramReadableFontData);
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\CompositeGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */