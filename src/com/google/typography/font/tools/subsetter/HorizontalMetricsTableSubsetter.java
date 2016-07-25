package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.HorizontalMetricsTable;
import java.util.ArrayList;
import java.util.List;

public class HorizontalMetricsTableSubsetter
  extends TableSubsetterImpl
{
  protected HorizontalMetricsTableSubsetter()
  {
    super(new Integer[] { Integer.valueOf(Tag.hmtx), Integer.valueOf(Tag.hhea) });
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
  {
    List localList = paramSubsetter.glyphMappingTable();
    if (localList == null) {
      return false;
    }
    HorizontalMetricsTable localHorizontalMetricsTable = (HorizontalMetricsTable)paramFont.getTable(Tag.hmtx);
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < localList.size(); i++)
    {
      int j = ((Integer)localList.get(i)).intValue();
      int k = localHorizontalMetricsTable.advanceWidth(j);
      int m = localHorizontalMetricsTable.leftSideBearing(j);
      localArrayList.add(new HorizontalMetricsTableBuilder.LongHorMetric(k, m));
    }
    new HorizontalMetricsTableBuilder(paramBuilder, localArrayList).build();
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\HorizontalMetricsTableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */