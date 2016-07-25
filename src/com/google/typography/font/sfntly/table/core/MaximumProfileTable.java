package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;

public final class MaximumProfileTable
  extends Table
{
  private MaximumProfileTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int tableVersion()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public int numGlyphs()
  {
    return this.data.readUShort(Offset.numGlyphs.offset);
  }
  
  public int maxPoints()
  {
    return this.data.readUShort(Offset.maxPoints.offset);
  }
  
  public int maxContours()
  {
    return this.data.readUShort(Offset.maxContours.offset);
  }
  
  public int maxCompositePoints()
  {
    return this.data.readUShort(Offset.maxCompositePoints.offset);
  }
  
  public int maxCompositeContours()
  {
    return this.data.readUShort(Offset.maxCompositeContours.offset);
  }
  
  public int maxZones()
  {
    return this.data.readUShort(Offset.maxZones.offset);
  }
  
  public int maxTwilightPoints()
  {
    return this.data.readUShort(Offset.maxTwilightPoints.offset);
  }
  
  public int maxStorage()
  {
    return this.data.readUShort(Offset.maxStorage.offset);
  }
  
  public int maxFunctionDefs()
  {
    return this.data.readUShort(Offset.maxFunctionDefs.offset);
  }
  
  public int maxStackElements()
  {
    return this.data.readUShort(Offset.maxStackElements.offset);
  }
  
  public int maxSizeOfInstructions()
  {
    return this.data.readUShort(Offset.maxSizeOfInstructions.offset);
  }
  
  public int maxComponentElements()
  {
    return this.data.readUShort(Offset.maxComponentElements.offset);
  }
  
  public int maxComponentDepth()
  {
    return this.data.readUShort(Offset.maxComponentDepth.offset);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<MaximumProfileTable>
  {
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
    
    protected MaximumProfileTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new MaximumProfileTable(header(), paramReadableFontData, null);
    }
    
    public int tableVersion()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.version));
    }
    
    public void setTableVersion(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.version), paramInt);
    }
    
    public int numGlyphs()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.numGlyphs));
    }
    
    public void setNumGlyphs(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.numGlyphs), paramInt);
    }
    
    public int maxPoints()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxPoints));
    }
    
    public void maxPoints(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxPoints), paramInt);
    }
    
    public int maxContours()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxContours));
    }
    
    public void setMaxContours(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxContours), paramInt);
    }
    
    public int maxCompositePoints()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxCompositePoints));
    }
    
    public void setMaxCompositePoints(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxCompositePoints), paramInt);
    }
    
    public int maxCompositeContours()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxCompositeContours));
    }
    
    public void setMaxCompositeContours(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxCompositeContours), paramInt);
    }
    
    public int maxZones()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxZones));
    }
    
    public void setMaxZones(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxZones), paramInt);
    }
    
    public int maxTwilightPoints()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxTwilightPoints));
    }
    
    public void setMaxTwilightPoints(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxTwilightPoints), paramInt);
    }
    
    public int maxStorage()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxStorage));
    }
    
    public void setMaxStorage(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxStorage), paramInt);
    }
    
    public int maxFunctionDefs()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxFunctionDefs));
    }
    
    public void setMaxFunctionDefs(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxFunctionDefs), paramInt);
    }
    
    public int maxStackElements()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxStackElements));
    }
    
    public void setMaxStackElements(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxStackElements), paramInt);
    }
    
    public int maxSizeOfInstructions()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxSizeOfInstructions));
    }
    
    public void setMaxSizeOfInstructions(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxSizeOfInstructions), paramInt);
    }
    
    public int maxComponentElements()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxComponentElements));
    }
    
    public void setMaxComponentElements(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxComponentElements), paramInt);
    }
    
    public int maxComponentDepth()
    {
      return internalReadData().readUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxComponentDepth));
    }
    
    public void setMaxComponentDepth(int paramInt)
    {
      internalWriteData().writeUShort(MaximumProfileTable.Offset.access$000(MaximumProfileTable.Offset.maxComponentDepth), paramInt);
    }
  }
  
  private static enum Offset
  {
    version(0),  numGlyphs(4),  maxPoints(6),  maxContours(8),  maxCompositePoints(10),  maxCompositeContours(12),  maxZones(14),  maxTwilightPoints(16),  maxStorage(18),  maxFunctionDefs(20),  maxInstructionDefs(22),  maxStackElements(24),  maxSizeOfInstructions(26),  maxComponentElements(28),  maxComponentDepth(30);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\MaximumProfileTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */