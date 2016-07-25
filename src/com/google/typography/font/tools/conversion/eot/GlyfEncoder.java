package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.table.truetype.CompositeGlyph;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.GlyphTable;
import com.google.typography.font.sfntly.table.truetype.LocaTable;
import com.google.typography.font.sfntly.table.truetype.SimpleGlyph;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GlyfEncoder
{
  private final boolean doPush;
  private final ByteArrayOutputStream glyfStream;
  private final ByteArrayOutputStream pushStream;
  private final ByteArrayOutputStream codeStream;
  
  public GlyfEncoder(boolean paramBoolean)
  {
    this.doPush = paramBoolean;
    this.glyfStream = new ByteArrayOutputStream();
    this.pushStream = new ByteArrayOutputStream();
    this.codeStream = new ByteArrayOutputStream();
  }
  
  public GlyfEncoder()
  {
    this(true);
  }
  
  public void encode(Font paramFont)
  {
    LocaTable localLocaTable = (LocaTable)paramFont.getTable(Tag.loca);
    int i = localLocaTable.numGlyphs();
    GlyphTable localGlyphTable = (GlyphTable)paramFont.getTable(Tag.glyf);
    for (int j = 0; j < i; j++)
    {
      int k = localLocaTable.glyphOffset(j);
      int m = localLocaTable.glyphLength(j);
      Glyph localGlyph = localGlyphTable.glyph(k, m);
      writeGlyph(localGlyph);
    }
  }
  
  private void writeGlyph(Glyph paramGlyph)
  {
    try
    {
      if ((paramGlyph == null) || (paramGlyph.dataLength() == 0)) {
        writeUShort(0);
      } else if ((paramGlyph instanceof SimpleGlyph)) {
        writeSimpleGlyph((SimpleGlyph)paramGlyph);
      } else if ((paramGlyph instanceof CompositeGlyph)) {
        writeCompositeGlyph((CompositeGlyph)paramGlyph);
      }
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("unexpected IOException writing glyph data", localIOException);
    }
  }
  
  private void writeInstructions(Glyph paramGlyph)
    throws IOException
  {
    if (this.doPush)
    {
      splitPush(paramGlyph);
    }
    else
    {
      int i = 0;
      int j = paramGlyph.instructionSize();
      write255UShort(this.glyfStream, i);
      write255UShort(this.glyfStream, j);
      if (j > 0) {
        paramGlyph.instructions().copyTo(this.codeStream);
      }
    }
  }
  
  private void writeSimpleGlyph(SimpleGlyph paramSimpleGlyph)
    throws IOException
  {
    int i = paramSimpleGlyph.numberOfContours();
    writeUShort(i);
    for (int j = 0; j < i; j++) {
      write255UShort(this.glyfStream, paramSimpleGlyph.numberOfPoints(j) - (j == 0 ? 1 : 0));
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int k = 0;
    int m = 0;
    for (int n = 0; n < i; n++)
    {
      int i1 = paramSimpleGlyph.numberOfPoints(n);
      for (int i2 = 0; i2 < i1; i2++)
      {
        int i3 = paramSimpleGlyph.xCoordinate(n, i2);
        int i4 = paramSimpleGlyph.yCoordinate(n, i2);
        int i5 = i3 - k;
        int i6 = i4 - m;
        writeTriplet(localByteArrayOutputStream, paramSimpleGlyph.onCurve(n, i2), i5, i6);
        k = i3;
        m = i4;
      }
    }
    localByteArrayOutputStream.writeTo(this.glyfStream);
    if (i > 0) {
      writeInstructions(paramSimpleGlyph);
    }
  }
  
  private void writeCompositeGlyph(CompositeGlyph paramCompositeGlyph)
    throws IOException
  {
    int i = 0;
    writeUShort(-1);
    writeUShort(paramCompositeGlyph.xMin());
    writeUShort(paramCompositeGlyph.yMin());
    writeUShort(paramCompositeGlyph.xMax());
    writeUShort(paramCompositeGlyph.yMax());
    for (int j = 0; j < paramCompositeGlyph.numGlyphs(); j++)
    {
      int k = paramCompositeGlyph.flags(j);
      writeUShort(k);
      i = (k & 0x100) != 0 ? 1 : 0;
      writeUShort(paramCompositeGlyph.glyphIndex(j));
      if ((k & 0x1) == 0)
      {
        this.glyfStream.write(paramCompositeGlyph.argument1(j));
        this.glyfStream.write(paramCompositeGlyph.argument2(j));
      }
      else
      {
        writeUShort(paramCompositeGlyph.argument1(j));
        writeUShort(paramCompositeGlyph.argument2(j));
      }
      if (paramCompositeGlyph.transformationSize(j) != 0) {
        try
        {
          this.glyfStream.write(paramCompositeGlyph.transformation(j));
        }
        catch (IOException localIOException) {}
      }
    }
    if (i != 0) {
      writeInstructions(paramCompositeGlyph);
    }
  }
  
  private void writeUShort(int paramInt)
  {
    this.glyfStream.write(paramInt >> 8);
    this.glyfStream.write(paramInt & 0xFF);
  }
  
  static void write255UShort(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException();
    }
    if (paramInt < 253)
    {
      paramOutputStream.write((byte)paramInt);
    }
    else if (paramInt < 506)
    {
      paramOutputStream.write(255);
      paramOutputStream.write((byte)(paramInt - 253));
    }
    else if (paramInt < 762)
    {
      paramOutputStream.write(254);
      paramOutputStream.write((byte)(paramInt - 506));
    }
    else
    {
      paramOutputStream.write(253);
      paramOutputStream.write((byte)(paramInt >> 8));
      paramOutputStream.write((byte)(paramInt & 0xFF));
    }
  }
  
  static void write255Short(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    int i = Math.abs(paramInt);
    if (paramInt < 0) {
      paramOutputStream.write(250);
    }
    if (i < 250)
    {
      paramOutputStream.write((byte)i);
    }
    else if (i < 500)
    {
      paramOutputStream.write(255);
      paramOutputStream.write((byte)(i - 250));
    }
    else if (i < 756)
    {
      paramOutputStream.write(254);
      paramOutputStream.write((byte)(i - 500));
    }
    else
    {
      paramOutputStream.write(253);
      paramOutputStream.write((byte)(i >> 8));
      paramOutputStream.write((byte)(i & 0xFF));
    }
  }
  
  void writeTriplet(OutputStream paramOutputStream, boolean paramBoolean, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = paramBoolean ? 0 : 128;
    int m = paramInt1 < 0 ? 0 : 1;
    int n = paramInt2 < 0 ? 0 : 1;
    int i1 = m + 2 * n;
    if ((paramInt1 == 0) && (j < 1280))
    {
      this.glyfStream.write(k + ((j & 0xF00) >> 7) + n);
      paramOutputStream.write(j & 0xFF);
    }
    else if ((paramInt2 == 0) && (i < 1280))
    {
      this.glyfStream.write(k + 10 + ((i & 0xF00) >> 7) + m);
      paramOutputStream.write(i & 0xFF);
    }
    else if ((i < 65) && (j < 65))
    {
      this.glyfStream.write(k + 20 + (i - 1 & 0x30) + ((j - 1 & 0x30) >> 2) + i1);
      paramOutputStream.write((i - 1 & 0xF) << 4 | j - 1 & 0xF);
    }
    else if ((i < 769) && (j < 769))
    {
      this.glyfStream.write(k + 84 + 12 * ((i - 1 & 0x300) >> 8) + ((j - 1 & 0x300) >> 6) + i1);
      paramOutputStream.write(i - 1 & 0xFF);
      paramOutputStream.write(j - 1 & 0xFF);
    }
    else if ((i < 4096) && (j < 4096))
    {
      this.glyfStream.write(k + 120 + i1);
      paramOutputStream.write(i >> 4);
      paramOutputStream.write((i & 0xF) << 4 | j >> 8);
      paramOutputStream.write(j & 0xFF);
    }
    else
    {
      this.glyfStream.write(k + 124 + i1);
      paramOutputStream.write(i >> 8);
      paramOutputStream.write(i & 0xFF);
      paramOutputStream.write(j >> 8);
      paramOutputStream.write(j & 0xFF);
    }
  }
  
  private void splitPush(Glyph paramGlyph)
    throws IOException
  {
    int i = paramGlyph.instructionSize();
    ReadableFontData localReadableFontData = paramGlyph.instructions();
    int j = 0;
    ArrayList localArrayList = new ArrayList();
    while (j + 1 < i)
    {
      k = j;
      m = localReadableFontData.readUByte(k++);
      int n = 0;
      int i1 = 0;
      if ((m == 64) || (m == 65))
      {
        n = localReadableFontData.readUByte(k++);
        i1 = (m & 0x1) + 1;
      }
      else
      {
        if ((m < 176) || (m >= 192)) {
          break;
        }
        n = 1 + (m & 0x7);
        i1 = ((m & 0x8) >> 3) + 1;
      }
      if (j + i1 * n > i) {
        break;
      }
      for (int i2 = 0; i2 < n; i2++)
      {
        if (i1 == 1) {
          localArrayList.add(Integer.valueOf(localReadableFontData.readUByte(k)));
        } else {
          localArrayList.add(Integer.valueOf(localReadableFontData.readShort(k)));
        }
        k += i1;
      }
      j = k;
    }
    int k = localArrayList.size();
    int m = i - j;
    write255UShort(this.glyfStream, k);
    write255UShort(this.glyfStream, m);
    encodePushSequence(this.pushStream, localArrayList);
    if (m > 0) {
      localReadableFontData.slice(j).copyTo(this.codeStream);
    }
  }
  
  private void encodePushSequence(OutputStream paramOutputStream, List<Integer> paramList)
    throws IOException
  {
    int i = paramList.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      if ((j & 0x1) == 0)
      {
        int m = ((Integer)paramList.get(k)).intValue();
        if ((j == 0) && (k >= 2) && (k + 2 < i) && (m == ((Integer)paramList.get(k - 2)).intValue()) && (m == ((Integer)paramList.get(k + 2)).intValue()))
        {
          if ((k + 4 < i) && (m == ((Integer)paramList.get(k + 4)).intValue()))
          {
            paramOutputStream.write(252);
            j = 20;
          }
          else
          {
            paramOutputStream.write(251);
            j = 4;
          }
        }
        else {
          write255Short(paramOutputStream, ((Integer)paramList.get(k)).intValue());
        }
      }
      j >>= 1;
    }
  }
  
  public byte[] getGlyfBytes()
  {
    return this.glyfStream.toByteArray();
  }
  
  public byte[] getPushBytes()
  {
    return this.pushStream.toByteArray();
  }
  
  public byte[] getCodeBytes()
  {
    return this.codeStream.toByteArray();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\GlyfEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */