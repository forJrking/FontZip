package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.table.core.CMap.CMapFormat;
import com.google.typography.font.sfntly.table.core.CMapFormat4.Builder;
import com.google.typography.font.sfntly.table.core.CMapFormat4.Builder.Segment;
import com.google.typography.font.sfntly.table.core.CMapTable.Builder;
import com.google.typography.font.sfntly.table.core.CMapTable.CMapId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class CMapTableBuilder
{
  private static final int MAX_FORMAT4_ENDCODE = 65535;
  private final Font.Builder fontBuilder;
  private final Map<Integer, Integer> mapping;
  
  public CMapTableBuilder(Font.Builder paramBuilder, Map<Integer, Integer> paramMap)
  {
    this.fontBuilder = paramBuilder;
    this.mapping = paramMap;
  }
  
  private List<CMap4Segment> getFormat4Segments()
  {
    ArrayList localArrayList = new ArrayList();
    TreeMap localTreeMap = new TreeMap(this.mapping);
    if (!localTreeMap.containsKey(Integer.valueOf(65535))) {
      localTreeMap.put(Integer.valueOf(65535), Integer.valueOf(0));
    }
    CMap4Segment localCMap4Segment = null;
    Iterator localIterator = localTreeMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      int i = ((Integer)localEntry.getKey()).intValue();
      if (i > 65535) {
        break;
      }
      int j = ((Integer)localEntry.getValue()).intValue();
      if ((localCMap4Segment == null) || (i != localCMap4Segment.getEndCode() + 1))
      {
        localCMap4Segment = new CMap4Segment(i, i);
        localArrayList.add(localCMap4Segment);
      }
      else
      {
        localCMap4Segment.setEndCode(i);
      }
      localCMap4Segment.getGlyphIds().add(Integer.valueOf(j));
    }
    return localArrayList;
  }
  
  private void buildCMapFormat4(CMapFormat4.Builder paramBuilder, List<CMap4Segment> paramList)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int i = paramList.size();
    for (int j = 0; j < paramList.size(); j++)
    {
      CMap4Segment localCMap4Segment = (CMap4Segment)paramList.get(j);
      int k;
      if (localCMap4Segment.isContiguous())
      {
        k = 0;
      }
      else
      {
        k = (i - j) * FontData.DataSize.USHORT.size();
        localArrayList2.addAll(localCMap4Segment.getGlyphIds());
        i += localCMap4Segment.getGlyphIds().size();
      }
      localArrayList1.add(new CMapFormat4.Builder.Segment(localCMap4Segment.getStartCode(), localCMap4Segment.getEndCode(), localCMap4Segment.idDelta(), k));
    }
    paramBuilder.setGlyphIdArray(localArrayList2);
    paramBuilder.setSegments(localArrayList1);
  }
  
  public void build()
  {
    CMapTable.Builder localBuilder = (CMapTable.Builder)this.fontBuilder.newTableBuilder(Tag.cmap);
    CMapFormat4.Builder localBuilder1 = (CMapFormat4.Builder)localBuilder.newCMapBuilder(CMapTable.CMapId.WINDOWS_BMP, CMap.CMapFormat.Format4);
    buildCMapFormat4(localBuilder1, getFormat4Segments());
  }
  
  private class CMap4Segment
  {
    private final int startCode;
    private int endCode;
    List<Integer> glyphIds;
    
    CMap4Segment(int paramInt1, int paramInt2)
    {
      this.startCode = paramInt1;
      this.endCode = paramInt2;
      this.glyphIds = new ArrayList();
    }
    
    private boolean isContiguous()
    {
      int i = ((Integer)this.glyphIds.get(0)).intValue();
      for (int j = 1; j < this.glyphIds.size(); j++) {
        if (((Integer)this.glyphIds.get(j)).intValue() != i + j) {
          return false;
        }
      }
      return true;
    }
    
    int idDelta()
    {
      return isContiguous() ? ((Integer)getGlyphIds().get(0)).intValue() - getStartCode() : 0;
    }
    
    public int getStartCode()
    {
      return this.startCode;
    }
    
    public void setEndCode(int paramInt)
    {
      this.endCode = paramInt;
    }
    
    public int getEndCode()
    {
      return this.endCode;
    }
    
    public List<Integer> getGlyphIds()
    {
      return this.glyphIds;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\CMapTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */