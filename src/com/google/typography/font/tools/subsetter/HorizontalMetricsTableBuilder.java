package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.HorizontalHeaderTable.Builder;
import java.util.List;

public class HorizontalMetricsTableBuilder
{
  private final Font.Builder fontBuilder;
  private final List<LongHorMetric> metrics;
  
  public HorizontalMetricsTableBuilder(Font.Builder paramBuilder, List<LongHorMetric> paramList)
  {
    this.fontBuilder = paramBuilder;
    this.metrics = paramList;
  }
  
  public void build()
  {
    int i = this.metrics.size();
    if (i <= 0) {
      throw new IllegalArgumentException("nMetrics must be positive");
    }
    int j = ((LongHorMetric)this.metrics.get(i - 1)).advanceWidth;
    for (int k = i; (k > 1) && (((LongHorMetric)this.metrics.get(k - 2)).advanceWidth == j); k--) {}
    int m = 4 * k + 2 * (i - k);
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(m);
    int n = 0;
    int i1 = 0;
    for (int i2 = 0; i2 < k; i2++)
    {
      int i3 = ((LongHorMetric)this.metrics.get(i2)).advanceWidth;
      i1 = Math.max(i3, i1);
      n += localWritableFontData.writeUShort(n, i3);
      n += localWritableFontData.writeShort(n, ((LongHorMetric)this.metrics.get(i2)).lsb);
    }
    for (i2 = k; i2 < i; i2++) {
      n += localWritableFontData.writeShort(n, ((LongHorMetric)this.metrics.get(i2)).lsb);
    }
    this.fontBuilder.newTableBuilder(Tag.hmtx, localWritableFontData);
    HorizontalHeaderTable.Builder localBuilder = (HorizontalHeaderTable.Builder)this.fontBuilder.getTableBuilder(Tag.hhea);
    localBuilder.setNumberOfHMetrics(k);
    localBuilder.setAdvanceWidthMax(i1);
  }
  
  public static class LongHorMetric
  {
    public int advanceWidth;
    public int lsb;
    
    public LongHorMetric(int paramInt1, int paramInt2)
    {
      this.advanceWidth = paramInt1;
      this.lsb = paramInt2;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\HorizontalMetricsTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */