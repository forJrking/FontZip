package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.OutputStream;

public class FontOutputStream
  extends OutputStream
{
  private final OutputStream out;
  private long position;
  
  public FontOutputStream(OutputStream paramOutputStream)
  {
    this.out = paramOutputStream;
  }
  
  public long position()
  {
    return this.position;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    this.out.write(paramInt);
    this.position += 1L;
  }
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
      throw new IndexOutOfBoundsException();
    }
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
    this.position += paramInt2;
  }
  
  public void writeChar(byte paramByte)
    throws IOException
  {
    write(paramByte);
  }
  
  public void writeUShort(int paramInt)
    throws IOException
  {
    write((byte)(paramInt >> 8 & 0xFF));
    write((byte)(paramInt & 0xFF));
  }
  
  public void writeShort(int paramInt)
    throws IOException
  {
    writeUShort(paramInt);
  }
  
  public void writeUInt24(int paramInt)
    throws IOException
  {
    write((byte)(paramInt >> 16 & 0xFF));
    write((byte)(paramInt >> 8 & 0xFF));
    write((byte)(paramInt & 0xFF));
  }
  
  public void writeULong(long paramLong)
    throws IOException
  {
    write((byte)(int)(paramLong >> 24 & 0xFF));
    write((byte)(int)(paramLong >> 16 & 0xFF));
    write((byte)(int)(paramLong >> 8 & 0xFF));
    write((byte)(int)(paramLong & 0xFF));
  }
  
  public void writeLong(long paramLong)
    throws IOException
  {
    writeULong(paramLong);
  }
  
  public void writeFixed(int paramInt)
    throws IOException
  {
    writeULong(paramInt);
  }
  
  public void writeDateTime(long paramLong)
    throws IOException
  {
    writeULong(paramLong >> 32 & 0xFFFFFFFFFFFFFFFF);
    writeULong(paramLong & 0xFFFFFFFFFFFFFFFF);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\FontOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */