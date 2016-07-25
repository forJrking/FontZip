package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public abstract class TableBasedTableBuilder<T extends Table>
  extends Table.Builder<T>
{
  private T table;
  
  protected TableBasedTableBuilder(Header paramHeader, WritableFontData paramWritableFontData)
  {
    super(paramHeader, paramWritableFontData);
  }
  
  protected TableBasedTableBuilder(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  protected TableBasedTableBuilder(Header paramHeader)
  {
    super(paramHeader);
  }
  
  protected T table()
  {
    if (this.table == null) {
      this.table = ((Table)subBuildTable(internalReadData()));
    }
    return this.table;
  }
  
  protected void subDataSet()
  {
    this.table = null;
  }
  
  protected int subDataSizeToSerialize()
  {
    return 0;
  }
  
  protected boolean subReadyToSerialize()
  {
    return true;
  }
  
  protected int subSerialize(WritableFontData paramWritableFontData)
  {
    return 0;
  }
  
  public T build()
  {
    if (!subReadyToSerialize()) {
      return null;
    }
    Table localTable = table();
    notifyPostTableBuild(localTable);
    return localTable;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\TableBasedTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */