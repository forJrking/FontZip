package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class ReadableFontData
  extends FontData
{
  private volatile boolean checksumSet = false;
  private final Object checksumLock = new Object();
  private volatile long checksum;
  private volatile int[] checksumRange;
  
  public static ReadableFontData createReadableFontData(byte[] paramArrayOfByte)
  {
    MemoryByteArray localMemoryByteArray = new MemoryByteArray(paramArrayOfByte);
    return new ReadableFontData(localMemoryByteArray);
  }
  
  protected ReadableFontData(ByteArray<? extends ByteArray<?>> paramByteArray)
  {
    super(paramByteArray);
  }
  
  protected ReadableFontData(ReadableFontData paramReadableFontData, int paramInt)
  {
    super(paramReadableFontData, paramInt);
  }
  
  protected ReadableFontData(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    super(paramReadableFontData, paramInt1, paramInt2);
  }
  
  public ReadableFontData slice(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 + paramInt2 > size())) {
      throw new IndexOutOfBoundsException("Attempt to bind data outside of its limits.");
    }
    ReadableFontData localReadableFontData = new ReadableFontData(this, paramInt1, paramInt2);
    return localReadableFontData;
  }
  
  public ReadableFontData slice(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > size())) {
      throw new IndexOutOfBoundsException("Attempt to bind data outside of its limits.");
    }
    ReadableFontData localReadableFontData = new ReadableFontData(this, paramInt);
    return localReadableFontData;
  }
  
  public String toString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[l=" + length() + ", cs=" + checksum() + "]\n");
    localStringBuilder.append(this.array.toString(boundOffset(0), boundLength(0, paramInt)));
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    return toString(0);
  }
  
  public long checksum()
  {
    if (!this.checksumSet) {
      computeChecksum();
    }
    return this.checksum;
  }
  
  private void computeChecksum()
  {
    synchronized (this.checksumLock)
    {
      if (this.checksumSet) {
        return;
      }
      long l = 0L;
      if (this.checksumRange == null) {
        l = computeCheckSum(0, length());
      } else {
        for (int i = 0; i < this.checksumRange.length; i += 2)
        {
          int j = this.checksumRange[i];
          int k = i == this.checksumRange.length - 1 ? length() : this.checksumRange[(i + 1)];
          l += computeCheckSum(j, k);
        }
      }
      this.checksum = (l & 0xFFFFFFFF);
      this.checksumSet = true;
    }
  }
  
  private long computeCheckSum(int paramInt1, int paramInt2)
  {
    long l = 0L;
    for (int i = paramInt1; i <= paramInt2 - 4; i += 4) {
      l += readULong(i);
    }
    i = paramInt2 & 0xFFFFFFFC;
    if (i < paramInt2)
    {
      int j = readUByte(i);
      int k = i + 1 < paramInt2 ? readUByte(i + 1) : 0;
      int m = i + 2 < paramInt2 ? readUByte(i + 2) : 0;
      int n = 0;
      l += (j << 24 | k << 16 | m << 8 | n);
    }
    return l;
  }
  
  public void setCheckSumRanges(int... paramVarArgs)
  {
    synchronized (this.checksumLock)
    {
      if ((paramVarArgs != null) && (paramVarArgs.length > 0)) {
        this.checksumRange = Arrays.copyOf(paramVarArgs, paramVarArgs.length);
      } else {
        this.checksumRange = null;
      }
      this.checksumSet = false;
    }
  }
  
  public int[] checkSumRange()
  {
    synchronized (this.checksumLock)
    {
      if ((this.checksumRange != null) && (this.checksumRange.length > 0)) {
        return Arrays.copyOf(this.checksumRange, this.checksumRange.length);
      }
      return new int[0];
    }
  }
  
  public int readUByte(int paramInt)
  {
    if (!boundsCheck(paramInt, 1)) {
      throw new IndexOutOfBoundsException("Index attempted to be read from is out of bounds: " + Integer.toHexString(paramInt));
    }
    int i = this.array.get(boundOffset(paramInt));
    if (i < 0) {
      throw new IndexOutOfBoundsException("Index attempted to be read from is out of bounds: " + Integer.toHexString(paramInt));
    }
    return i;
  }
  
  public int readByte(int paramInt)
  {
    if (!boundsCheck(paramInt, 1)) {
      throw new IndexOutOfBoundsException("Index attempted to be read from is out of bounds: " + Integer.toHexString(paramInt));
    }
    int i = this.array.get(boundOffset(paramInt));
    if (i < 0) {
      throw new IndexOutOfBoundsException("Index attempted to be read from is out of bounds: " + Integer.toHexString(paramInt));
    }
    return i << 24 >> 24;
  }
  
  public int readBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    int i = this.array.get(boundOffset(paramInt1), paramArrayOfByte, paramInt2, boundLength(paramInt1, paramInt3));
    if (i < 0) {
      throw new IndexOutOfBoundsException("Index attempted to be read from is out of bounds: " + Integer.toHexString(paramInt1));
    }
    return i;
  }
  
  public int readChar(int paramInt)
  {
    return readUByte(paramInt);
  }
  
  public int readUShort(int paramInt)
  {
    return 0xFFFF & (readUByte(paramInt) << 8 | readUByte(paramInt + 1));
  }
  
  public int readShort(int paramInt)
  {
    return (readByte(paramInt) << 8 | readUByte(paramInt + 1)) << 16 >> 16;
  }
  
  public int readUInt24(int paramInt)
  {
    return 0xFFFFFF & (readUByte(paramInt) << 16 | readUByte(paramInt + 1) << 8 | readUByte(paramInt + 2));
  }
  
  public long readULong(int paramInt)
  {
    return 0xFFFFFFFF & (readUByte(paramInt) << 24 | readUByte(paramInt + 1) << 16 | readUByte(paramInt + 2) << 8 | readUByte(paramInt + 3));
  }
  
  public int readULongAsInt(int paramInt)
  {
    long l = readULong(paramInt);
    if ((l & 0xFFFFFFFF80000000) == -2147483648L) {
      throw new ArithmeticException("Long value too large to fit into an integer.");
    }
    return (int)l;
  }
  
  public long readULongLE(int paramInt)
  {
    return 0xFFFFFFFF & (readUByte(paramInt) | readUByte(paramInt + 1) << 8 | readUByte(paramInt + 2) << 16 | readUByte(paramInt + 3) << 24);
  }
  
  public int readLong(int paramInt)
  {
    return readByte(paramInt) << 24 | readUByte(paramInt + 1) << 16 | readUByte(paramInt + 2) << 8 | readUByte(paramInt + 3);
  }
  
  public int readFixed(int paramInt)
  {
    return readLong(paramInt);
  }
  
  public BigDecimal readF2Dot14(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public long readDateTimeAsLong(int paramInt)
  {
    return readULong(paramInt) << 32 | readULong(paramInt + 4);
  }
  
  public Date readLongDateTime(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public int readFUnit(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public int readFWord(int paramInt)
  {
    return readShort(paramInt);
  }
  
  public int readUFWord(int paramInt)
  {
    return readUShort(paramInt);
  }
  
  public int copyTo(OutputStream paramOutputStream)
    throws IOException
  {
    return this.array.copyTo(paramOutputStream, boundOffset(0), length());
  }
  
  public int copyTo(WritableFontData paramWritableFontData)
  {
    return this.array.copyTo(paramWritableFontData.boundOffset(0), paramWritableFontData.array, boundOffset(0), length());
  }
  
  public int searchUShort(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = 0;
    int j = 0;
    int k = paramInt5;
    while (k != j)
    {
      i = (k + j) / 2;
      int m = readUShort(paramInt1 + i * paramInt2);
      if (paramInt6 < m)
      {
        k = i;
      }
      else
      {
        int n = readUShort(paramInt3 + i * paramInt4);
        if (paramInt6 <= n) {
          return i;
        }
        j = i + 1;
      }
    }
    return -1;
  }
  
  public int searchULong(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = 0;
    int j = 0;
    int k = paramInt5;
    while (k != j)
    {
      i = (k + j) / 2;
      int m = readULongAsInt(paramInt1 + i * paramInt2);
      if (paramInt6 < m)
      {
        k = i;
      }
      else
      {
        int n = readULongAsInt(paramInt3 + i * paramInt4);
        if (paramInt6 <= n) {
          return i;
        }
        j = i + 1;
      }
    }
    return -1;
  }
  
  public int searchUShort(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    int j = 0;
    int k = paramInt3;
    while (k != j)
    {
      i = (k + j) / 2;
      int m = readUShort(paramInt1 + i * paramInt2);
      if (paramInt4 < m) {
        k = i;
      } else if (paramInt4 > m) {
        j = i + 1;
      } else {
        return i;
      }
    }
    return -1;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\ReadableFontData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */