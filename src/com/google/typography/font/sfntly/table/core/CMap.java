package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.SubTable.Builder;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class CMap
  extends SubTable
  implements Iterable<Integer>
{
  protected final int format;
  protected final CMapTable.CMapId cmapId;
  
  protected CMap(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData);
    this.format = paramInt;
    this.cmapId = paramCMapId;
  }
  
  public int format()
  {
    return this.format;
  }
  
  public CMapTable.CMapId cmapId()
  {
    return this.cmapId;
  }
  
  public int platformId()
  {
    return cmapId().platformId();
  }
  
  public int encodingId()
  {
    return cmapId().encodingId();
  }
  
  public int hashCode()
  {
    return this.cmapId.hashCode();
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CMap)) {
      return false;
    }
    return this.cmapId.equals(((CMap)paramObject).cmapId);
  }
  
  public abstract int language();
  
  public abstract int glyphId(int paramInt);
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cmap: ");
    localStringBuilder.append(cmapId());
    localStringBuilder.append(", ");
    localStringBuilder.append(CMapFormat.valueOf(format()));
    localStringBuilder.append(", Data Size=0x");
    localStringBuilder.append(Integer.toHexString(this.data.length()));
    return localStringBuilder.toString();
  }
  
  public static abstract class Builder<T extends CMap>
    extends SubTable.Builder<T>
  {
    private final CMap.CMapFormat format;
    private final CMapTable.CMapId cmapId;
    private int language;
    
    protected Builder(ReadableFontData paramReadableFontData, CMap.CMapFormat paramCMapFormat, CMapTable.CMapId paramCMapId)
    {
      super();
      this.format = paramCMapFormat;
      this.cmapId = paramCMapId;
    }
    
    public CMapTable.CMapId cmapId()
    {
      return this.cmapId;
    }
    
    public int encodingId()
    {
      return cmapId().encodingId();
    }
    
    public int platformId()
    {
      return cmapId().platformId();
    }
    
    public CMap.CMapFormat format()
    {
      return this.format;
    }
    
    public int language()
    {
      return this.language;
    }
    
    public void setLanguage(int paramInt)
    {
      this.language = paramInt;
    }
    
    protected Builder(WritableFontData paramWritableFontData, CMap.CMapFormat paramCMapFormat, CMapTable.CMapId paramCMapId)
    {
      super();
      this.format = paramCMapFormat;
      this.cmapId = paramCMapId;
    }
    
    protected void subDataSet() {}
    
    protected int subDataSizeToSerialize()
    {
      return internalReadData().length();
    }
    
    protected boolean subReadyToSerialize()
    {
      return true;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      return internalReadData().copyTo(paramWritableFontData);
    }
    
    static Builder<? extends CMap> getBuilder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      int i = paramReadableFontData.readUShort(paramInt);
      CMap.CMapFormat localCMapFormat = CMap.CMapFormat.valueOf(i);
      switch (CMap.1.$SwitchMap$com$google$typography$font$sfntly$table$core$CMap$CMapFormat[localCMapFormat.ordinal()])
      {
      case 1: 
        return new CMapFormat0.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 2: 
        return new CMapFormat2.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 3: 
        return new CMapFormat4.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 4: 
        return new CMapFormat6.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 5: 
        return new CMapFormat8.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 6: 
        return new CMapFormat10.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 7: 
        return new CMapFormat12.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 8: 
        return new CMapFormat13.Builder(paramReadableFontData, paramInt, paramCMapId);
      case 9: 
        return new CMapFormat14.Builder(paramReadableFontData, paramInt, paramCMapId);
      }
      return null;
    }
    
    static Builder<? extends CMap> getBuilder(CMap.CMapFormat paramCMapFormat, CMapTable.CMapId paramCMapId)
    {
      switch (CMap.1.$SwitchMap$com$google$typography$font$sfntly$table$core$CMap$CMapFormat[paramCMapFormat.ordinal()])
      {
      case 1: 
        return new CMapFormat0.Builder(null, 0, paramCMapId);
      case 3: 
        return new CMapFormat4.Builder(null, 0, paramCMapId);
      }
      return null;
    }
    
    public String toString()
    {
      return String.format("%s, format = %s", new Object[] { cmapId(), format() });
    }
  }
  
  protected class CharacterIterator
    implements Iterator<Integer>
  {
    private int character = 0;
    private final int maxCharacter;
    
    CharacterIterator(int paramInt1, int paramInt2)
    {
      this.character = paramInt1;
      this.maxCharacter = paramInt2;
    }
    
    public boolean hasNext()
    {
      return this.character < this.maxCharacter;
    }
    
    public Integer next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException("No more characters to iterate.");
      }
      return Integer.valueOf(this.character++);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Unable to remove a character from cmap.");
    }
  }
  
  public static enum CMapFormat
  {
    Format0(0),  Format2(2),  Format4(4),  Format6(6),  Format8(8),  Format10(10),  Format12(12),  Format13(13),  Format14(14);
    
    final int value;
    
    private CMapFormat(int paramInt)
    {
      this.value = paramInt;
    }
    
    public int value()
    {
      return this.value;
    }
    
    public boolean equals(int paramInt)
    {
      return paramInt == this.value;
    }
    
    public static CMapFormat valueOf(int paramInt)
    {
      for (CMapFormat localCMapFormat : ) {
        if (localCMapFormat.equals(paramInt)) {
          return localCMapFormat;
        }
      }
      return null;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */