package com.google.typography.font.tools.conversion.woff;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.core.FontHeaderTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.Deflater;

public class WoffWriter
{
  protected boolean woff_compression_faster = false;
  private static final long SIGNATURE = 2001684038L;
  private static final int WOFF_HEADER_SIZE = 9 * FontData.DataSize.ULONG.size() + 4 * FontData.DataSize.USHORT.size();
  
  public WritableFontData convert(Font paramFont)
  {
    List localList = createTableDirectoryEntries(paramFont);
    int i = WOFF_HEADER_SIZE + computeTableDirectoryEntriesLength(localList) + computeTablesLength(localList);
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(i);
    int j = 0;
    j += writeWoffHeader(localWritableFontData, j, localList, paramFont.sfntVersion(), i, extractMajorVersion(paramFont), extractMinorVersion(paramFont));
    j += writeTableDirectoryEntries(localWritableFontData, j, localList);
    j += writeTables(localWritableFontData, j, localList);
    return localWritableFontData;
  }
  
  private int extractMajorVersion(Font paramFont)
  {
    FontHeaderTable localFontHeaderTable = (FontHeaderTable)paramFont.getTable(Tag.head);
    return localFontHeaderTable.fontRevision() >> 16 & 0xFFFF;
  }
  
  private int extractMinorVersion(Font paramFont)
  {
    FontHeaderTable localFontHeaderTable = (FontHeaderTable)paramFont.getTable(Tag.head);
    return localFontHeaderTable.fontRevision() & 0xFFFF;
  }
  
  private int align4(int paramInt)
  {
    return paramInt + 3 & 0xFFFFFFFC;
  }
  
  private int computeTableDirectoryEntriesLength(List<TableDirectoryEntry> paramList)
  {
    return TableDirectoryEntry.ENTRY_SIZE * paramList.size();
  }
  
  private int computeTablesLength(List<TableDirectoryEntry> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TableDirectoryEntry localTableDirectoryEntry = (TableDirectoryEntry)localIterator.next();
      i += localTableDirectoryEntry.getCompressedTableLength();
      i = align4(i);
    }
    return i;
  }
  
  private int writeWoffHeader(WritableFontData paramWritableFontData, int paramInt1, List<TableDirectoryEntry> paramList, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = paramInt1;
    i += paramWritableFontData.writeULong(i, 2001684038L);
    i += paramWritableFontData.writeULong(i, paramInt2);
    i += paramWritableFontData.writeULong(i, paramInt3);
    i += paramWritableFontData.writeUShort(i, paramList.size());
    i += paramWritableFontData.writeUShort(i, 0);
    i += paramWritableFontData.writeULong(i, computeUncompressedTablesLength(paramList) + computeTableSfntHeaderLength(paramList));
    i += paramWritableFontData.writeUShort(i, 1);
    i += paramWritableFontData.writeUShort(i, 1);
    i += paramWritableFontData.writeULong(i, 0L);
    i += paramWritableFontData.writeULong(i, 0L);
    i += paramWritableFontData.writeULong(i, 0L);
    i += paramWritableFontData.writeULong(i, 0L);
    i += paramWritableFontData.writeULong(i, 0L);
    return WOFF_HEADER_SIZE;
  }
  
  private int computeTableSfntHeaderLength(List<TableDirectoryEntry> paramList)
  {
    return FontData.DataSize.ULONG.size() + 4 * FontData.DataSize.USHORT.size() + 4 * FontData.DataSize.ULONG.size() * paramList.size();
  }
  
  private int computeUncompressedTablesLength(List<TableDirectoryEntry> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TableDirectoryEntry localTableDirectoryEntry = (TableDirectoryEntry)localIterator.next();
      i = (int)(i + localTableDirectoryEntry.getUncompressedTableLength());
      i = align4(i);
    }
    return i;
  }
  
  private int writeTableDirectoryEntries(WritableFontData paramWritableFontData, int paramInt, List<TableDirectoryEntry> paramList)
  {
    int i = paramInt;
    int j = align4(paramInt + computeTableDirectoryEntriesLength(paramList));
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TableDirectoryEntry localTableDirectoryEntry = (TableDirectoryEntry)localIterator.next();
      i += localTableDirectoryEntry.writeEntry(paramWritableFontData, j, i);
      j += localTableDirectoryEntry.getCompressedTableLength();
      j = align4(j);
    }
    return computeTableDirectoryEntriesLength(paramList);
  }
  
  private int writeTables(WritableFontData paramWritableFontData, int paramInt, List<TableDirectoryEntry> paramList)
  {
    int i = align4(paramInt);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TableDirectoryEntry localTableDirectoryEntry = (TableDirectoryEntry)localIterator.next();
      i += localTableDirectoryEntry.writeTable(paramWritableFontData, i);
      i = align4(i);
    }
    return i - paramInt;
  }
  
  private List<TableDirectoryEntry> createTableDirectoryEntries(Font paramFont)
  {
    ArrayList localArrayList = new ArrayList();
    TreeSet localTreeSet = new TreeSet(paramFont.tableMap().keySet());
    localTreeSet.remove(Integer.valueOf(Tag.DSIG));
    Iterator localIterator = localTreeSet.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      Table localTable = paramFont.getTable(i);
      TableDirectoryEntry localTableDirectoryEntry = new TableDirectoryEntry(null);
      localTableDirectoryEntry.setTag(i);
      localTableDirectoryEntry.setOrigLength(localTable.dataLength());
      localTableDirectoryEntry.setOrigChecksum(localTable.calculatedChecksum());
      setCompressedTableData(localTableDirectoryEntry, localTable);
      localArrayList.add(localTableDirectoryEntry);
    }
    return localArrayList;
  }
  
  private void setCompressedTableData(TableDirectoryEntry paramTableDirectoryEntry, Table paramTable)
  {
    int i = paramTable.dataLength();
    byte[] arrayOfByte1 = new byte[i];
    paramTable.readFontData().readBytes(0, arrayOfByte1, 0, i);
    if ((this.woff_compression_faster) && ((i < 100) || (paramTable.headerTag() == Tag.loca)))
    {
      paramTableDirectoryEntry.setCompTable(arrayOfByte1);
    }
    else
    {
      byte[] arrayOfByte2 = new byte[i];
      Deflater localDeflater = new Deflater();
      localDeflater.setInput(arrayOfByte1);
      localDeflater.finish();
      int j = localDeflater.deflate(arrayOfByte2);
      paramTableDirectoryEntry.setCompTable((j == i) || (!localDeflater.finished()) ? arrayOfByte1 : Arrays.copyOfRange(arrayOfByte2, 0, j));
    }
  }
  
  private static class TableDirectoryEntry
  {
    public static final int ENTRY_SIZE = 5 * FontData.DataSize.ULONG.size();
    private long tag;
    private long origLength;
    private long origChecksum;
    private byte[] compTable;
    
    public void setTag(int paramInt)
    {
      this.tag = paramInt;
    }
    
    public void setOrigLength(int paramInt)
    {
      this.origLength = paramInt;
    }
    
    public void setOrigChecksum(long paramLong)
    {
      this.origChecksum = paramLong;
    }
    
    public void setCompTable(byte[] paramArrayOfByte)
    {
      this.compTable = paramArrayOfByte;
    }
    
    public int getCompressedTableLength()
    {
      return this.compTable.length;
    }
    
    public long getUncompressedTableLength()
    {
      return this.origLength;
    }
    
    public int writeEntry(WritableFontData paramWritableFontData, int paramInt1, int paramInt2)
    {
      int i = paramInt2;
      i += paramWritableFontData.writeULong(i, this.tag);
      i += paramWritableFontData.writeULong(i, paramInt1);
      i += paramWritableFontData.writeULong(i, this.compTable.length);
      i += paramWritableFontData.writeULong(i, this.origLength);
      i += paramWritableFontData.writeULong(i, this.origChecksum);
      return ENTRY_SIZE;
    }
    
    public int writeTable(WritableFontData paramWritableFontData, int paramInt)
    {
      paramWritableFontData.writeBytes(paramInt, this.compTable, 0, this.compTable.length);
      return getCompressedTableLength();
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\woff\WoffWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */