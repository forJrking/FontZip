package com.google.typography.font.sfntly.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontInputStream
  extends FilterInputStream
{
  private long position;
  private long length;
  private boolean bounded;
  
  public FontInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }
  
  public FontInputStream(InputStream paramInputStream, int paramInt)
  {
    this(paramInputStream);
    this.length = paramInt;
    this.bounded = true;
  }
  
  public int read()
    throws IOException
  {
    if ((this.bounded) && (this.position >= this.length)) {
      return -1;
    }
    int i = super.read();
    if (i >= 0) {
      this.position += 1L;
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if ((this.bounded) && (this.position >= this.length)) {
      return -1;
    }
    int i = this.bounded ? (int)Math.min(paramInt2, this.length - this.position) : paramInt2;
    int j = super.read(paramArrayOfByte, paramInt1, i);
    this.position += j;
    return j;
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public long position()
  {
    return this.position;
  }
  
  public int readChar()
    throws IOException
  {
    return read();
  }
  
  public int readUShort()
    throws IOException
  {
    return 0xFFFF & (read() << 8 | read());
  }
  
  public int readShort()
    throws IOException
  {
    return (read() << 8 | read()) << 16 >> 16;
  }
  
  public int readUInt24()
    throws IOException
  {
    return 0xFFFFFF & (read() << 16 | read() << 8 | read());
  }
  
  public long readULong()
    throws IOException
  {
    return 0xFFFFFFFF & readLong();
  }
  
  public int readULongAsInt()
    throws IOException
  {
    long l = readULong();
    if ((l & 0xFFFFFFFF80000000) == -2147483648L) {
      throw new ArithmeticException("Long value too large to fit into an integer.");
    }
    return (int)l & 0x7FFFFFFF;
  }
  
  public int readLong()
    throws IOException
  {
    return read() << 24 | read() << 16 | read() << 8 | read();
  }
  
  public int readFixed()
    throws IOException
  {
    return readLong();
  }
  
  public long readDateTimeAsLong()
    throws IOException
  {
    return readULong() << 32 | readULong();
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    long l = super.skip(paramLong);
    this.position += l;
    return l;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\FontInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */