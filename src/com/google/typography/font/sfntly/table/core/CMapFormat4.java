package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.math.FontMath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class CMapFormat4
  extends CMap
{
  private final int segCount = this.data.readUShort(CMapTable.Offset.format4SegCountX2.offset) / 2;
  private final int glyphIdArrayOffset = glyphIdArrayOffset(this.segCount);
  
  protected CMapFormat4(ReadableFontData paramReadableFontData, CMapTable.CMapId paramCMapId)
  {
    super(paramReadableFontData, CMap.CMapFormat.Format4.value, paramCMapId);
  }
  
  public int glyphId(int paramInt)
  {
    int i = this.data.searchUShort(startCodeOffset(this.segCount), FontData.DataSize.USHORT.size(), CMapTable.Offset.format4EndCount.offset, FontData.DataSize.USHORT.size(), this.segCount, paramInt);
    if (i == -1) {
      return 0;
    }
    int j = startCode(i);
    return retrieveGlyphId(i, j, paramInt);
  }
  
  public int retrieveGlyphId(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 < paramInt2) {
      return 0;
    }
    int i = idRangeOffset(paramInt1);
    if (i == 0) {
      return (paramInt3 + idDelta(paramInt1)) % 65536;
    }
    int j = this.data.readUShort(i + idRangeOffsetLocation(paramInt1) + 2 * (paramInt3 - paramInt2));
    if (j != 0) {
      j = (j + idDelta(paramInt1)) % 65536;
    }
    return j;
  }
  
  public int getSegCount()
  {
    return this.segCount;
  }
  
  public int startCode(int paramInt)
  {
    isValidIndex(paramInt);
    return startCode(this.data, this.segCount, paramInt);
  }
  
  private static int length(ReadableFontData paramReadableFontData)
  {
    int i = paramReadableFontData.readUShort(CMapTable.Offset.format4Length.offset);
    return i;
  }
  
  private static int segCount(ReadableFontData paramReadableFontData)
  {
    int i = paramReadableFontData.readUShort(CMapTable.Offset.format4SegCountX2.offset) / 2;
    return i;
  }
  
  private static int startCode(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    int i = paramReadableFontData.readUShort(startCodeOffset(paramInt1) + paramInt2 * FontData.DataSize.USHORT.size());
    return i;
  }
  
  private static int startCodeOffset(int paramInt)
  {
    int i = CMapTable.Offset.format4EndCount.offset + FontData.DataSize.USHORT.size() + paramInt * FontData.DataSize.USHORT.size();
    return i;
  }
  
  private static int endCode(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    int i = paramReadableFontData.readUShort(CMapTable.Offset.format4EndCount.offset + paramInt2 * FontData.DataSize.USHORT.size());
    return i;
  }
  
  private static int idDelta(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    int i = paramReadableFontData.readShort(idDeltaOffset(paramInt1) + paramInt2 * FontData.DataSize.SHORT.size());
    return i;
  }
  
  private static int idDeltaOffset(int paramInt)
  {
    int i = CMapTable.Offset.format4EndCount.offset + (2 * paramInt + 1) * FontData.DataSize.USHORT.size();
    return i;
  }
  
  private static int idRangeOffset(ReadableFontData paramReadableFontData, int paramInt1, int paramInt2)
  {
    int i = paramReadableFontData.readUShort(idRangeOffsetOffset(paramInt1) + paramInt2 * FontData.DataSize.USHORT.size());
    return i;
  }
  
  private static int idRangeOffsetOffset(int paramInt)
  {
    int i = CMapTable.Offset.format4EndCount.offset + (2 * paramInt + 1) * FontData.DataSize.USHORT.size() + paramInt * FontData.DataSize.SHORT.size();
    return i;
  }
  
  private static int glyphIdArrayOffset(int paramInt)
  {
    int i = CMapTable.Offset.format4EndCount.offset + (3 * paramInt + 1) * FontData.DataSize.USHORT.size() + paramInt * FontData.DataSize.SHORT.size();
    return i;
  }
  
  public int endCode(int paramInt)
  {
    isValidIndex(paramInt);
    return endCode(this.data, this.segCount, paramInt);
  }
  
  private void isValidIndex(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.segCount)) {
      throw new IllegalArgumentException();
    }
  }
  
  public int idDelta(int paramInt)
  {
    isValidIndex(paramInt);
    return idDelta(this.data, this.segCount, paramInt);
  }
  
  public int idRangeOffset(int paramInt)
  {
    isValidIndex(paramInt);
    return this.data.readUShort(idRangeOffsetLocation(paramInt));
  }
  
  public int idRangeOffsetLocation(int paramInt)
  {
    isValidIndex(paramInt);
    return idRangeOffsetOffset(this.segCount) + paramInt * FontData.DataSize.USHORT.size();
  }
  
  private int glyphIdArray(int paramInt)
  {
    return this.data.readUShort(this.glyphIdArrayOffset + paramInt * FontData.DataSize.USHORT.size());
  }
  
  public int language()
  {
    return this.data.readUShort(CMapTable.Offset.format4Language.offset);
  }
  
  public Iterator<Integer> iterator()
  {
    return new CharacterIterator(null);
  }
  
  public static class Builder
    extends CMap.Builder<CMapFormat4>
  {
    private List<Segment> segments;
    private List<Integer> glyphIdArray;
    
    protected Builder(WritableFontData paramWritableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format4, paramCMapId);
    }
    
    protected Builder(ReadableFontData paramReadableFontData, int paramInt, CMapTable.CMapId paramCMapId)
    {
      super(CMap.CMapFormat.Format4, paramCMapId);
    }
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      this.segments = new ArrayList();
      this.glyphIdArray = new ArrayList();
      if ((paramReadableFontData == null) || (paramReadableFontData.length() == 0)) {
        return;
      }
      int i = CMapFormat4.segCount(paramReadableFontData);
      for (int j = 0; j < i; j++)
      {
        Segment localSegment = new Segment();
        localSegment.setStartCount(CMapFormat4.startCode(paramReadableFontData, i, j));
        localSegment.setEndCount(CMapFormat4.endCode(paramReadableFontData, i, j));
        localSegment.setIdDelta(CMapFormat4.idDelta(paramReadableFontData, i, j));
        localSegment.setIdRangeOffset(CMapFormat4.idRangeOffset(paramReadableFontData, i, j));
        this.segments.add(localSegment);
      }
      j = CMapFormat4.length(paramReadableFontData) - CMapFormat4.glyphIdArrayOffset(i);
      int k = 0;
      while (k < j)
      {
        this.glyphIdArray.add(Integer.valueOf(paramReadableFontData.readUShort(k + CMapFormat4.glyphIdArrayOffset(i))));
        k += FontData.DataSize.USHORT.size();
      }
    }
    
    public List<Segment> getSegments()
    {
      if (this.segments == null)
      {
        initialize(internalReadData());
        setModelChanged();
      }
      return this.segments;
    }
    
    public void setSegments(List<Segment> paramList)
    {
      this.segments = Segment.deepCopy(paramList);
      setModelChanged();
    }
    
    public List<Integer> getGlyphIdArray()
    {
      if (this.glyphIdArray == null)
      {
        initialize(internalReadData());
        setModelChanged();
      }
      return this.glyphIdArray;
    }
    
    public void setGlyphIdArray(List<Integer> paramList)
    {
      this.glyphIdArray = new ArrayList(paramList);
      setModelChanged();
    }
    
    protected CMapFormat4 subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new CMapFormat4(paramReadableFontData, cmapId());
    }
    
    protected void subDataSet()
    {
      this.segments = null;
      this.glyphIdArray = null;
      super.setModelChanged(false);
    }
    
    protected int subDataSizeToSerialize()
    {
      if (!modelChanged()) {
        return super.subDataSizeToSerialize();
      }
      int i = CMapTable.Offset.format4FixedSize.offset + this.segments.size() * (3 * FontData.DataSize.USHORT.size() + FontData.DataSize.SHORT.size()) + this.glyphIdArray.size() * FontData.DataSize.USHORT.size();
      return i;
    }
    
    protected boolean subReadyToSerialize()
    {
      if (!modelChanged()) {
        return super.subReadyToSerialize();
      }
      return this.segments != null;
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      if (!modelChanged()) {
        return super.subSerialize(paramWritableFontData);
      }
      int i = 0;
      i += paramWritableFontData.writeUShort(i, CMap.CMapFormat.Format4.value());
      i += FontData.DataSize.USHORT.size();
      i += paramWritableFontData.writeUShort(i, language());
      int j = this.segments.size();
      i += paramWritableFontData.writeUShort(i, j * 2);
      int k = FontMath.log2(this.segments.size());
      int m = 1 << k + 1;
      i += paramWritableFontData.writeUShort(i, m);
      int n = k;
      i += paramWritableFontData.writeUShort(i, n);
      int i1 = 2 * j - m;
      i += paramWritableFontData.writeUShort(i, i1);
      for (int i2 = 0; i2 < j; i2++) {
        i += paramWritableFontData.writeUShort(i, ((Segment)this.segments.get(i2)).getEndCount());
      }
      i += FontData.DataSize.USHORT.size();
      for (i2 = 0; i2 < j; i2++) {
        i += paramWritableFontData.writeUShort(i, ((Segment)this.segments.get(i2)).getStartCount());
      }
      for (i2 = 0; i2 < j; i2++) {
        i += paramWritableFontData.writeShort(i, ((Segment)this.segments.get(i2)).getIdDelta());
      }
      for (i2 = 0; i2 < j; i2++) {
        i += paramWritableFontData.writeUShort(i, ((Segment)this.segments.get(i2)).getIdRangeOffset());
      }
      for (i2 = 0; i2 < this.glyphIdArray.size(); i2++) {
        i += paramWritableFontData.writeUShort(i, ((Integer)this.glyphIdArray.get(i2)).intValue());
      }
      paramWritableFontData.writeUShort(CMapTable.Offset.format4Length.offset, i);
      return i;
    }
    
    public static class Segment
    {
      private int startCount;
      private int endCount;
      private int idDelta;
      private int idRangeOffset;
      
      public static List<Segment> deepCopy(List<Segment> paramList)
      {
        ArrayList localArrayList = new ArrayList(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          Segment localSegment = (Segment)localIterator.next();
          localArrayList.add(new Segment(localSegment));
        }
        return localArrayList;
      }
      
      public Segment() {}
      
      public Segment(Segment paramSegment)
      {
        this(paramSegment.startCount, paramSegment.endCount, paramSegment.idDelta, paramSegment.idRangeOffset);
      }
      
      public Segment(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        this.startCount = paramInt1;
        this.endCount = paramInt2;
        this.idDelta = paramInt3;
        this.idRangeOffset = paramInt4;
      }
      
      public int getStartCount()
      {
        return this.startCount;
      }
      
      public void setStartCount(int paramInt)
      {
        this.startCount = paramInt;
      }
      
      public int getEndCount()
      {
        return this.endCount;
      }
      
      public void setEndCount(int paramInt)
      {
        this.endCount = paramInt;
      }
      
      public int getIdDelta()
      {
        return this.idDelta;
      }
      
      public void setIdDelta(int paramInt)
      {
        this.idDelta = paramInt;
      }
      
      public int getIdRangeOffset()
      {
        return this.idRangeOffset;
      }
      
      public void setIdRangeOffset(int paramInt)
      {
        this.idRangeOffset = paramInt;
      }
      
      public String toString()
      {
        return String.format("[0x%04x - 0x%04x, delta = 0x%04x, rangeOffset = 0x%04x]", new Object[] { Integer.valueOf(this.startCount), Integer.valueOf(this.endCount), Integer.valueOf(this.idDelta), Integer.valueOf(this.idRangeOffset) });
      }
    }
  }
  
  private class CharacterIterator
    implements Iterator<Integer>
  {
    private int segmentIndex = 0;
    private int firstCharInSegment = -1;
    private int lastCharInSegment;
    private int nextChar;
    private boolean nextCharSet;
    
    private CharacterIterator() {}
    
    public boolean hasNext()
    {
      if (this.nextCharSet == true) {
        return true;
      }
      while (this.segmentIndex < CMapFormat4.this.segCount)
      {
        if (this.firstCharInSegment < 0)
        {
          this.firstCharInSegment = CMapFormat4.this.startCode(this.segmentIndex);
          this.lastCharInSegment = CMapFormat4.this.endCode(this.segmentIndex);
          this.nextChar = this.firstCharInSegment;
          this.nextCharSet = true;
          return true;
        }
        if (this.nextChar < this.lastCharInSegment)
        {
          this.nextChar += 1;
          this.nextCharSet = true;
          return true;
        }
        this.segmentIndex += 1;
        this.firstCharInSegment = -1;
      }
      return false;
    }
    
    public Integer next()
    {
      if ((!this.nextCharSet) && (!hasNext())) {
        throw new NoSuchElementException("No more characters to iterate.");
      }
      this.nextCharSet = false;
      return Integer.valueOf(this.nextChar);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Unable to remove a character from cmap.");
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\CMapFormat4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */