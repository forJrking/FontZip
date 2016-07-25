package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.core.FontHeaderTable;
import com.google.typography.font.sfntly.table.core.HorizontalDeviceMetricsTable;
import com.google.typography.font.sfntly.table.truetype.ControlValueTable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MtxWriter
{
  private static final Set<Integer> REMOVE_TABLES = ;
  
  private static Set<Integer> createRemoveTables()
  {
    HashSet localHashSet = new HashSet();
    localHashSet.add(Integer.valueOf(Tag.VDMX));
    localHashSet.add(Integer.valueOf(Tag.glyf));
    localHashSet.add(Integer.valueOf(Tag.cvt));
    localHashSet.add(Integer.valueOf(Tag.loca));
    localHashSet.add(Integer.valueOf(Tag.hdmx));
    localHashSet.add(Integer.valueOf(Tag.head));
    return Collections.unmodifiableSet(localHashSet);
  }
  
  public byte[] compress(Font paramFont)
  {
    MtxFontBuilder localMtxFontBuilder = new MtxFontBuilder();
    Object localObject1 = paramFont.tableMap().entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localObject3 = (Integer)((Map.Entry)localObject2).getKey();
      if (!REMOVE_TABLES.contains(localObject3)) {
        localMtxFontBuilder.addTable(((Integer)localObject3).intValue(), ((Table)((Map.Entry)localObject2).getValue()).readFontData());
      }
    }
    localObject1 = (FontHeaderTable)paramFont.getTable(Tag.head);
    localMtxFontBuilder.getHeadBuilder().initFrom((FontHeaderTable)localObject1);
    Object localObject2 = new GlyfEncoder();
    ((GlyfEncoder)localObject2).encode(paramFont);
    localMtxFontBuilder.addTableBytes(Tag.glyf, ((GlyfEncoder)localObject2).getGlyfBytes());
    localMtxFontBuilder.addTable(Tag.loca, null);
    Object localObject3 = (ControlValueTable)paramFont.getTable(Tag.cvt);
    if (localObject3 != null)
    {
      localObject4 = new CvtEncoder();
      ((CvtEncoder)localObject4).encode((ControlValueTable)localObject3);
      localMtxFontBuilder.addTableBytes(Tag.cvt, ((CvtEncoder)localObject4).toByteArray());
    }
    Object localObject4 = (HorizontalDeviceMetricsTable)paramFont.getTable(Tag.hdmx);
    if (localObject4 != null) {
      localMtxFontBuilder.addTable(Tag.hdmx, new HdmxEncoder().encode(paramFont));
    }
    byte[] arrayOfByte1 = localMtxFontBuilder.build();
    byte[] arrayOfByte2 = ((GlyfEncoder)localObject2).getPushBytes();
    byte[] arrayOfByte3 = ((GlyfEncoder)localObject2).getCodeBytes();
    return packMtx(arrayOfByte1, arrayOfByte2, arrayOfByte3);
  }
  
  private static void writeBE24(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >> 16 & 0xFF));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8 & 0xFF));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 & 0xFF));
  }
  
  private static byte[] packMtx(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    int i = Math.max(paramArrayOfByte1.length, Math.max(paramArrayOfByte2.length, paramArrayOfByte3.length)) + LzcompCompress.getPreloadSize();
    byte[] arrayOfByte1 = LzcompCompress.compress(paramArrayOfByte1);
    byte[] arrayOfByte2 = LzcompCompress.compress(paramArrayOfByte2);
    byte[] arrayOfByte3 = LzcompCompress.compress(paramArrayOfByte3);
    int j = 10 + arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length;
    byte[] arrayOfByte4 = new byte[j];
    arrayOfByte4[0] = 3;
    writeBE24(arrayOfByte4, i, 1);
    int k = 10 + arrayOfByte1.length;
    int m = k + arrayOfByte2.length;
    writeBE24(arrayOfByte4, k, 4);
    writeBE24(arrayOfByte4, m, 7);
    System.arraycopy(arrayOfByte1, 0, arrayOfByte4, 10, arrayOfByte1.length);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte4, k, arrayOfByte2.length);
    System.arraycopy(arrayOfByte3, 0, arrayOfByte4, m, arrayOfByte3.length);
    return arrayOfByte4;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\MtxWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */