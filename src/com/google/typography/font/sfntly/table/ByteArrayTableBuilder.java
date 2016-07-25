package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.io.IOException;

public abstract class ByteArrayTableBuilder<T extends Table>
  extends TableBasedTableBuilder<T>
{
  protected ByteArrayTableBuilder(Header paramHeader, WritableFontData paramWritableFontData)
  {
    super(paramHeader, paramWritableFontData);
  }
  
  protected ByteArrayTableBuilder(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int byteValue(int paramInt)
    throws IOException
  {
    ReadableFontData localReadableFontData = internalReadData();
    if (localReadableFontData == null) {
      throw new IOException("No font data for the table.");
    }
    return localReadableFontData.readByte(paramInt);
  }
  
  public void setByteValue(int paramInt, byte paramByte)
    throws IOException
  {
    WritableFontData localWritableFontData = internalWriteData();
    if (localWritableFontData == null) {
      throw new IOException("No font data for the table.");
    }
    localWritableFontData.writeByte(paramInt, paramByte);
  }
  
  public int byteCount()
    throws IOException
  {
    ReadableFontData localReadableFontData = internalReadData();
    if (localReadableFontData == null) {
      throw new IOException("No font data for the table.");
    }
    return localReadableFontData.length();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\ByteArrayTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */