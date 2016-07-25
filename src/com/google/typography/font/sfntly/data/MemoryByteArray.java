package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.OutputStream;

final class MemoryByteArray
  extends ByteArray<MemoryByteArray>
{
  private byte[] b;
  
  public MemoryByteArray(int paramInt)
  {
    this(new byte[paramInt], 0);
  }
  
  public MemoryByteArray(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, paramArrayOfByte.length);
  }
  
  public MemoryByteArray(byte[] paramArrayOfByte, int paramInt)
  {
    super(paramInt, paramArrayOfByte.length);
    this.b = paramArrayOfByte;
  }
  
  protected void internalPut(int paramInt, byte paramByte)
  {
    this.b[paramInt] = paramByte;
  }
  
  protected int internalPut(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    System.arraycopy(paramArrayOfByte, paramInt2, this.b, paramInt1, paramInt3);
    return paramInt3;
  }
  
  protected int internalGet(int paramInt)
  {
    return this.b[paramInt];
  }
  
  protected int internalGet(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    System.arraycopy(this.b, paramInt1, paramArrayOfByte, paramInt2, paramInt3);
    return paramInt3;
  }
  
  public void close()
  {
    this.b = null;
  }
  
  public int copyTo(OutputStream paramOutputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    paramOutputStream.write(this.b, paramInt1, paramInt2);
    return paramInt2;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\MemoryByteArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */