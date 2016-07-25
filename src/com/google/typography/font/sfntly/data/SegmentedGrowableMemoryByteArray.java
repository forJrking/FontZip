package com.google.typography.font.sfntly.data;

import com.google.typography.font.sfntly.math.FontMath;
import java.util.ArrayList;
import java.util.List;

final class SegmentedGrowableMemoryByteArray
  extends ByteArray<SegmentedGrowableMemoryByteArray>
{
  private static final int DEFAULT_BUFFER_LOW_BITS = 8;
  private List<byte[]> buffers = new ArrayList();
  private final int lowBits;
  
  public SegmentedGrowableMemoryByteArray()
  {
    this(8);
  }
  
  public SegmentedGrowableMemoryByteArray(int paramInt)
  {
    super(0, Integer.MAX_VALUE, true);
    this.lowBits = paramInt;
  }
  
  protected void internalPut(int paramInt, byte paramByte)
  {
    int i = bufferIndex(paramInt);
    int j = bufferOffset(i, paramInt);
    byte[] arrayOfByte = buffer(i);
    arrayOfByte[j] = paramByte;
  }
  
  protected int internalPut(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    int i = 0;
    while (i < paramInt3)
    {
      int j = bufferIndex(paramInt1);
      int k = bufferOffset(j, paramInt1);
      byte[] arrayOfByte = buffer(j);
      int m = Math.min(paramInt3 - i, arrayOfByte.length - k);
      System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, k, m);
      paramInt1 += m;
      paramInt2 += m;
      i += m;
    }
    return i;
  }
  
  protected int internalGet(int paramInt)
  {
    int i = bufferIndex(paramInt);
    int j = bufferOffset(i, paramInt);
    byte[] arrayOfByte = buffer(i);
    return arrayOfByte[j];
  }
  
  protected int internalGet(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    int i = 0;
    while (i < paramInt3)
    {
      int j = bufferIndex(paramInt1);
      int k = bufferOffset(j, paramInt1);
      byte[] arrayOfByte = buffer(j);
      int m = Math.min(paramInt3 - i, arrayOfByte.length - k);
      System.arraycopy(arrayOfByte, k, paramArrayOfByte, paramInt2, m);
      paramInt1 += m;
      paramInt2 += m;
      i += m;
    }
    return i;
  }
  
  public void close()
  {
    this.buffers = null;
  }
  
  private int bufferOffset(int paramInt1, int paramInt2)
  {
    return paramInt2 & (1 << Math.max(this.lowBits, paramInt1 + this.lowBits - 1) ^ 0xFFFFFFFF);
  }
  
  private int bufferIndex(int paramInt)
  {
    return FontMath.log2(paramInt >> this.lowBits) + 1;
  }
  
  private byte[] buffer(int paramInt)
  {
    byte[] arrayOfByte = null;
    if (paramInt >= this.buffers.size()) {
      for (int i = this.buffers.size(); i < paramInt + 1; i++)
      {
        int j = 1 << Math.max(0, i - 1) + this.lowBits;
        arrayOfByte = new byte[j];
        this.buffers.add(arrayOfByte);
      }
    }
    arrayOfByte = (byte[])this.buffers.get(paramInt);
    return arrayOfByte;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\SegmentedGrowableMemoryByteArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */