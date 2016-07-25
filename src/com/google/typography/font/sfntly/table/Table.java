package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.bitmap.EbdtTable.Builder;
import com.google.typography.font.sfntly.table.bitmap.EblcTable.Builder;
import com.google.typography.font.sfntly.table.bitmap.EbscTable.Builder;
import com.google.typography.font.sfntly.table.core.CMapTable.Builder;
import com.google.typography.font.sfntly.table.core.FontHeaderTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalDeviceMetricsTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalHeaderTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalMetricsTable.Builder;
import com.google.typography.font.sfntly.table.core.MaximumProfileTable.Builder;
import com.google.typography.font.sfntly.table.core.NameTable.Builder;
import com.google.typography.font.sfntly.table.core.OS2Table.Builder;
import com.google.typography.font.sfntly.table.core.PostScriptTable.Builder;
import com.google.typography.font.sfntly.table.truetype.ControlProgramTable.Builder;
import com.google.typography.font.sfntly.table.truetype.ControlValueTable.Builder;
import com.google.typography.font.sfntly.table.truetype.GlyphTable.Builder;
import com.google.typography.font.sfntly.table.truetype.LocaTable.Builder;

public class Table
  extends FontDataTable
{
  private Header header;
  
  protected Table(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramReadableFontData);
    this.header = paramHeader;
  }
  
  public long calculatedChecksum()
  {
    return this.data.checksum();
  }
  
  public Header header()
  {
    return this.header;
  }
  
  public int headerTag()
  {
    return header().tag();
  }
  
  public int headerOffset()
  {
    return header().offset();
  }
  
  public int headerLength()
  {
    return header().length();
  }
  
  public long headerChecksum()
  {
    return header().checksum();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(Tag.stringValue(this.header.tag()));
    localStringBuilder.append(", cs=0x");
    localStringBuilder.append(Long.toHexString(this.header.checksum()));
    localStringBuilder.append(", offset=0x");
    localStringBuilder.append(Integer.toHexString(this.header.offset()));
    localStringBuilder.append(", size=0x");
    localStringBuilder.append(Integer.toHexString(this.header.length()));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public static abstract class Builder<T extends Table>
    extends FontDataTable.Builder<T>
  {
    private Header header;
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super();
      this.header = paramHeader;
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super();
      this.header = paramHeader;
    }
    
    protected Builder(Header paramHeader)
    {
      this(paramHeader, null);
    }
    
    public String toString()
    {
      return "Table Builder for - " + this.header.toString();
    }
    
    public final Header header()
    {
      return this.header;
    }
    
    protected void notifyPostTableBuild(T paramT)
    {
      if ((modelChanged()) || (dataChanged()))
      {
        Header localHeader = new Header(header().tag(), paramT.dataLength());
        paramT.header = localHeader;
      }
    }
    
    public static Builder<? extends Table> getBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      int i = paramHeader.tag();
      if (i == Tag.cmap) {
        return CMapTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.head) {
        return FontHeaderTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.hhea) {
        return HorizontalHeaderTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.hmtx) {
        return HorizontalMetricsTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.maxp) {
        return MaximumProfileTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.name) {
        return NameTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.OS_2) {
        return OS2Table.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.post) {
        return PostScriptTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.cvt) {
        return ControlValueTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.glyf) {
        return GlyphTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.loca) {
        return LocaTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.prep) {
        return ControlProgramTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.EBDT) {
        return EbdtTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.EBLC) {
        return EblcTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.EBSC) {
        return EbscTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.hdmx) {
        return HorizontalDeviceMetricsTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.bhed) {
        return FontHeaderTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.bdat) {
        return EbdtTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      if (i == Tag.bloc) {
        return EblcTable.Builder.createBuilder(paramHeader, paramWritableFontData);
      }
      return GenericTableBuilder.createBuilder(paramHeader, paramWritableFontData);
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */