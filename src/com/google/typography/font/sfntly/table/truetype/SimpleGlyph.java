package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public final class SimpleGlyph
  extends Glyph
{
  private static final int FLAG_ONCURVE = 1;
  private static final int FLAG_XSHORT = 2;
  private static final int FLAG_YSHORT = 4;
  private static final int FLAG_REPEAT = 8;
  private static final int FLAG_XREPEATSIGN = 16;
  private static final int FLAG_YREPEATSIGN = 32;
  private int instructionSize;
  private int numberOfPoints;
  private int instructionsOffset;
  private int flagsOffset;
  private int xCoordinatesOffset;
  private int yCoordinatesOffset;
  private int flagByteCount;
  private int xByteCount;
  private int yByteCount;
  private int[] xCoordinates;
  private int[] yCoordinates;
  private boolean[] onCurve;
  private int[] contourIndex;
  
  SimpleGlyph(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2, Glyph.GlyphType.Simple);
  }
  
  private SimpleGlyph(ReadableFontData paramReadableFontData)
  {
    super(paramReadableFontData, Glyph.GlyphType.Simple);
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
      if (readFontData().length() == 0)
      {
        this.instructionSize = 0;
        this.numberOfPoints = 0;
        this.instructionsOffset = 0;
        this.flagsOffset = 0;
        this.xCoordinatesOffset = 0;
        this.yCoordinatesOffset = 0;
        return;
      }
      this.instructionSize = this.data.readUShort(GlyphTable.Offset.simpleEndPtsOfCountours.offset + numberOfContours() * FontData.DataSize.USHORT.size());
      this.instructionsOffset = (GlyphTable.Offset.simpleEndPtsOfCountours.offset + (numberOfContours() + 1) * FontData.DataSize.USHORT.size());
      this.flagsOffset = (this.instructionsOffset + this.instructionSize * FontData.DataSize.BYTE.size());
      this.numberOfPoints = (contourEndPoint(numberOfContours() - 1) + 1);
      this.xCoordinates = new int[this.numberOfPoints];
      this.yCoordinates = new int[this.numberOfPoints];
      this.onCurve = new boolean[this.numberOfPoints];
      parseData(false);
      this.xCoordinatesOffset = (this.flagsOffset + this.flagByteCount * FontData.DataSize.BYTE.size());
      this.yCoordinatesOffset = (this.xCoordinatesOffset + this.xByteCount * FontData.DataSize.BYTE.size());
      this.contourIndex = new int[numberOfContours() + 1];
      this.contourIndex[0] = 0;
      for (int i = 0; i < this.contourIndex.length - 1; i++) {
        this.contourIndex[(i + 1)] = (contourEndPoint(i) + 1);
      }
      parseData(true);
      i = 5 * FontData.DataSize.SHORT.size() + numberOfContours() * FontData.DataSize.USHORT.size() + FontData.DataSize.USHORT.size() + this.instructionSize * FontData.DataSize.BYTE.size() + this.flagByteCount * FontData.DataSize.BYTE.size() + this.xByteCount * FontData.DataSize.BYTE.size() + this.yByteCount * FontData.DataSize.BYTE.size();
      setPadding(dataLength() - i);
      this.initialized = true;
    }
  }
  
  private void parseData(boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    for (int i1 = 0; i1 < this.numberOfPoints; i1++)
    {
      if (j == 0)
      {
        i = flagAsInt(k++);
        if ((i & 0x8) == 8) {
          j = flagAsInt(k++);
        }
      }
      else
      {
        j--;
      }
      if (paramBoolean) {
        this.onCurve[i1] = ((i & 0x1) == 1 ? 1 : false);
      }
      if ((i & 0x2) == 2)
      {
        if (paramBoolean)
        {
          this.xCoordinates[i1] = this.data.readUByte(this.xCoordinatesOffset + m);
          this.xCoordinates[i1] *= ((i & 0x10) == 16 ? 1 : -1);
        }
        m++;
      }
      else if ((i & 0x10) != 16)
      {
        if (paramBoolean) {
          this.xCoordinates[i1] = this.data.readShort(this.xCoordinatesOffset + m);
        }
        m += 2;
      }
      if ((paramBoolean) && (i1 > 0)) {
        this.xCoordinates[i1] += this.xCoordinates[(i1 - 1)];
      }
      if ((i & 0x4) == 4)
      {
        if (paramBoolean)
        {
          this.yCoordinates[i1] = this.data.readUByte(this.yCoordinatesOffset + n);
          this.yCoordinates[i1] *= ((i & 0x20) == 32 ? 1 : -1);
        }
        n++;
      }
      else if ((i & 0x20) != 32)
      {
        if (paramBoolean) {
          this.yCoordinates[i1] = this.data.readShort(this.yCoordinatesOffset + n);
        }
        n += 2;
      }
      if ((paramBoolean) && (i1 > 0)) {
        this.yCoordinates[i1] += this.yCoordinates[(i1 - 1)];
      }
    }
    this.flagByteCount = k;
    this.xByteCount = m;
    this.yByteCount = n;
  }
  
  private int flagAsInt(int paramInt)
  {
    return this.data.readUByte(this.flagsOffset + paramInt * FontData.DataSize.BYTE.size());
  }
  
  public int contourEndPoint(int paramInt)
  {
    return this.data.readUShort(paramInt * FontData.DataSize.USHORT.size() + GlyphTable.Offset.simpleEndPtsOfCountours.offset);
  }
  
  public int instructionSize()
  {
    initialize();
    return this.instructionSize;
  }
  
  public ReadableFontData instructions()
  {
    initialize();
    return this.data.slice(this.instructionsOffset, instructionSize());
  }
  
  public int numberOfPoints(int paramInt)
  {
    initialize();
    if (paramInt >= numberOfContours()) {
      return 0;
    }
    return this.contourIndex[(paramInt + 1)] - this.contourIndex[paramInt];
  }
  
  public int xCoordinate(int paramInt1, int paramInt2)
  {
    initialize();
    return this.xCoordinates[(this.contourIndex[paramInt1] + paramInt2)];
  }
  
  public int yCoordinate(int paramInt1, int paramInt2)
  {
    initialize();
    return this.yCoordinates[(this.contourIndex[paramInt1] + paramInt2)];
  }
  
  public boolean onCurve(int paramInt1, int paramInt2)
  {
    initialize();
    return this.onCurve[(this.contourIndex[paramInt1] + paramInt2)];
  }
  
  public String toString()
  {
    initialize();
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    localStringBuilder.append("\tinstruction bytes = " + instructionSize() + "\n");
    for (int i = 0; i < numberOfContours(); i++) {
      for (int j = 0; j < numberOfPoints(i); j++) {
        localStringBuilder.append("\t" + i + ":" + j + " = [" + xCoordinate(i, j) + ", " + yCoordinate(i, j) + ", " + onCurve(i, j) + "]\n");
      }
    }
    return localStringBuilder.toString();
  }
  
  public static class SimpleGlyphBuilder
    extends Glyph.Builder<SimpleGlyph>
  {
    protected SimpleGlyphBuilder(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      super();
    }
    
    protected SimpleGlyphBuilder(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
    {
      super();
    }
    
    protected SimpleGlyph subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new SimpleGlyph(paramReadableFontData, 0, paramReadableFontData.length());
    }
  }
  
  public static final class SimpleContour
    extends Glyph.Contour
  {}
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\SimpleGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */