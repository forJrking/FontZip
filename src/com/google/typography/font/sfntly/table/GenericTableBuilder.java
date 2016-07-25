package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

final class GenericTableBuilder
  extends TableBasedTableBuilder<Table>
{
  static GenericTableBuilder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
  {
    return new GenericTableBuilder(paramHeader, paramWritableFontData);
  }
  
  private GenericTableBuilder(Header paramHeader, WritableFontData paramWritableFontData)
  {
    super(paramHeader, paramWritableFontData);
  }
  
  private GenericTableBuilder(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  protected Table subBuildTable(ReadableFontData paramReadableFontData)
  {
    return new Table(header(), internalReadData());
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\GenericTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */