package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.ReadableFontData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class MtxFontBuilder
{
  private static final int OPENTYPE_VERSION_1_0 = 65536;
  private static final int FONT_HEADER_BASE_SIZE = 12;
  private static final int FONT_HEADER_PER_TABLE_SIZE = 16;
  private static final int TABLE_ALIGN = 4;
  private Map<Integer, ReadableFontData> tables = new HashMap();
  private MtxHeadBuilder headBuilder = new MtxHeadBuilder();
  
  public MtxHeadBuilder getHeadBuilder()
  {
    return this.headBuilder;
  }
  
  public void addTable(int paramInt, ReadableFontData paramReadableFontData)
  {
    this.tables.put(Integer.valueOf(paramInt), paramReadableFontData);
  }
  
  public void addTableBytes(int paramInt, byte[] paramArrayOfByte)
  {
    addTable(paramInt, ReadableFontData.createReadableFontData(paramArrayOfByte));
  }
  
  private static void putUshort(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 8));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)paramInt2);
  }
  
  private static void putUlong(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 24));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >> 16));
    paramArrayOfByte[(paramInt1 + 2)] = ((byte)(paramInt2 >> 8));
    paramArrayOfByte[(paramInt1 + 3)] = ((byte)paramInt2);
  }
  
  public byte[] build()
  {
    addTable(Tag.head, this.headBuilder.build());
    TreeSet localTreeSet = new TreeSet(this.tables.keySet());
    int i = this.tables.size();
    int j = 12 + 16 * i;
    Object localObject = this.tables.entrySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
      ReadableFontData localReadableFontData1 = (ReadableFontData)localEntry.getValue();
      if (localReadableFontData1 != null) {
        j += (((ReadableFontData)localEntry.getValue()).length() + 4 - 1 & 0xFFFFFFFC);
      }
    }
    localObject = new byte[j];
    putUlong((byte[])localObject, 0, 65536);
    putUshort((byte[])localObject, 4, i);
    int k = 0;
    int m = searchRange(i);
    putUshort((byte[])localObject, 6, m * 16);
    putUshort((byte[])localObject, 8, log2(m));
    putUshort((byte[])localObject, 10, (i - m) * 16);
    int n = 12;
    int i1 = 12 + 16 * i;
    Iterator localIterator = localTreeSet.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      ReadableFontData localReadableFontData2 = (ReadableFontData)this.tables.get(localInteger);
      putUlong((byte[])localObject, n, localInteger.intValue());
      int i2 = 0;
      putUlong((byte[])localObject, n + 4, i2);
      if (localReadableFontData2 == null)
      {
        putUlong((byte[])localObject, n + 8, 0);
        putUlong((byte[])localObject, n + 12, 0);
      }
      else
      {
        putUlong((byte[])localObject, n + 8, i1);
        int i3 = localReadableFontData2.length();
        putUlong((byte[])localObject, n + 12, i3);
        localReadableFontData2.readBytes(0, (byte[])localObject, i1, i3);
        i1 += (i3 + 4 - 1 & 0xFFFFFFFC);
      }
      n += 16;
    }
    return (byte[])localObject;
  }
  
  static int searchRange(int paramInt)
  {
    return Integer.highestOneBit(paramInt);
  }
  
  static int log2(int paramInt)
  {
    return 31 - Integer.numberOfLeadingZeros(paramInt);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\MtxFontBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */