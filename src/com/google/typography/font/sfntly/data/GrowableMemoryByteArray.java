package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.OutputStream;

final class GrowableMemoryByteArray
  extends ByteArray<GrowableMemoryByteArray>
{
  private static final int INITIAL_LENGTH = 256;
  private byte[] b = new byte['Ā'];
  
  public GrowableMemoryByteArray()
  {
    super(0, Integer.MAX_VALUE, true);
  }
  
  protected void internalPut(int paramInt, byte paramByte)
  {
    growTo(paramInt + 1);
    this.b[paramInt] = paramByte;
  }
  
  protected int internalPut(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    growTo(paramInt1 + paramInt3);
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
  
  private void growTo(int paramInt)
  {
    if (paramInt <= this.b.length) {
      return;
    }
    paramInt = Math.max(paramInt, this.b.length * 2);
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.b, 0, arrayOfByte, 0, this.b.length);
    this.b = arrayOfByte;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\GrowableMemoryByteArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */