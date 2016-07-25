package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.FontHeaderTable;
import com.google.typography.font.sfntly.table.core.NameTable;
import com.google.typography.font.sfntly.table.core.OS2Table;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EOTWriter
{
  private final boolean compressed;
  private final FontFactory factory = FontFactory.getInstance();
  private static final long RESERVED = 0L;
  private static final short PADDING = 0;
  private static final long VERSION = 131074L;
  private static final short MAGIC_NUMBER = 20556;
  private static final long DEFAULT_FLAGS = 0L;
  private static final long FLAGS_TT_COMPRESSED = 4L;
  private static final byte DEFAULT_CHARSET = 1;
  private static final long CS_XORKEY = 1346851650L;
  
  public EOTWriter()
  {
    this.compressed = false;
  }
  
  public EOTWriter(boolean paramBoolean)
  {
    this.compressed = paramBoolean;
  }
  
  public WritableFontData convert(Font paramFont)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    this.factory.serializeFont(paramFont, localByteArrayOutputStream);
    byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    NameTable localNameTable = (NameTable)paramFont.getTable(Tag.name);
    byte[] arrayOfByte2 = convertUTF16StringToLittleEndian(localNameTable.nameAsBytes(3, 1, 1033, 1));
    byte[] arrayOfByte3 = convertUTF16StringToLittleEndian(localNameTable.nameAsBytes(3, 1, 1033, 2));
    byte[] arrayOfByte4 = convertUTF16StringToLittleEndian(localNameTable.nameAsBytes(3, 1, 1033, 5));
    byte[] arrayOfByte5 = convertUTF16StringToLittleEndian(localNameTable.nameAsBytes(3, 1, 1033, 4));
    long l1 = 0L;
    if (this.compressed)
    {
      l1 |= 0x4;
      MtxWriter localMtxWriter = new MtxWriter();
      arrayOfByte1 = localMtxWriter.compress(paramFont);
    }
    long l2 = computeEotSize(arrayOfByte2.length, arrayOfByte3.length, arrayOfByte4.length, arrayOfByte5.length, arrayOfByte1.length);
    WritableFontData localWritableFontData = createWritableFontData((int)l2);
    OS2Table localOS2Table = (OS2Table)paramFont.getTable(Tag.OS_2);
    int i = 0;
    i += localWritableFontData.writeULongLE(i, l2);
    i += localWritableFontData.writeULongLE(i, arrayOfByte1.length);
    i += localWritableFontData.writeULongLE(i, 131074L);
    i += localWritableFontData.writeULongLE(i, l1);
    i += writeFontPANOSE(i, localOS2Table, localWritableFontData);
    i += localWritableFontData.writeByte(i, (byte)1);
    i += localWritableFontData.writeByte(i, (byte)(localOS2Table.fsSelectionAsInt() & 0x1));
    i += localWritableFontData.writeULongLE(i, localOS2Table.usWeightClass());
    i += localWritableFontData.writeUShortLE(i, (short)localOS2Table.fsTypeAsInt());
    i += localWritableFontData.writeUShortLE(i, 20556);
    i += writeUnicodeRanges(i, localOS2Table, localWritableFontData);
    i += writeCodePages(i, localOS2Table, localWritableFontData);
    FontHeaderTable localFontHeaderTable = (FontHeaderTable)paramFont.getTable(Tag.head);
    i += localWritableFontData.writeULongLE(i, localFontHeaderTable.checkSumAdjustment());
    i += writeReservedFields(i, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += writeUTF16String(i, arrayOfByte2, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += writeUTF16String(i, arrayOfByte3, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += writeUTF16String(i, arrayOfByte4, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += writeUTF16String(i, arrayOfByte5, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += localWritableFontData.writeULongLE(i, 1346851650L);
    i += localWritableFontData.writeULongLE(i, 0L);
    i += writePadding(i, localWritableFontData);
    i += writePadding(i, localWritableFontData);
    i += localWritableFontData.writeULongLE(i, 0L);
    i += localWritableFontData.writeULongLE(i, 0L);
    localWritableFontData.writeBytes(i, arrayOfByte1, 0, arrayOfByte1.length);
    return localWritableFontData;
  }
  
  private long computeEotSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    return 16 * FontData.DataSize.ULONG.size() + 12 * FontData.DataSize.BYTE.size() + 12 * FontData.DataSize.USHORT.size() + paramInt1 * FontData.DataSize.BYTE.size() + paramInt2 * FontData.DataSize.BYTE.size() + paramInt3 * FontData.DataSize.BYTE.size() + paramInt4 * FontData.DataSize.BYTE.size() + paramInt5 * FontData.DataSize.BYTE.size() + 5 * FontData.DataSize.ULONG.size();
  }
  
  private int writeFontPANOSE(int paramInt, OS2Table paramOS2Table, WritableFontData paramWritableFontData)
  {
    byte[] arrayOfByte = paramOS2Table.panose();
    return paramWritableFontData.writeBytes(paramInt, arrayOfByte, 0, arrayOfByte.length);
  }
  
  private int writeReservedFields(int paramInt, WritableFontData paramWritableFontData)
  {
    int i = paramInt;
    for (int j = 0; j < 4; j++) {
      i += paramWritableFontData.writeULongLE(i, 0L);
    }
    return i - paramInt;
  }
  
  private int writeUnicodeRanges(int paramInt, OS2Table paramOS2Table, WritableFontData paramWritableFontData)
  {
    int i = paramInt;
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulUnicodeRange1());
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulUnicodeRange2());
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulUnicodeRange3());
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulUnicodeRange4());
    return i - paramInt;
  }
  
  private int writeCodePages(int paramInt, OS2Table paramOS2Table, WritableFontData paramWritableFontData)
  {
    int i = paramInt;
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulCodePageRange1());
    i += paramWritableFontData.writeULongLE(i, paramOS2Table.ulCodePageRange2());
    return i - paramInt;
  }
  
  private int writePadding(int paramInt, WritableFontData paramWritableFontData)
  {
    return paramWritableFontData.writeUShortLE(paramInt, 0);
  }
  
  private int writeUTF16String(int paramInt, byte[] paramArrayOfByte, WritableFontData paramWritableFontData)
  {
    int i = paramInt;
    i += paramWritableFontData.writeUShortLE(i, (short)paramArrayOfByte.length);
    i += paramWritableFontData.writeBytes(i, paramArrayOfByte, 0, paramArrayOfByte.length);
    return i - paramInt;
  }
  
  private byte[] convertUTF16StringToLittleEndian(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return new byte[0];
    }
    for (int i = 0; i < paramArrayOfByte.length; i += 2)
    {
      int j = paramArrayOfByte[i];
      paramArrayOfByte[i] = paramArrayOfByte[(i + 1)];
      paramArrayOfByte[(i + 1)] = j;
    }
    return paramArrayOfByte;
  }
  
  private WritableFontData createWritableFontData(int paramInt)
  {
    return WritableFontData.createWritableFontData(paramInt);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\EOTWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */