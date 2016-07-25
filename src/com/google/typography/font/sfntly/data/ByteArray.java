package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class ByteArray<T extends ByteArray<T>>
{
  private static final int COPY_BUFFER_SIZE = 8192;
  private boolean bound;
  private int storageLength;
  private int filledLength;
  private boolean growable;
  
  protected ByteArray(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.storageLength = paramInt2;
    setFilledLength(paramInt1);
    this.growable = paramBoolean;
  }
  
  protected ByteArray(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, false);
  }
  
  public int get(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.filledLength)) {
      return -1;
    }
    return internalGet(paramInt) & 0xFF;
  }
  
  public int get(int paramInt, byte[] paramArrayOfByte)
  {
    return get(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int get(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt1 >= this.filledLength)) {
      return -1;
    }
    int i = Math.min(paramInt3, this.filledLength - paramInt1);
    return internalGet(paramInt1, paramArrayOfByte, paramInt2, i);
  }
  
  public int length()
  {
    return this.filledLength;
  }
  
  public int size()
  {
    return this.storageLength;
  }
  
  public final boolean growable()
  {
    return this.growable;
  }
  
  public int setFilledLength(int paramInt)
  {
    this.filledLength = Math.min(paramInt, this.storageLength);
    return this.filledLength;
  }
  
  public void put(int paramInt, byte paramByte)
  {
    if ((paramInt < 0) || (paramInt >= size())) {
      throw new IndexOutOfBoundsException("Attempt to write outside the bounds of the data.");
    }
    internalPut(paramInt, paramByte);
    this.filledLength = Math.max(this.filledLength, paramInt + 1);
  }
  
  public int put(int paramInt, byte[] paramArrayOfByte)
  {
    return put(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int put(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt1 >= size())) {
      throw new IndexOutOfBoundsException("Attempt to write outside the bounds of the data.");
    }
    int i = Math.min(paramInt3, size() - paramInt1);
    int j = internalPut(paramInt1, paramArrayOfByte, paramInt2, i);
    this.filledLength = Math.max(this.filledLength, paramInt1 + j);
    return j;
  }
  
  public int copyTo(ByteArray<? extends ByteArray<?>> paramByteArray)
  {
    return copyTo(paramByteArray, 0, length());
  }
  
  public int copyTo(ByteArray<? extends ByteArray<?>> paramByteArray, int paramInt1, int paramInt2)
  {
    return copyTo(0, paramByteArray, paramInt1, paramInt2);
  }
  
  public int copyTo(int paramInt1, ByteArray<? extends ByteArray<?>> paramByteArray, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte = new byte[' '];
    int i = 0;
    int j = 0;
    for (int k = Math.min(arrayOfByte.length, paramInt3); (i = get(j + paramInt2, arrayOfByte, 0, k)) > 0; k = Math.min(arrayOfByte.length, paramInt3))
    {
      int m = paramByteArray.put(j + paramInt1, arrayOfByte, 0, i);
      j += i;
      paramInt3 -= i;
    }
    return j;
  }
  
  public int copyTo(OutputStream paramOutputStream)
    throws IOException
  {
    return copyTo(paramOutputStream, 0, length());
  }
  
  public int copyTo(OutputStream paramOutputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    byte[] arrayOfByte = new byte[' '];
    int i = 0;
    int j = 0;
    for (int k = Math.min(arrayOfByte.length, paramInt2); (i = get(j + paramInt1, arrayOfByte, 0, k)) > 0; k = Math.min(arrayOfByte.length, paramInt2 - j))
    {
      paramOutputStream.write(arrayOfByte, 0, i);
      j += i;
    }
    return j;
  }
  
  public void copyFrom(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[' '];
    int i = 0;
    int j = 0;
    for (int k = Math.min(arrayOfByte.length, paramInt); (i = paramInputStream.read(arrayOfByte, 0, k)) > 0; k = Math.min(arrayOfByte.length, paramInt))
    {
      if (put(j, arrayOfByte, 0, i) != i) {
        throw new IOException("Error writing bytes.");
      }
      j += i;
      paramInt -= i;
    }
  }
  
  public void copyFrom(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[' '];
    int i = 0;
    int j = 0;
    int k = arrayOfByte.length;
    while ((i = paramInputStream.read(arrayOfByte, 0, k)) > 0)
    {
      if (put(j, arrayOfByte, 0, i) != i) {
        throw new IOException("Error writing bytes.");
      }
      j += i;
    }
  }
  
  protected abstract void internalPut(int paramInt, byte paramByte);
  
  protected abstract int internalPut(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
  
  protected abstract int internalGet(int paramInt);
  
  protected abstract int internalGet(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
  
  public abstract void close();
  
  public String toString(int paramInt1, int paramInt2)
  {
    if (paramInt2 == -1) {
      paramInt2 = length();
    }
    paramInt2 = Math.min(paramInt2, length());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[l=" + this.filledLength + ", s=" + size() + "]");
    if (paramInt2 > 0) {
      localStringBuilder.append("\n");
    }
    for (int i = 0; i < paramInt2; i++)
    {
      int j = get(i + paramInt1);
      if (j < 16) {
        localStringBuilder.append("0");
      }
      localStringBuilder.append(Integer.toHexString(j));
      localStringBuilder.append(" ");
      if ((i > 0) && ((i + 1) % 16 == 0)) {
        localStringBuilder.append("\n");
      }
    }
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    return toString(0, 0);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\ByteArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */