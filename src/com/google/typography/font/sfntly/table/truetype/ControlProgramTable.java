package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.ByteArrayTableBuilder;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;

public final class ControlProgramTable
  extends Table
{
  protected ControlProgramTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int data(int paramInt)
  {
    return this.data.readByte(paramInt);
  }
  
  public int byteCount()
  {
    return dataLength() / FontData.DataSize.BYTE.size();
  }
  
  public static class Builder
    extends ByteArrayTableBuilder<ControlProgramTable>
  {
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    protected ControlProgramTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new ControlProgramTable(header(), paramReadableFontData);
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\ControlProgramTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */