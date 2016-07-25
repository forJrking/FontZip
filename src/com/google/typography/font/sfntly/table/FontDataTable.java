package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.io.IOException;
import java.io.OutputStream;

public abstract class FontDataTable
{
  protected ReadableFontData data;
  
  FontDataTable(ReadableFontData paramReadableFontData)
  {
    this.data = paramReadableFontData;
  }
  
  public ReadableFontData readFontData()
  {
    return this.data;
  }
  
  public String toString()
  {
    return this.data.toString();
  }
  
  public final int dataLength()
  {
    return this.data.length();
  }
  
  public int serialize(OutputStream paramOutputStream)
    throws IOException
  {
    return this.data.copyTo(paramOutputStream);
  }
  
  protected int serialize(WritableFontData paramWritableFontData)
  {
    return this.data.copyTo(paramWritableFontData);
  }
  
  public static abstract class Builder<T extends FontDataTable>
  {
    private WritableFontData wData;
    private ReadableFontData rData;
    private boolean modelChanged;
    private boolean containedModelChanged;
    private boolean dataChanged;
    
    protected Builder(int paramInt)
    {
      this.wData = WritableFontData.createWritableFontData(paramInt);
    }
    
    protected Builder(WritableFontData paramWritableFontData)
    {
      this.wData = paramWritableFontData;
    }
    
    protected Builder(ReadableFontData paramReadableFontData)
    {
      this.rData = paramReadableFontData;
    }
    
    public WritableFontData data()
    {
      WritableFontData localWritableFontData;
      if (this.modelChanged)
      {
        if (!subReadyToSerialize()) {
          throw new RuntimeException("Table not ready to build.");
        }
        int i = subDataSizeToSerialize();
        localWritableFontData = WritableFontData.createWritableFontData(i);
        subSerialize(localWritableFontData);
      }
      else
      {
        ReadableFontData localReadableFontData = internalReadData();
        localWritableFontData = WritableFontData.createWritableFontData(localReadableFontData != null ? localReadableFontData.length() : 0);
        if (localReadableFontData != null) {
          localReadableFontData.copyTo(localWritableFontData);
        }
      }
      return localWritableFontData;
    }
    
    public void setData(WritableFontData paramWritableFontData)
    {
      internalSetData(paramWritableFontData, true);
    }
    
    public void setData(ReadableFontData paramReadableFontData)
    {
      internalSetData(paramReadableFontData, true);
    }
    
    private void internalSetData(WritableFontData paramWritableFontData, boolean paramBoolean)
    {
      this.wData = paramWritableFontData;
      this.rData = null;
      if (paramBoolean)
      {
        this.dataChanged = true;
        subDataSet();
      }
    }
    
    private void internalSetData(ReadableFontData paramReadableFontData, boolean paramBoolean)
    {
      this.rData = paramReadableFontData;
      this.wData = null;
      if (paramBoolean)
      {
        this.dataChanged = true;
        subDataSet();
      }
    }
    
    public T build()
    {
      FontDataTable localFontDataTable = null;
      Object localObject = internalReadData();
      if (this.modelChanged)
      {
        if (!subReadyToSerialize()) {
          return null;
        }
        int i = subDataSizeToSerialize();
        WritableFontData localWritableFontData = WritableFontData.createWritableFontData(i);
        subSerialize(localWritableFontData);
        localObject = localWritableFontData;
      }
      if (localObject != null)
      {
        localFontDataTable = subBuildTable((ReadableFontData)localObject);
        notifyPostTableBuild(localFontDataTable);
      }
      this.rData = null;
      this.wData = null;
      return localFontDataTable;
    }
    
    public boolean readyToBuild()
    {
      return true;
    }
    
    protected ReadableFontData internalReadData()
    {
      if (this.rData != null) {
        return this.rData;
      }
      return this.wData;
    }
    
    protected WritableFontData internalWriteData()
    {
      if (this.wData == null)
      {
        WritableFontData localWritableFontData = WritableFontData.createWritableFontData(this.rData == null ? 0 : this.rData.length());
        if (this.rData != null) {
          this.rData.copyTo(localWritableFontData);
        }
        internalSetData(localWritableFontData, false);
      }
      return this.wData;
    }
    
    public boolean changed()
    {
      return (dataChanged()) || (modelChanged());
    }
    
    protected boolean dataChanged()
    {
      return this.dataChanged;
    }
    
    protected boolean modelChanged()
    {
      return (currentModelChanged()) || (containedModelChanged());
    }
    
    protected boolean currentModelChanged()
    {
      return this.modelChanged;
    }
    
    protected boolean containedModelChanged()
    {
      return this.containedModelChanged;
    }
    
    protected boolean setModelChanged()
    {
      return setModelChanged(true);
    }
    
    protected boolean setModelChanged(boolean paramBoolean)
    {
      boolean bool = this.modelChanged;
      this.modelChanged = paramBoolean;
      return bool;
    }
    
    protected void notifyPostTableBuild(T paramT) {}
    
    protected abstract int subSerialize(WritableFontData paramWritableFontData);
    
    protected abstract boolean subReadyToSerialize();
    
    protected abstract int subDataSizeToSerialize();
    
    protected abstract void subDataSet();
    
    protected abstract T subBuildTable(ReadableFontData paramReadableFontData);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\FontDataTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */