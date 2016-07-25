package com.google.typography.font.sfntly.table.truetype;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTableContainerTable;
import com.google.typography.font.sfntly.table.SubTableContainerTable.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GlyphTable
  extends SubTableContainerTable
{
  private GlyphTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public Glyph glyph(int paramInt1, int paramInt2)
  {
    return Glyph.getGlyph(this, this.data, paramInt1, paramInt2);
  }
  
  public static class Builder
    extends SubTableContainerTable.Builder<GlyphTable>
  {
    private List<Glyph.Builder<? extends Glyph>> glyphBuilders;
    private List<Integer> loca;
    
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
    }
    
    public void setLoca(List<Integer> paramList)
    {
      this.loca = new ArrayList(paramList);
      setModelChanged(false);
      this.glyphBuilders = null;
    }
    
    public List<Integer> generateLocaList()
    {
      ArrayList localArrayList = new ArrayList(getGlyphBuilders().size());
      localArrayList.add(Integer.valueOf(0));
      if (getGlyphBuilders().size() == 0)
      {
        localArrayList.add(Integer.valueOf(0));
      }
      else
      {
        int i = 0;
        Iterator localIterator = getGlyphBuilders().iterator();
        while (localIterator.hasNext())
        {
          Glyph.Builder localBuilder = (Glyph.Builder)localIterator.next();
          int j = localBuilder.subDataSizeToSerialize();
          localArrayList.add(Integer.valueOf(i + j));
          i += j;
        }
      }
      return localArrayList;
    }
    
    private void initialize(ReadableFontData paramReadableFontData, List<Integer> paramList)
    {
      this.glyphBuilders = new ArrayList();
      if (paramReadableFontData != null)
      {
        int j = ((Integer)paramList.get(0)).intValue();
        for (int k = 1; k < paramList.size(); k++)
        {
          int i = ((Integer)paramList.get(k)).intValue();
          this.glyphBuilders.add(Glyph.Builder.getBuilder(this, paramReadableFontData, j, i - j));
          j = i;
        }
      }
    }
    
    private List<Glyph.Builder<? extends Glyph>> getGlyphBuilders()
    {
      if (this.glyphBuilders == null)
      {
        if ((internalReadData() != null) && (this.loca == null)) {
          throw new IllegalStateException("Loca values not set - unable to parse glyph data.");
        }
        initialize(internalReadData(), this.loca);
        setModelChanged();
      }
      return this.glyphBuilders;
    }
    
    public void revert()
    {
      this.glyphBuilders = null;
      setModelChanged(false);
    }
    
    public List<Glyph.Builder<? extends Glyph>> glyphBuilders()
    {
      return getGlyphBuilders();
    }
    
    public void setGlyphBuilders(List<Glyph.Builder<? extends Glyph>> paramList)
    {
      this.glyphBuilders = paramList;
      setModelChanged();
    }
    
    public Glyph.Builder<? extends Glyph> glyphBuilder(ReadableFontData paramReadableFontData)
    {
      Glyph.Builder localBuilder = Glyph.Builder.getBuilder(this, paramReadableFontData);
      return localBuilder;
    }
    
    protected GlyphTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new GlyphTable(header(), paramReadableFontData, null);
    }
    
    protected void subDataSet()
    {
      this.glyphBuilders = null;
      super.setModelChanged(false);
    }
    
    protected int subDataSizeToSerialize()
    {
      if ((this.glyphBuilders == null) || (this.glyphBuilders.size() == 0)) {
        return 0;
      }
      int i = 0;
      int j = 0;
      Iterator localIterator = this.glyphBuilders.iterator();
      while (localIterator.hasNext())
      {
        Glyph.Builder localBuilder = (Glyph.Builder)localIterator.next();
        int k = localBuilder.subDataSizeToSerialize();
        j += Math.abs(k);
        i |= (k <= 0 ? 1 : 0);
      }
      return i != 0 ? -j : j;
    }
    
    protected boolean subReadyToSerialize()
    {
      return this.glyphBuilders != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = 0;
      Iterator localIterator = this.glyphBuilders.iterator();
      while (localIterator.hasNext())
      {
        Glyph.Builder localBuilder = (Glyph.Builder)localIterator.next();
        i += localBuilder.subSerialize(paramWritableFontData.slice(i));
      }
      return i;
    }
  }
  
  public static enum Offset
  {
    numberOfContours(0),  xMin(2),  yMin(4),  xMax(6),  yMax(8),  simpleEndPtsOfCountours(10),  simpleInstructionLength(0),  simpleInstructions(2),  compositeFlags(0),  compositeGyphIndexWithoutFlag(0),  compositeGlyphIndexWithFlag(2);
    
    final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\truetype\GlyphTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */