package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;
import java.util.EnumSet;
import java.util.Iterator;

public final class FontHeaderTable
  extends Table
{
  public static final long CHECKSUM_ADJUSTMENT_BASE = 2981146554L;
  public static final long MAGIC_NUMBER = 1594834165L;
  private static final int[] CHECKSUM_RANGES = { 0, Offset.checkSumAdjustment.offset, Offset.magicNumber.offset };
  
  private FontHeaderTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
    paramReadableFontData.setCheckSumRanges(new int[] { 0, Offset.checkSumAdjustment.offset, Offset.magicNumber.offset });
  }
  
  public int tableVersion()
  {
    return this.data.readFixed(Offset.tableVersion.offset);
  }
  
  public int fontRevision()
  {
    return this.data.readFixed(Offset.fontRevision.offset);
  }
  
  public long checkSumAdjustment()
  {
    return this.data.readULong(Offset.checkSumAdjustment.offset);
  }
  
  public long magicNumber()
  {
    return this.data.readULong(Offset.magicNumber.offset);
  }
  
  public int flagsAsInt()
  {
    return this.data.readUShort(Offset.flags.offset);
  }
  
  public EnumSet<Flags> flags()
  {
    return Flags.asSet(flagsAsInt());
  }
  
  public int unitsPerEm()
  {
    return this.data.readUShort(Offset.unitsPerEm.offset);
  }
  
  public long created()
  {
    return this.data.readDateTimeAsLong(Offset.created.offset);
  }
  
  public long modified()
  {
    return this.data.readDateTimeAsLong(Offset.modified.offset);
  }
  
  public int xMin()
  {
    return this.data.readShort(Offset.xMin.offset);
  }
  
  public int yMin()
  {
    return this.data.readShort(Offset.yMin.offset);
  }
  
  public int xMax()
  {
    return this.data.readShort(Offset.xMax.offset);
  }
  
  public int yMax()
  {
    return this.data.readShort(Offset.yMax.offset);
  }
  
  public int macStyleAsInt()
  {
    return this.data.readUShort(Offset.macStyle.offset);
  }
  
  public EnumSet<MacStyle> macStyle()
  {
    return MacStyle.asSet(macStyleAsInt());
  }
  
  public int lowestRecPPEM()
  {
    return this.data.readUShort(Offset.lowestRecPPEM.offset);
  }
  
  public int fontDirectionHintAsInt()
  {
    return this.data.readShort(Offset.fontDirectionHint.offset);
  }
  
  public FontDirectionHint fontDirectionHint()
  {
    return FontDirectionHint.valueOf(fontDirectionHintAsInt());
  }
  
  public int indexToLocFormatAsInt()
  {
    return this.data.readShort(Offset.indexToLocFormat.offset);
  }
  
  public IndexToLocFormat indexToLocFormat()
  {
    return IndexToLocFormat.valueOf(indexToLocFormatAsInt());
  }
  
  public int glyphdataFormat()
  {
    return this.data.readShort(Offset.glyphDataFormat.offset);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<FontHeaderTable>
  {
    private boolean fontChecksumSet = false;
    private long fontChecksum = 0L;
    
    public static Builder createBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      return new Builder(paramHeader, paramWritableFontData);
    }
    
    protected Builder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      super(paramWritableFontData);
      paramWritableFontData.setCheckSumRanges(new int[] { 0, FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.checkSumAdjustment), FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.magicNumber) });
    }
    
    protected Builder(Header paramHeader, ReadableFontData paramReadableFontData)
    {
      super(paramReadableFontData);
      paramReadableFontData.setCheckSumRanges(FontHeaderTable.CHECKSUM_RANGES);
    }
    
    protected boolean subReadyToSerialize()
    {
      ReadableFontData localReadableFontData;
      if (dataChanged())
      {
        localReadableFontData = internalReadData();
        localReadableFontData.setCheckSumRanges(FontHeaderTable.CHECKSUM_RANGES);
      }
      if (this.fontChecksumSet)
      {
        localReadableFontData = internalReadData();
        localReadableFontData.setCheckSumRanges(FontHeaderTable.CHECKSUM_RANGES);
        long l = 2981146554L - (this.fontChecksum + localReadableFontData.checksum());
        setCheckSumAdjustment(l);
      }
      return super.subReadyToSerialize();
    }
    
    protected FontHeaderTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new FontHeaderTable(header(), paramReadableFontData, null);
    }
    
    public void setFontChecksum(long paramLong)
    {
      if (this.fontChecksumSet) {
        return;
      }
      this.fontChecksumSet = true;
      this.fontChecksum = paramLong;
    }
    
    public void clearFontChecksum()
    {
      this.fontChecksumSet = false;
    }
    
    public int tableVersion()
    {
      return ((FontHeaderTable)table()).tableVersion();
    }
    
    public void setTableVersion(int paramInt)
    {
      internalWriteData().writeFixed(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.tableVersion), paramInt);
    }
    
    public int fontRevision()
    {
      return ((FontHeaderTable)table()).fontRevision();
    }
    
    public void setFontRevision(int paramInt)
    {
      internalWriteData().writeFixed(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.fontRevision), paramInt);
    }
    
    public long checkSumAdjustment()
    {
      return ((FontHeaderTable)table()).checkSumAdjustment();
    }
    
    public void setCheckSumAdjustment(long paramLong)
    {
      internalWriteData().writeULong(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.checkSumAdjustment), paramLong);
    }
    
    public long magicNumber()
    {
      return ((FontHeaderTable)table()).magicNumber();
    }
    
    public void setMagicNumber(long paramLong)
    {
      internalWriteData().writeULong(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.magicNumber), paramLong);
    }
    
    public int flagsAsInt()
    {
      return ((FontHeaderTable)table()).flagsAsInt();
    }
    
    public EnumSet<FontHeaderTable.Flags> flags()
    {
      return ((FontHeaderTable)table()).flags();
    }
    
    public void setFlagsAsInt(int paramInt)
    {
      internalWriteData().writeUShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.flags), paramInt);
    }
    
    public void setFlags(EnumSet<FontHeaderTable.Flags> paramEnumSet)
    {
      setFlagsAsInt(FontHeaderTable.Flags.cleanValue(paramEnumSet));
    }
    
    public int unitsPerEm()
    {
      return ((FontHeaderTable)table()).unitsPerEm();
    }
    
    public void setUnitsPerEm(int paramInt)
    {
      internalWriteData().writeUShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.unitsPerEm), paramInt);
    }
    
    public long created()
    {
      return ((FontHeaderTable)table()).created();
    }
    
    public void setCreated(long paramLong)
    {
      internalWriteData().writeDateTime(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.created), paramLong);
    }
    
    public long modified()
    {
      return ((FontHeaderTable)table()).modified();
    }
    
    public void setModified(long paramLong)
    {
      internalWriteData().writeDateTime(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.modified), paramLong);
    }
    
    public int xMin()
    {
      return ((FontHeaderTable)table()).xMin();
    }
    
    public void setXMin(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.xMin), paramInt);
    }
    
    public int yMin()
    {
      return ((FontHeaderTable)table()).yMin();
    }
    
    public void setYMin(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.yMin), paramInt);
    }
    
    public int xMax()
    {
      return ((FontHeaderTable)table()).xMax();
    }
    
    public void setXMax(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.xMax), paramInt);
    }
    
    public int yMax()
    {
      return ((FontHeaderTable)table()).yMax();
    }
    
    public void setYMax(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.yMax), paramInt);
    }
    
    public int macStyleAsInt()
    {
      return ((FontHeaderTable)table()).macStyleAsInt();
    }
    
    public void setMacStyleAsInt(int paramInt)
    {
      internalWriteData().writeUShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.macStyle), paramInt);
    }
    
    public EnumSet<FontHeaderTable.MacStyle> macStyle()
    {
      return ((FontHeaderTable)table()).macStyle();
    }
    
    public void macStyle(EnumSet<FontHeaderTable.MacStyle> paramEnumSet)
    {
      setMacStyleAsInt(FontHeaderTable.MacStyle.cleanValue(paramEnumSet));
    }
    
    public int lowestRecPPEM()
    {
      return ((FontHeaderTable)table()).lowestRecPPEM();
    }
    
    public void setLowestRecPPEM(int paramInt)
    {
      internalWriteData().writeUShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.lowestRecPPEM), paramInt);
    }
    
    public int fontDirectionHintAsInt()
    {
      return ((FontHeaderTable)table()).fontDirectionHintAsInt();
    }
    
    public void setFontDirectionHintAsInt(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.fontDirectionHint), paramInt);
    }
    
    public FontHeaderTable.FontDirectionHint fontDirectionHint()
    {
      return ((FontHeaderTable)table()).fontDirectionHint();
    }
    
    public void setFontDirectionHint(FontHeaderTable.FontDirectionHint paramFontDirectionHint)
    {
      setFontDirectionHintAsInt(paramFontDirectionHint.value());
    }
    
    public int indexToLocFormatAsInt()
    {
      return ((FontHeaderTable)table()).indexToLocFormatAsInt();
    }
    
    public void setIndexToLocFormatAsInt(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.indexToLocFormat), paramInt);
    }
    
    public FontHeaderTable.IndexToLocFormat indexToLocFormat()
    {
      return ((FontHeaderTable)table()).indexToLocFormat();
    }
    
    public void setIndexToLocFormat(FontHeaderTable.IndexToLocFormat paramIndexToLocFormat)
    {
      setIndexToLocFormatAsInt(paramIndexToLocFormat.value());
    }
    
    public int glyphdataFormat()
    {
      return ((FontHeaderTable)table()).glyphdataFormat();
    }
    
    public void setGlyphdataFormat(int paramInt)
    {
      internalWriteData().writeShort(FontHeaderTable.Offset.access$000(FontHeaderTable.Offset.glyphDataFormat), paramInt);
    }
  }
  
  public static enum IndexToLocFormat
  {
    shortOffset(0),  longOffset(1);
    
    private final int value;
    
    private IndexToLocFormat(int paramInt)
    {
      this.value = paramInt;
    }
    
    public int value()
    {
      return this.value;
    }
    
    public boolean equals(int paramInt)
    {
      return paramInt == this.value;
    }
    
    public static IndexToLocFormat valueOf(int paramInt)
    {
      for (IndexToLocFormat localIndexToLocFormat : ) {
        if (localIndexToLocFormat.equals(paramInt)) {
          return localIndexToLocFormat;
        }
      }
      return null;
    }
  }
  
  public static enum FontDirectionHint
  {
    FullyMixed(0),  OnlyStrongLTR(1),  StrongLTRAndNeutral(2),  OnlyStrongRTL(-1),  StrongRTLAndNeutral(-2);
    
    private final int value;
    
    private FontDirectionHint(int paramInt)
    {
      this.value = paramInt;
    }
    
    public int value()
    {
      return this.value;
    }
    
    public boolean equals(int paramInt)
    {
      return paramInt == this.value;
    }
    
    public static FontDirectionHint valueOf(int paramInt)
    {
      for (FontDirectionHint localFontDirectionHint : ) {
        if (localFontDirectionHint.equals(paramInt)) {
          return localFontDirectionHint;
        }
      }
      return null;
    }
  }
  
  public static enum MacStyle
  {
    Bold,  Italic,  Underline,  Outline,  Shadow,  Condensed,  Extended,  Reserved7,  Reserved8,  Reserved9,  Reserved10,  Reserved11,  Reserved12,  Reserved13,  Reserved14,  Reserved15;
    
    private static final EnumSet<MacStyle> reserved = EnumSet.range(Reserved7, Reserved15);
    
    private MacStyle() {}
    
    public int mask()
    {
      return 1 << ordinal();
    }
    
    public static EnumSet<MacStyle> asSet(int paramInt)
    {
      EnumSet localEnumSet = EnumSet.noneOf(MacStyle.class);
      for (MacStyle localMacStyle : values()) {
        if ((paramInt & localMacStyle.mask()) == localMacStyle.mask()) {
          localEnumSet.add(localMacStyle);
        }
      }
      return localEnumSet;
    }
    
    public static int value(EnumSet<MacStyle> paramEnumSet)
    {
      int i = 0;
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        MacStyle localMacStyle = (MacStyle)localIterator.next();
        i |= localMacStyle.mask();
      }
      return i;
    }
    
    public static int cleanValue(EnumSet<MacStyle> paramEnumSet)
    {
      EnumSet localEnumSet = EnumSet.copyOf(paramEnumSet);
      localEnumSet.removeAll(reserved);
      return value(localEnumSet);
    }
  }
  
  public static enum Flags
  {
    BaselineAtY0,  LeftSidebearingAtX0,  InstructionsDependOnPointSize,  ForcePPEMToInteger,  InstructionsAlterAdvanceWidth,  Apple_Vertical,  Apple_Zero,  Apple_RequiresLayout,  Apple_GXMetamorphosis,  Apple_StrongRTL,  Apple_IndicRearrangement,  FontDataLossless,  FontConverted,  OptimizedForClearType,  Reserved14,  Reserved15;
    
    private Flags() {}
    
    public int mask()
    {
      return 1 << ordinal();
    }
    
    public static EnumSet<Flags> asSet(int paramInt)
    {
      EnumSet localEnumSet = EnumSet.noneOf(Flags.class);
      for (Flags localFlags : values()) {
        if ((paramInt & localFlags.mask()) == localFlags.mask()) {
          localEnumSet.add(localFlags);
        }
      }
      return localEnumSet;
    }
    
    public static int value(EnumSet<Flags> paramEnumSet)
    {
      int i = 0;
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        Flags localFlags = (Flags)localIterator.next();
        i |= localFlags.mask();
      }
      return i;
    }
    
    public static int cleanValue(EnumSet<Flags> paramEnumSet)
    {
      EnumSet localEnumSet = EnumSet.copyOf(paramEnumSet);
      localEnumSet.remove(Reserved14);
      localEnumSet.remove(Reserved15);
      return value(localEnumSet);
    }
  }
  
  private static enum Offset
  {
    tableVersion(0),  fontRevision(4),  checkSumAdjustment(8),  magicNumber(12),  flags(16),  unitsPerEm(18),  created(20),  modified(28),  xMin(36),  yMin(38),  xMax(40),  yMax(42),  macStyle(44),  lowestRecPPEM(46),  fontDirectionHint(48),  indexToLocFormat(50),  glyphDataFormat(52);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\FontHeaderTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */