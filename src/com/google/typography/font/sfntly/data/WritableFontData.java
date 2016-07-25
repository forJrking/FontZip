package com.google.typography.font.sfntly.data;

import java.io.IOException;
import java.io.InputStream;

public final class WritableFontData
  extends ReadableFontData
{
  public static final WritableFontData createWritableFontData(int paramInt)
  {
    Object localObject = null;
    if (paramInt > 0)
    {
      localObject = new MemoryByteArray(paramInt);
      ((ByteArray)localObject).setFilledLength(paramInt);
    }
    else
    {
      localObject = new GrowableMemoryByteArray();
    }
    WritableFontData localWritableFontData = new WritableFontData((ByteArray)localObject);
    return localWritableFontData;
  }
  
  public static final WritableFontData createWritableFontData(byte[] paramArrayOfByte)
  {
    MemoryByteArray localMemoryByteArray = new MemoryByteArray(paramArrayOfByte);
    WritableFontData localWritableFontData = new WritableFontData(localMemoryByteArray);
    return localWritableFontData;
  }
  
  public static final WritableFontData createWritableFontData(ReadableFontData paramReadableFontData)
  {
    Object localObject = null;
    if (paramReadableFontData.array.growable()) {
      localObject = new GrowableMemoryByteArray();
    } else {
      localObject = new MemoryByteArray(paramReadableFontData.array.length());
    }
    paramReadableFontData.array.copyTo((ByteArray)localObject);
    WritableFontData localWritableFontData = new WritableFontData((ByteArray)localObject);
    localWritableFontData.setCheckSumRanges(paramReadableFontData.checkSumRange());
    return localWritableFontData;
  }
  
  private WritableFontData(ByteArray<? extends ByteArray<?>> paramByteArray)
  {
    super(paramByteArray);
  }
  
  private WritableFontData(WritableFontData paramWritableFontData, int paramInt)
  {
    super(paramWritableFontData, paramInt);
  }
  
  private WritableFontData(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
  {
    super(paramWritableFontData, paramInt1, paramInt2);
  }
  
  public WritableFontData slice(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 + paramInt2 > size())) {
      throw new IndexOutOfBoundsException("Attempt to bind data outside of its limits.");
    }
    WritableFontData localWritableFontData = new WritableFontData(this, paramInt1, paramInt2);
    return localWritableFontData;
  }
  
  public WritableFontData slice(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > size())) {
      throw new IndexOutOfBoundsException("Attempt to bind data outside of its limits.");
    }
    WritableFontData localWritableFontData = new WritableFontData(this, paramInt);
    return localWritableFontData;
  }
  
  public int writeByte(int paramInt, byte paramByte)
  {
    this.array.put(boundOffset(paramInt), paramByte);
    return 1;
  }
  
  public int writeBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    return this.array.put(boundOffset(paramInt1), paramArrayOfByte, paramInt2, boundLength(paramInt1, paramInt3));
  }
  
  public int writeBytesPad(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, byte paramByte)
  {
    int i = this.array.put(boundOffset(paramInt1), paramArrayOfByte, paramInt2, boundLength(paramInt1, Math.min(paramInt3, paramArrayOfByte.length - paramInt2)));
    i += writePadding(i + paramInt1, paramInt3 - i, paramByte);
    return i;
  }
  
  public int writePadding(int paramInt1, int paramInt2)
  {
    return writePadding(paramInt1, paramInt2, (byte)0);
  }
  
  public int writePadding(int paramInt1, int paramInt2, byte paramByte)
  {
    for (int i = 0; i < paramInt2; i++) {
      this.array.put(paramInt1 + i, paramByte);
    }
    return paramInt2;
  }
  
  public int writeBytes(int paramInt, byte[] paramArrayOfByte)
  {
    return writeBytes(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int writeChar(int paramInt, byte paramByte)
  {
    return writeByte(paramInt, paramByte);
  }
  
  public int writeUShort(int paramInt1, int paramInt2)
  {
    writeByte(paramInt1, (byte)(paramInt2 >> 8 & 0xFF));
    writeByte(paramInt1 + 1, (byte)(paramInt2 & 0xFF));
    return 2;
  }
  
  public int writeUShortLE(int paramInt1, int paramInt2)
  {
    this.array.put(paramInt1, (byte)(paramInt2 & 0xFF));
    this.array.put(paramInt1 + 1, (byte)(paramInt2 >> 8 & 0xFF));
    return 2;
  }
  
  public int writeShort(int paramInt1, int paramInt2)
  {
    return writeUShort(paramInt1, paramInt2);
  }
  
  public int writeUInt24(int paramInt1, int paramInt2)
  {
    writeByte(paramInt1, (byte)(paramInt2 >> 16 & 0xFF));
    writeByte(paramInt1 + 1, (byte)(paramInt2 >> 8 & 0xFF));
    writeByte(paramInt1 + 2, (byte)(paramInt2 & 0xFF));
    return 3;
  }
  
  public int writeULong(int paramInt, long paramLong)
  {
    writeByte(paramInt, (byte)(int)(paramLong >> 24 & 0xFF));
    writeByte(paramInt + 1, (byte)(int)(paramLong >> 16 & 0xFF));
    writeByte(paramInt + 2, (byte)(int)(paramLong >> 8 & 0xFF));
    writeByte(paramInt + 3, (byte)(int)(paramLong & 0xFF));
    return 4;
  }
  
  public int writeULongLE(int paramInt, long paramLong)
  {
    this.array.put(paramInt, (byte)(int)(paramLong & 0xFF));
    this.array.put(paramInt + 1, (byte)(int)(paramLong >> 8 & 0xFF));
    this.array.put(paramInt + 2, (byte)(int)(paramLong >> 16 & 0xFF));
    this.array.put(paramInt + 3, (byte)(int)(paramLong >> 24 & 0xFF));
    return 4;
  }
  
  public int writeLong(int paramInt, long paramLong)
  {
    return writeULong(paramInt, paramLong);
  }
  
  public int writeFixed(int paramInt1, int paramInt2)
  {
    return writeLong(paramInt1, paramInt2);
  }
  
  public int writeDateTime(int paramInt, long paramLong)
  {
    writeULong(paramInt, paramLong >> 32 & 0xFFFFFFFFFFFFFFFF);
    writeULong(paramInt + 4, paramLong & 0xFFFFFFFFFFFFFFFF);
    return 8;
  }
  
  public void copyFrom(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    this.array.copyFrom(paramInputStream, paramInt);
  }
  
  public void copyFrom(InputStream paramInputStream)
    throws IOException
  {
    this.array.copyFrom(paramInputStream);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\data\WritableFontData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */