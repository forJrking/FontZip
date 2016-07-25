package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

public abstract class SubTable
  extends FontDataTable
{
  private final ReadableFontData masterData;
  private int padding = 0;
  
  protected SubTable(ReadableFontData paramReadableFontData1, ReadableFontData paramReadableFontData2)
  {
    super(paramReadableFontData1);
    this.masterData = paramReadableFontData2;
  }
  
  protected SubTable(ReadableFontData paramReadableFontData)
  {
    this(paramReadableFontData, null);
  }
  
  protected SubTable(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    this(paramReadableFontData.slice(paramInt1, paramInt2));
  }
  
  protected ReadableFontData masterReadData()
  {
    return this.masterData;
  }
  
  public int padding()
  {
    return this.padding;
  }
  
  protected void setPadding(int paramInt)
  {
    this.padding = paramInt;
  }
  
  protected static abstract class Builder<T extends SubTable>
    extends FontDataTable.Builder<T>
  {
    private ReadableFontData masterData;
    
    protected Builder(WritableFontData paramWritableFontData, ReadableFontData paramReadableFontData)
    {
      super();
      this.masterData = paramReadableFontData;
    }
    
    protected Builder(ReadableFontData paramReadableFontData1, ReadableFontData paramReadableFontData2)
    {
      super();
      this.masterData = paramReadableFontData2;
    }
    
    protected Builder(WritableFontData paramWritableFontData)
    {
      super();
    }
    
    protected Builder(ReadableFontData paramReadableFontData)
    {
      super();
    }
    
    protected Builder(int paramInt)
    {
      super();
    }
    
    protected ReadableFontData masterReadData()
    {
      return this.masterData;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\SubTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */