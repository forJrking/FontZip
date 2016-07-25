package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;
import java.util.EnumSet;
import java.util.Iterator;

public final class OS2Table
  extends Table
{
  private OS2Table(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int tableVersion()
  {
    return this.data.readUShort(Offset.version.offset);
  }
  
  public int xAvgCharWidth()
  {
    return this.data.readShort(Offset.xAvgCharWidth.offset);
  }
  
  public int usWeightClass()
  {
    return this.data.readUShort(Offset.usWeightClass.offset);
  }
  
  public int usWidthClass()
  {
    return this.data.readUShort(Offset.usWidthClass.offset);
  }
  
  public EnumSet<EmbeddingFlags> fsType()
  {
    return EmbeddingFlags.asSet(fsTypeAsInt());
  }
  
  public int fsTypeAsInt()
  {
    return this.data.readUShort(Offset.fsType.offset);
  }
  
  public int ySubscriptXSize()
  {
    return this.data.readShort(Offset.ySubscriptXSize.offset);
  }
  
  public int ySubscriptYSize()
  {
    return this.data.readShort(Offset.ySubscriptYSize.offset);
  }
  
  public int ySubscriptXOffset()
  {
    return this.data.readShort(Offset.ySubscriptXOffset.offset);
  }
  
  public int ySubscriptYOffset()
  {
    return this.data.readShort(Offset.ySubscriptYOffset.offset);
  }
  
  public int ySuperscriptXSize()
  {
    return this.data.readShort(Offset.ySuperscriptXSize.offset);
  }
  
  public int ySuperscriptYSize()
  {
    return this.data.readShort(Offset.ySuperscriptYSize.offset);
  }
  
  public int ySuperscriptXOffset()
  {
    return this.data.readShort(Offset.ySuperscriptXOffset.offset);
  }
  
  public int ySuperscriptYOffset()
  {
    return this.data.readShort(Offset.ySuperscriptYOffset.offset);
  }
  
  public int yStrikeoutSize()
  {
    return this.data.readShort(Offset.yStrikeoutSize.offset);
  }
  
  public int yStrikeoutPosition()
  {
    return this.data.readShort(Offset.yStrikeoutPosition.offset);
  }
  
  public int sFamilyClass()
  {
    return this.data.readShort(Offset.sFamilyClass.offset);
  }
  
  public byte[] panose()
  {
    byte[] arrayOfByte = new byte[10];
    this.data.readBytes(Offset.panose.offset, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }
  
  public long ulUnicodeRange1()
  {
    return this.data.readULong(Offset.ulUnicodeRange1.offset);
  }
  
  public long ulUnicodeRange2()
  {
    return this.data.readULong(Offset.ulUnicodeRange2.offset);
  }
  
  public long ulUnicodeRange3()
  {
    return this.data.readULong(Offset.ulUnicodeRange3.offset);
  }
  
  public long ulUnicodeRange4()
  {
    return this.data.readULong(Offset.ulUnicodeRange4.offset);
  }
  
  public EnumSet<UnicodeRange> ulUnicodeRange()
  {
    return UnicodeRange.asSet(ulUnicodeRange1(), ulUnicodeRange2(), ulUnicodeRange3(), ulUnicodeRange4());
  }
  
  public byte[] achVendId()
  {
    byte[] arrayOfByte = new byte[4];
    this.data.readBytes(Offset.achVendId.offset, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }
  
  public int fsSelectionAsInt()
  {
    return this.data.readUShort(Offset.fsSelection.offset);
  }
  
  public EnumSet<FsSelection> fsSelection()
  {
    return FsSelection.asSet(fsSelectionAsInt());
  }
  
  public int usFirstCharIndex()
  {
    return this.data.readUShort(Offset.usFirstCharIndex.offset);
  }
  
  public int usLastCharIndex()
  {
    return this.data.readUShort(Offset.usLastCharIndex.offset);
  }
  
  public int sTypoAscender()
  {
    return this.data.readShort(Offset.sTypoAscender.offset);
  }
  
  public int sTypoDescender()
  {
    return this.data.readShort(Offset.sTypoDescender.offset);
  }
  
  public int sTypoLineGap()
  {
    return this.data.readShort(Offset.sTypoLineGap.offset);
  }
  
  public int usWinAscent()
  {
    return this.data.readUShort(Offset.usWinAscent.offset);
  }
  
  public int usWinDescent()
  {
    return this.data.readUShort(Offset.usWinDescent.offset);
  }
  
  public long ulCodePageRange1()
  {
    return this.data.readULong(Offset.ulCodePageRange1.offset);
  }
  
  public long ulCodePageRange2()
  {
    return this.data.readULong(Offset.ulCodePageRange2.offset);
  }
  
  public EnumSet<CodePageRange> ulCodePageRange()
  {
    return CodePageRange.asSet(ulCodePageRange1(), ulCodePageRange1());
  }
  
  public int sxHeight()
  {
    return this.data.readShort(Offset.sxHeight.offset);
  }
  
  public int sCapHeight()
  {
    return this.data.readShort(Offset.sCapHeight.offset);
  }
  
  public int usDefaultChar()
  {
    return this.data.readUShort(Offset.usDefaultChar.offset);
  }
  
  public int usBreakChar()
  {
    return this.data.readUShort(Offset.usBreakChar.offset);
  }
  
  public int usMaxContext()
  {
    return this.data.readUShort(Offset.usMaxContext.offset);
  }
  
  public static class Builder
    extends TableBasedTableBuilder<OS2Table>
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
    
    protected OS2Table subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new OS2Table(header(), paramReadableFontData, null);
    }
    
    public int tableVersion()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.version));
    }
    
    public void setTableVersion(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.version), paramInt);
    }
    
    public int xAvgCharWidth()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.xAvgCharWidth));
    }
    
    public void setXAvgCharWidth(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.xAvgCharWidth), paramInt);
    }
    
    public int usWeightClass()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWeightClass));
    }
    
    public void setUsWeightClass(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWeightClass), paramInt);
    }
    
    public int usWidthClass()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWidthClass));
    }
    
    public void setUsWidthClass(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWidthClass), paramInt);
    }
    
    public EnumSet<OS2Table.EmbeddingFlags> fsType()
    {
      return OS2Table.EmbeddingFlags.asSet(fsTypeAsInt());
    }
    
    public int fsTypeAsInt()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.fsType));
    }
    
    public void setFsType(EnumSet<OS2Table.EmbeddingFlags> paramEnumSet)
    {
      setFsType(OS2Table.EmbeddingFlags.asUShort(paramEnumSet));
    }
    
    public void setFsType(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.fsType), paramInt);
    }
    
    public int ySubscriptXSize()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptXSize));
    }
    
    public void setYSubscriptXSize(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptXSize), paramInt);
    }
    
    public int ySubscriptYSize()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptYSize));
    }
    
    public void setYSubscriptYSize(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptYSize), paramInt);
    }
    
    public int ySubscriptXOffset()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptXOffset));
    }
    
    public void setYSubscriptXOffset(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptXOffset), paramInt);
    }
    
    public int ySubscriptYOffset()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptYOffset));
    }
    
    public void setYSubscriptYOffset(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySubscriptYOffset), paramInt);
    }
    
    public int ySuperscriptXSize()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptXSize));
    }
    
    public void setYSuperscriptXSize(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptXSize), paramInt);
    }
    
    public int ySuperscriptYSize()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptYSize));
    }
    
    public void setYSuperscriptYSize(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptYSize), paramInt);
    }
    
    public int ySuperscriptXOffset()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptXOffset));
    }
    
    public void setYSuperscriptXOffset(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptXOffset), paramInt);
    }
    
    public int ySuperscriptYOffset()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptYOffset));
    }
    
    public void setYSuperscriptYOffset(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.ySuperscriptYOffset), paramInt);
    }
    
    public int yStrikeoutSize()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.yStrikeoutSize));
    }
    
    public void setYStrikeoutSize(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.yStrikeoutSize), paramInt);
    }
    
    public int yStrikeoutPosition()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.yStrikeoutPosition));
    }
    
    public void setYStrikeoutPosition(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.yStrikeoutPosition), paramInt);
    }
    
    public int sFamilyClass()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sFamilyClass));
    }
    
    public void setSFamilyClass(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sFamilyClass), paramInt);
    }
    
    public byte[] panose()
    {
      byte[] arrayOfByte = new byte[OS2Table.Offset.access$000(OS2Table.Offset.panoseLength)];
      internalReadData().readBytes(OS2Table.Offset.access$000(OS2Table.Offset.panose), arrayOfByte, 0, arrayOfByte.length);
      return arrayOfByte;
    }
    
    public void setPanose(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte.length != OS2Table.Offset.access$000(OS2Table.Offset.panoseLength)) {
        throw new IllegalArgumentException("Panose bytes must be exactly 10 in length.");
      }
      internalWriteData().writeBytes(OS2Table.Offset.access$000(OS2Table.Offset.panose), paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public long ulUnicodeRange1()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange1));
    }
    
    public void setUlUnicodeRange1(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange1), paramLong);
    }
    
    public long ulUnicodeRange2()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange2));
    }
    
    public void setUlUnicodeRange2(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange2), paramLong);
    }
    
    public long ulUnicodeRange3()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange3));
    }
    
    public void setUlUnicodeRange3(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange3), paramLong);
    }
    
    public long ulUnicodeRange4()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange4));
    }
    
    public void setUlUnicodeRange4(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulUnicodeRange4), paramLong);
    }
    
    public EnumSet<OS2Table.UnicodeRange> ulUnicodeRange()
    {
      return OS2Table.UnicodeRange.asSet(ulUnicodeRange1(), ulUnicodeRange2(), ulUnicodeRange3(), ulUnicodeRange4());
    }
    
    public void setUlUnicodeRange(EnumSet<OS2Table.UnicodeRange> paramEnumSet)
    {
      long[] arrayOfLong = OS2Table.UnicodeRange.asArray(paramEnumSet);
      setUlUnicodeRange1(arrayOfLong[0]);
      setUlUnicodeRange2(arrayOfLong[1]);
      setUlUnicodeRange3(arrayOfLong[2]);
      setUlUnicodeRange4(arrayOfLong[3]);
    }
    
    public byte[] achVendId()
    {
      byte[] arrayOfByte = new byte[OS2Table.Offset.access$000(OS2Table.Offset.achVendIdLength)];
      internalReadData().readBytes(OS2Table.Offset.access$000(OS2Table.Offset.achVendId), arrayOfByte, 0, arrayOfByte.length);
      return arrayOfByte;
    }
    
    public void setAchVendId(byte[] paramArrayOfByte)
    {
      internalWriteData().writeBytesPad(OS2Table.Offset.access$000(OS2Table.Offset.achVendId), paramArrayOfByte, 0, OS2Table.Offset.access$000(OS2Table.Offset.achVendIdLength), (byte)32);
    }
    
    public int fsSelectionAsInt()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.fsSelection));
    }
    
    public void setFsSelection(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.fsSelection), paramInt);
    }
    
    public void fsSelection(EnumSet<OS2Table.FsSelection> paramEnumSet)
    {
      setFsSelection(OS2Table.FsSelection.asInt(paramEnumSet));
    }
    
    public int usFirstCharIndex()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usFirstCharIndex));
    }
    
    public void setUsFirstCharIndex(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usFirstCharIndex), paramInt);
    }
    
    public int usLastCharIndex()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usLastCharIndex));
    }
    
    public void setUsLastCharIndex(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usLastCharIndex), paramInt);
    }
    
    public int sTypoAscender()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoAscender));
    }
    
    public void setSTypoAscender(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoAscender), paramInt);
    }
    
    public int sTypoDescender()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoDescender));
    }
    
    public void setSTypoDescender(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoDescender), paramInt);
    }
    
    public int sTypoLineGap()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoLineGap));
    }
    
    public void setSTypoLineGap(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sTypoLineGap), paramInt);
    }
    
    public int usWinAscent()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWinAscent));
    }
    
    public void setUsWinAscent(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWinAscent), paramInt);
    }
    
    public int usWinDescent()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWinDescent));
    }
    
    public void setUsWinDescent(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usWinAscent), paramInt);
    }
    
    public long ulCodePageRange1()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulCodePageRange1));
    }
    
    public void setUlCodePageRange1(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulCodePageRange1), paramLong);
    }
    
    public long ulCodePageRange2()
    {
      return internalReadData().readULong(OS2Table.Offset.access$000(OS2Table.Offset.ulCodePageRange2));
    }
    
    public void setUlCodePageRange2(long paramLong)
    {
      internalWriteData().writeULong(OS2Table.Offset.access$000(OS2Table.Offset.ulCodePageRange2), paramLong);
    }
    
    public EnumSet<OS2Table.CodePageRange> ulCodePageRange()
    {
      return OS2Table.CodePageRange.asSet(ulCodePageRange1(), ulCodePageRange2());
    }
    
    public void setUlCodePageRange(EnumSet<OS2Table.CodePageRange> paramEnumSet)
    {
      long[] arrayOfLong = OS2Table.CodePageRange.asArray(paramEnumSet);
      setUlCodePageRange1(arrayOfLong[0]);
      setUlCodePageRange2(arrayOfLong[1]);
    }
    
    public int sxHeight()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sxHeight));
    }
    
    public void setSxHeight(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sxHeight), paramInt);
    }
    
    public int sCapHeight()
    {
      return internalReadData().readShort(OS2Table.Offset.access$000(OS2Table.Offset.sCapHeight));
    }
    
    public void setSCapHeight(int paramInt)
    {
      internalWriteData().writeShort(OS2Table.Offset.access$000(OS2Table.Offset.sCapHeight), paramInt);
    }
    
    public int usDefaultChar()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usDefaultChar));
    }
    
    public void setUsDefaultChar(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usDefaultChar), paramInt);
    }
    
    public int usBreakChar()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usBreakChar));
    }
    
    public void setUsBreakChar(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usBreakChar), paramInt);
    }
    
    public int usMaxContext()
    {
      return internalReadData().readUShort(OS2Table.Offset.access$000(OS2Table.Offset.usMaxContext));
    }
    
    public void setUsMaxContext(int paramInt)
    {
      internalWriteData().writeUShort(OS2Table.Offset.access$000(OS2Table.Offset.usMaxContext), paramInt);
    }
  }
  
  public static enum CodePageRange
  {
    Latin1_1252,  Latin2_1250,  Cyrillic_1251,  Greek_1253,  Turkish_1254,  Hebrew_1255,  Arabic_1256,  WindowsBaltic_1257,  Vietnamese_1258,  AlternateANSI9,  AlternateANSI10,  AlternateANSI11,  AlternateANSI12,  AlternateANSI13,  AlternateANSI14,  AlternateANSI15,  Thai_874,  JapanJIS_932,  ChineseSimplified_936,  KoreanWansung_949,  ChineseTraditional_950,  KoreanJohab_1361,  AlternateANSI22,  AlternateANSI23,  AlternateANSI24,  AlternateANSI25,  AlternateANSI26,  AlternateANSI27,  AlternateANSI28,  MacintoshCharacterSet,  OEMCharacterSet,  SymbolCharacterSet,  ReservedForOEM32,  ReservedForOEM33,  ReservedForOEM34,  ReservedForOEM35,  ReservedForOEM36,  ReservedForOEM37,  ReservedForOEM38,  ReservedForOEM39,  ReservedForOEM40,  ReservedForOEM41,  ReservedForOEM42,  ReservedForOEM43,  ReservedForOEM44,  ReservedForOEM45,  ReservedForOEM46,  ReservedForOEM47,  IBMGreek_869,  MSDOSRussion_866,  MSDOSNordic_865,  Arabic_864,  MSDOSCanadianFrench_863,  Hebrew_862,  MSDOSIcelandic_861,  MSDOSPortugese_860,  IBMTurkish_857,  IBMCyrillic_855,  Latin2_852,  MSDOSBaltic_775,  Greek_737,  Arabic_708,  Latin1_850,  US_437;
    
    private CodePageRange() {}
    
    public static OS2Table.UnicodeRange range(int paramInt)
    {
      if (paramInt > OS2Table.UnicodeRange.values().length) {
        return null;
      }
      return OS2Table.UnicodeRange.values()[paramInt];
    }
    
    public static EnumSet<CodePageRange> asSet(long paramLong1, long paramLong2)
    {
      EnumSet localEnumSet = EnumSet.noneOf(CodePageRange.class);
      long[] arrayOfLong = { paramLong1, paramLong2 };
      int i = 0;
      int j = -1;
      for (CodePageRange localCodePageRange : values())
      {
        if (localCodePageRange.ordinal() % 32 == 0)
        {
          i = 0;
          j++;
        }
        else
        {
          i++;
        }
        if ((arrayOfLong[j] & 1 << i) == 1 << i) {
          localEnumSet.add(localCodePageRange);
        }
      }
      return localEnumSet;
    }
    
    public static long[] asArray(EnumSet<CodePageRange> paramEnumSet)
    {
      long[] arrayOfLong = new long[4];
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        CodePageRange localCodePageRange = (CodePageRange)localIterator.next();
        int i = localCodePageRange.ordinal() / 32;
        long l = 1 << localCodePageRange.ordinal() % 32;
        arrayOfLong[i] |= l;
      }
      return arrayOfLong;
    }
  }
  
  public static enum FsSelection
  {
    ITALIC,  UNDERSCORE,  NEGATIVE,  OUTLINED,  STRIKEOUT,  BOLD,  REGULAR,  USE_TYPO_METRICS,  WWS,  OBLIQUE;
    
    private FsSelection() {}
    
    public int mask()
    {
      return 1 << ordinal();
    }
    
    public static EnumSet<FsSelection> asSet(int paramInt)
    {
      EnumSet localEnumSet = EnumSet.noneOf(FsSelection.class);
      for (FsSelection localFsSelection : values()) {
        if ((paramInt & localFsSelection.mask()) == localFsSelection.mask()) {
          localEnumSet.add(localFsSelection);
        }
      }
      return localEnumSet;
    }
    
    public static int asInt(EnumSet<FsSelection> paramEnumSet)
    {
      int i = 0;
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        FsSelection localFsSelection = (FsSelection)localIterator.next();
        i |= localFsSelection.mask();
      }
      return i;
    }
  }
  
  public static enum UnicodeRange
  {
    BasicLatin,  Latin1Supplement,  LatinExtendedA,  LatinExtendedB,  IPAExtensions,  SpacingModifierLetters,  CombiningDiacriticalMarks,  GreekAndCoptic,  Coptic,  Cyrillic,  Armenian,  Hebrew,  Vai,  Arabic,  NKo,  Devanagari,  Bengali,  Gurmukhi,  Gujarati,  Oriya,  Tamil,  Telugu,  Kannada,  Malayalam,  Thai,  Lao,  Georgian,  Balinese,  HangulJamo,  LatinExtendedAdditional,  GreekExtended,  GeneralPunctuation,  SuperscriptsAndSubscripts,  CurrencySymbols,  NumberForms,  Arrows,  MathematicalOperators,  MiscTechnical,  ControlPictures,  OCR,  EnclosedAlphanumerics,  BoxDrawing,  BlockElements,  GeometricShapes,  MiscSymbols,  Dingbats,  CJKSymbolsAndPunctuation,  Hiragana,  Katakana,  Bopomofo,  HangulCompatibilityJamo,  Phagspa,  EnclosedCJKLettersAndMonths,  CJKCompatibility,  HangulSyllables,  NonPlane0,  Phoenician,  CJKUnifiedIdeographs,  PrivateUseAreaPlane0,  CJKStrokes,  AlphabeticPresentationForms,  ArabicPresentationFormsA,  CombiningHalfMarks,  VerticalForms,  SmallFormVariants,  ArabicPresentationFormsB,  HalfwidthAndFullwidthForms,  Specials,  Tibetan,  Syriac,  Thaana,  Sinhala,  Myanmar,  Ethiopic,  Cherokee,  UnifiedCanadianAboriginalSyllabics,  Ogham,  Runic,  Khmer,  Mongolian,  BraillePatterns,  YiSyllables,  Tagalog,  OldItalic,  Gothic,  Deseret,  MusicalSymbols,  MathematicalAlphanumericSymbols,  PrivateUsePlane15And16,  VariationSelectors,  Tags,  Limbu,  TaiLe,  NewTaiLue,  Buginese,  Glagolitic,  Tifnagh,  YijingHexagramSymbols,  SylotiNagari,  LinearB,  AncientGreekNumbers,  Ugaritic,  OldPersian,  Shavian,  Osmanya,  CypriotSyllabary,  Kharoshthi,  TaiXuanJingSymbols,  Cuneiform,  CountingRodNumerals,  Sudanese,  Lepcha,  OlChiki,  Saurashtra,  KayahLi,  Rejang,  Charm,  AncientSymbols,  PhaistosDisc,  Carian,  DominoTiles,  Reserved123,  Reserved124,  Reserved125,  Reserved126,  Reserved127;
    
    private UnicodeRange() {}
    
    public static UnicodeRange range(int paramInt)
    {
      if (paramInt > values().length) {
        return null;
      }
      return values()[paramInt];
    }
    
    public static EnumSet<UnicodeRange> asSet(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
    {
      EnumSet localEnumSet = EnumSet.noneOf(UnicodeRange.class);
      long[] arrayOfLong = { paramLong1, paramLong2, paramLong3, paramLong4 };
      int i = 0;
      int j = -1;
      for (UnicodeRange localUnicodeRange : values())
      {
        if (localUnicodeRange.ordinal() % 32 == 0)
        {
          i = 0;
          j++;
        }
        else
        {
          i++;
        }
        if ((arrayOfLong[j] & 1 << i) == 1 << i) {
          localEnumSet.add(localUnicodeRange);
        }
      }
      return localEnumSet;
    }
    
    public static long[] asArray(EnumSet<UnicodeRange> paramEnumSet)
    {
      long[] arrayOfLong = new long[4];
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        UnicodeRange localUnicodeRange = (UnicodeRange)localIterator.next();
        int i = localUnicodeRange.ordinal() / 32;
        long l = 1 << localUnicodeRange.ordinal() % 32;
        arrayOfLong[i] |= l;
      }
      return arrayOfLong;
    }
  }
  
  public static enum EmbeddingFlags
  {
    Reserved0,  RestrictedLicenseEmbedding,  PreviewAndPrintEmbedding,  EditableEmbedding,  Reserved4,  Reserved5,  Reserved6,  Reserved7,  NoSubsetting,  BitmapEmbeddingOnly,  Reserved10,  Reserved11,  Reserved12,  Reserved13,  Reserved14,  Reserved15;
    
    private EmbeddingFlags() {}
    
    public int mask()
    {
      return 1 << ordinal();
    }
    
    public static EnumSet<EmbeddingFlags> asSet(int paramInt)
    {
      EnumSet localEnumSet = EnumSet.noneOf(EmbeddingFlags.class);
      for (EmbeddingFlags localEmbeddingFlags : values()) {
        if ((paramInt & localEmbeddingFlags.mask()) == localEmbeddingFlags.mask()) {
          localEnumSet.add(localEmbeddingFlags);
        }
      }
      return localEnumSet;
    }
    
    public static int asUShort(EnumSet<EmbeddingFlags> paramEnumSet)
    {
      int i = 0;
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        EmbeddingFlags localEmbeddingFlags = (EmbeddingFlags)localIterator.next();
        i |= localEmbeddingFlags.mask();
      }
      return i;
    }
    
    public static boolean isInstallableEditing(EnumSet<EmbeddingFlags> paramEnumSet)
    {
      return paramEnumSet.isEmpty();
    }
    
    public static boolean isInstallableEditing(int paramInt)
    {
      return paramInt == 0;
    }
  }
  
  public static enum WidthClass
  {
    UltraCondensed(1),  ExtraCondensed(2),  Condensed(3),  SemiCondensed(4),  Medium(5),  Normal(5),  SemiExpanded(6),  Expanded(7),  ExtraExpanded(8),  UltraExpanded(9);
    
    private final int value;
    
    private WidthClass(int paramInt)
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
    
    public static OS2Table.WeightClass valueOf(int paramInt)
    {
      for (OS2Table.WeightClass localWeightClass : ) {
        if (localWeightClass.equals(paramInt)) {
          return localWeightClass;
        }
      }
      return null;
    }
  }
  
  public static enum WeightClass
  {
    Thin(100),  ExtraLight(200),  UltraLight(200),  Light(300),  Normal(400),  Regular(400),  Medium(500),  SemiBold(600),  DemiBold(600),  Bold(700),  ExtraBold(800),  UltraBold(800),  Black(900),  Heavy(900);
    
    private final int value;
    
    private WeightClass(int paramInt)
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
    
    public static WeightClass valueOf(int paramInt)
    {
      for (WeightClass localWeightClass : ) {
        if (localWeightClass.equals(paramInt)) {
          return localWeightClass;
        }
      }
      return null;
    }
  }
  
  private static enum Offset
  {
    version(0),  xAvgCharWidth(2),  usWeightClass(4),  usWidthClass(6),  fsType(8),  ySubscriptXSize(10),  ySubscriptYSize(12),  ySubscriptXOffset(14),  ySubscriptYOffset(16),  ySuperscriptXSize(18),  ySuperscriptYSize(20),  ySuperscriptXOffset(22),  ySuperscriptYOffset(24),  yStrikeoutSize(26),  yStrikeoutPosition(28),  sFamilyClass(30),  panose(32),  panoseLength(10),  ulUnicodeRange1(42),  ulUnicodeRange2(46),  ulUnicodeRange3(50),  ulUnicodeRange4(54),  achVendId(58),  achVendIdLength(4),  fsSelection(62),  usFirstCharIndex(64),  usLastCharIndex(66),  sTypoAscender(68),  sTypoDescender(70),  sTypoLineGap(72),  usWinAscent(74),  usWinDescent(76),  ulCodePageRange1(78),  ulCodePageRange2(82),  sxHeight(86),  sCapHeight(88),  usDefaultChar(90),  usBreakChar(92),  usMaxContext(94);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\OS2Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */