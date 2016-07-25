package com.google.typography.font.sfntly;

import com.google.typography.font.sfntly.data.FontInputStream;
import com.google.typography.font.sfntly.data.FontOutputStream;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.math.Fixed1616;
import com.google.typography.font.sfntly.math.FontMath;
import com.google.typography.font.sfntly.table.FontDataTable;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.Table.Builder;
import com.google.typography.font.sfntly.table.core.FontHeaderTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalDeviceMetricsTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalHeaderTable.Builder;
import com.google.typography.font.sfntly.table.core.HorizontalMetricsTable.Builder;
import com.google.typography.font.sfntly.table.core.MaximumProfileTable.Builder;
import com.google.typography.font.sfntly.table.truetype.LocaTable.Builder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

public class Font
{
  private static final Logger logger = Logger.getLogger(Font.class.getCanonicalName());
  private static final List<Integer> CFF_TABLE_ORDERING;
  private static final List<Integer> TRUE_TYPE_TABLE_ORDERING;
  public static final int SFNTVERSION_1 = Fixed1616.fixed(1, 0);
  private final int sfntVersion;
  private final byte[] digest;
  private long checksum;
  private Map<Integer, ? extends Table> tables;
  
  private Font(int paramInt, byte[] paramArrayOfByte)
  {
    this.sfntVersion = paramInt;
    this.digest = paramArrayOfByte;
  }
  
  public int sfntVersion()
  {
    return this.sfntVersion;
  }
  
  public byte[] digest()
  {
    if (this.digest == null) {
      return null;
    }
    return Arrays.copyOf(this.digest, this.digest.length);
  }
  
  public long checksum()
  {
    return this.checksum;
  }
  
  public int numTables()
  {
    return this.tables.size();
  }
  
  public Iterator<? extends Table> iterator()
  {
    return this.tables.values().iterator();
  }
  
  public boolean hasTable(int paramInt)
  {
    return this.tables.containsKey(Integer.valueOf(paramInt));
  }
  
  public <T extends Table> T getTable(int paramInt)
  {
    return (Table)this.tables.get(Integer.valueOf(paramInt));
  }
  
  public Map<Integer, ? extends Table> tableMap()
  {
    return Collections.unmodifiableMap(this.tables);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("digest = ");
    byte[] arrayOfByte = digest();
    if (arrayOfByte != null) {
      for (int i = 0; i < arrayOfByte.length; i++)
      {
        int j = 0xFF & arrayOfByte[i];
        if (j < 16) {
          localStringBuilder.append("0");
        }
        localStringBuilder.append(Integer.toHexString(j));
      }
    }
    localStringBuilder.append("\n[");
    localStringBuilder.append(Fixed1616.toString(this.sfntVersion));
    localStringBuilder.append(", ");
    localStringBuilder.append(numTables());
    localStringBuilder.append("]\n");
    Iterator localIterator = iterator();
    while (localIterator.hasNext())
    {
      FontDataTable localFontDataTable = (FontDataTable)localIterator.next();
      localStringBuilder.append("\t");
      localStringBuilder.append(localFontDataTable);
      localStringBuilder.append("\n");
    }
    return localStringBuilder.toString();
  }
  
  void serialize(OutputStream paramOutputStream, List<Integer> paramList)
    throws IOException
  {
    List localList1 = generateTableOrdering(paramList);
    List localList2 = buildTableHeadersForSerialization(localList1);
    FontOutputStream localFontOutputStream = new FontOutputStream(paramOutputStream);
    serializeHeader(localFontOutputStream, localList2);
    serializeTables(localFontOutputStream, localList2);
  }
  
  private List<Header> buildTableHeadersForSerialization(List<Integer> paramList)
  {
    List localList = generateTableOrdering(paramList);
    ArrayList localArrayList = new ArrayList(numTables());
    int i = Offset.tableRecordBegin.offset + numTables() * Offset.tableRecordSize.offset;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      Table localTable = (Table)this.tables.get(localInteger);
      if (localTable != null)
      {
        localArrayList.add(new Header(localInteger.intValue(), localTable.calculatedChecksum(), i, localTable.header().length()));
        i += (localTable.dataLength() + 3 & 0xFFFFFFFC);
      }
    }
    return localArrayList;
  }
  
  private void serializeHeader(FontOutputStream paramFontOutputStream, List<Header> paramList)
    throws IOException
  {
    paramFontOutputStream.writeFixed(this.sfntVersion);
    paramFontOutputStream.writeUShort(paramList.size());
    int i = FontMath.log2(paramList.size());
    int j = 2 << i - 1 + 4;
    paramFontOutputStream.writeUShort(j);
    paramFontOutputStream.writeUShort(i);
    paramFontOutputStream.writeUShort(paramList.size() * 16 - j);
    ArrayList localArrayList = new ArrayList(paramList);
    Collections.sort(localArrayList, Header.COMPARATOR_BY_TAG);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      Header localHeader = (Header)localIterator.next();
      paramFontOutputStream.writeULong(localHeader.tag());
      paramFontOutputStream.writeULong(localHeader.checksum());
      paramFontOutputStream.writeULong(localHeader.offset());
      paramFontOutputStream.writeULong(localHeader.length());
    }
  }
  
  private void serializeTables(FontOutputStream paramFontOutputStream, List<Header> paramList)
    throws IOException
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Header localHeader = (Header)localIterator.next();
      Table localTable = getTable(localHeader.tag());
      if (localTable == null) {
        throw new IOException("Table out of sync with font header.");
      }
      int i = localTable.serialize(paramFontOutputStream);
      int j = (i + 3 & 0xFFFFFFFC) - i;
      for (int k = 0; k < j; k++) {
        paramFontOutputStream.write(0);
      }
    }
  }
  
  private List<Integer> generateTableOrdering(List<Integer> paramList)
  {
    ArrayList localArrayList = new ArrayList(this.tables.size());
    if (paramList == null) {
      paramList = defaultTableOrdering();
    }
    TreeSet localTreeSet = new TreeSet(this.tables.keySet());
    Iterator localIterator = paramList.iterator();
    Integer localInteger;
    while (localIterator.hasNext())
    {
      localInteger = (Integer)localIterator.next();
      if (hasTable(localInteger.intValue()))
      {
        localArrayList.add(localInteger);
        localTreeSet.remove(localInteger);
      }
    }
    localIterator = localTreeSet.iterator();
    while (localIterator.hasNext())
    {
      localInteger = (Integer)localIterator.next();
      localArrayList.add(localInteger);
    }
    return localArrayList;
  }
  
  private List<Integer> defaultTableOrdering()
  {
    if (hasTable(Tag.CFF)) {
      return CFF_TABLE_ORDERING;
    }
    return TRUE_TYPE_TABLE_ORDERING;
  }
  
  static
  {
    Integer[] arrayOfInteger1 = { Integer.valueOf(Tag.head), Integer.valueOf(Tag.hhea), Integer.valueOf(Tag.maxp), Integer.valueOf(Tag.OS_2), Integer.valueOf(Tag.name), Integer.valueOf(Tag.cmap), Integer.valueOf(Tag.post), Integer.valueOf(Tag.CFF) };
    ArrayList localArrayList1 = new ArrayList(arrayOfInteger1.length);
    Collections.addAll(localArrayList1, arrayOfInteger1);
    CFF_TABLE_ORDERING = Collections.unmodifiableList(localArrayList1);
    Integer[] arrayOfInteger2 = { Integer.valueOf(Tag.head), Integer.valueOf(Tag.hhea), Integer.valueOf(Tag.maxp), Integer.valueOf(Tag.OS_2), Integer.valueOf(Tag.hmtx), Integer.valueOf(Tag.LTSH), Integer.valueOf(Tag.VDMX), Integer.valueOf(Tag.hdmx), Integer.valueOf(Tag.cmap), Integer.valueOf(Tag.fpgm), Integer.valueOf(Tag.prep), Integer.valueOf(Tag.cvt), Integer.valueOf(Tag.loca), Integer.valueOf(Tag.glyf), Integer.valueOf(Tag.kern), Integer.valueOf(Tag.name), Integer.valueOf(Tag.post), Integer.valueOf(Tag.gasp), Integer.valueOf(Tag.PCLT), Integer.valueOf(Tag.DSIG) };
    ArrayList localArrayList2 = new ArrayList(arrayOfInteger2.length);
    Collections.addAll(localArrayList2, arrayOfInteger2);
    TRUE_TYPE_TABLE_ORDERING = Collections.unmodifiableList(localArrayList2);
  }
  
  public static final class Builder
  {
    private Map<Integer, Table.Builder<? extends Table>> tableBuilders;
    private FontFactory factory;
    private int sfntVersion = Font.SFNTVERSION_1;
    private int numTables;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;
    private Map<Header, WritableFontData> dataBlocks;
    private byte[] digest;
    
    private Builder(FontFactory paramFontFactory)
    {
      this.factory = paramFontFactory;
      this.tableBuilders = new HashMap();
    }
    
    private void loadFont(InputStream paramInputStream)
      throws IOException
    {
      if (paramInputStream == null) {
        throw new IOException("No input stream for font.");
      }
      FontInputStream localFontInputStream = null;
      try
      {
        localFontInputStream = new FontInputStream(paramInputStream);
        SortedSet localSortedSet = readHeader(localFontInputStream);
        this.dataBlocks = loadTableData(localSortedSet, localFontInputStream);
        this.tableBuilders = buildAllTableBuilders(this.dataBlocks);
      }
      finally
      {
        localFontInputStream.close();
      }
    }
    
    private void loadFont(WritableFontData paramWritableFontData, int paramInt)
      throws IOException
    {
      if (paramWritableFontData == null) {
        throw new IOException("No data for font.");
      }
      SortedSet localSortedSet = readHeader(paramWritableFontData, paramInt);
      this.dataBlocks = loadTableData(localSortedSet, paramWritableFontData);
      this.tableBuilders = buildAllTableBuilders(this.dataBlocks);
    }
    
    static final Builder getOTFBuilder(FontFactory paramFontFactory, InputStream paramInputStream)
      throws IOException
    {
      Builder localBuilder = new Builder(paramFontFactory);
      localBuilder.loadFont(paramInputStream);
      return localBuilder;
    }
    
    static final Builder getOTFBuilder(FontFactory paramFontFactory, WritableFontData paramWritableFontData, int paramInt)
      throws IOException
    {
      Builder localBuilder = new Builder(paramFontFactory);
      localBuilder.loadFont(paramWritableFontData, paramInt);
      return localBuilder;
    }
    
    static final Builder getOTFBuilder(FontFactory paramFontFactory)
    {
      return new Builder(paramFontFactory);
    }
    
    public FontFactory getFontFactory()
    {
      return this.factory;
    }
    
    public boolean readyToBuild()
    {
      if ((this.tableBuilders == null) && (this.dataBlocks != null) && (this.dataBlocks.size() > 0)) {
        return true;
      }
      Iterator localIterator = this.tableBuilders.values().iterator();
      while (localIterator.hasNext())
      {
        Table.Builder localBuilder = (Table.Builder)localIterator.next();
        if (!localBuilder.readyToBuild()) {
          return false;
        }
      }
      return true;
    }
    
    public Font build()
    {
      Map localMap = null;
      Font localFont = new Font(this.sfntVersion, this.digest, null);
      if (this.tableBuilders.size() > 0) {
        localMap = buildTablesFromBuilders(localFont, this.tableBuilders);
      }
      localFont.tables = localMap;
      this.tableBuilders = null;
      this.dataBlocks = null;
      return localFont;
    }
    
    public void setDigest(byte[] paramArrayOfByte)
    {
      this.digest = paramArrayOfByte;
    }
    
    public void clearTableBuilders()
    {
      this.tableBuilders.clear();
    }
    
    public boolean hasTableBuilder(int paramInt)
    {
      return this.tableBuilders.containsKey(Integer.valueOf(paramInt));
    }
    
    public Table.Builder<? extends Table> getTableBuilder(int paramInt)
    {
      Table.Builder localBuilder = (Table.Builder)this.tableBuilders.get(Integer.valueOf(paramInt));
      return localBuilder;
    }
    
    public Table.Builder<? extends Table> newTableBuilder(int paramInt)
    {
      Header localHeader = new Header(paramInt);
      Table.Builder localBuilder = Table.Builder.getBuilder(localHeader, null);
      this.tableBuilders.put(Integer.valueOf(localHeader.tag()), localBuilder);
      return localBuilder;
    }
    
    public Table.Builder<? extends Table> newTableBuilder(int paramInt, ReadableFontData paramReadableFontData)
    {
      WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramReadableFontData.length());
      paramReadableFontData.copyTo(localWritableFontData);
      Header localHeader = new Header(paramInt, localWritableFontData.length());
      Table.Builder localBuilder = Table.Builder.getBuilder(localHeader, localWritableFontData);
      this.tableBuilders.put(Integer.valueOf(paramInt), localBuilder);
      return localBuilder;
    }
    
    public Map<Integer, Table.Builder<? extends Table>> tableBuilderMap()
    {
      return Collections.unmodifiableMap(this.tableBuilders);
    }
    
    public Table.Builder<? extends Table> removeTableBuilder(int paramInt)
    {
      return (Table.Builder)this.tableBuilders.remove(Integer.valueOf(paramInt));
    }
    
    public int tableBuilderCount()
    {
      return this.tableBuilders.size();
    }
    
    private int sfntWrapperSize()
    {
      return Font.Offset.access$000(Font.Offset.sfntHeaderSize) + Font.Offset.access$000(Font.Offset.tableRecordSize) * this.tableBuilders.size();
    }
    
    private Map<Integer, Table.Builder<? extends Table>> buildAllTableBuilders(Map<Header, WritableFontData> paramMap)
    {
      HashMap localHashMap = new HashMap();
      Set localSet = paramMap.keySet();
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        Header localHeader = (Header)localIterator.next();
        Table.Builder localBuilder = getTableBuilder(localHeader, (WritableFontData)paramMap.get(localHeader));
        localHashMap.put(Integer.valueOf(localHeader.tag()), localBuilder);
      }
      interRelateBuilders(localHashMap);
      return localHashMap;
    }
    
    private Table.Builder<? extends Table> getTableBuilder(Header paramHeader, WritableFontData paramWritableFontData)
    {
      Table.Builder localBuilder = Table.Builder.getBuilder(paramHeader, paramWritableFontData);
      return localBuilder;
    }
    
    private static Map<Integer, Table> buildTablesFromBuilders(Font paramFont, Map<Integer, Table.Builder<? extends Table>> paramMap)
    {
      TreeMap localTreeMap = new TreeMap();
      interRelateBuilders(paramMap);
      long l1 = 0L;
      boolean bool = false;
      FontHeaderTable.Builder localBuilder = null;
      Object localObject = paramMap.values().iterator();
      while (((Iterator)localObject).hasNext())
      {
        Table.Builder localBuilder1 = (Table.Builder)((Iterator)localObject).next();
        Table localTable = null;
        if (Tag.isHeaderTable(localBuilder1.header().tag()))
        {
          localBuilder = (FontHeaderTable.Builder)localBuilder1;
        }
        else
        {
          if (localBuilder1.readyToBuild())
          {
            bool |= localBuilder1.changed();
            localTable = (Table)localBuilder1.build();
          }
          if (localTable == null) {
            throw new RuntimeException("Unable to build table - " + localBuilder1);
          }
          long l2 = localTable.calculatedChecksum();
          l1 += l2;
          localTreeMap.put(Integer.valueOf(localTable.header().tag()), localTable);
        }
      }
      localObject = null;
      if (localBuilder != null)
      {
        if (bool) {
          localBuilder.setFontChecksum(l1);
        }
        if (localBuilder.readyToBuild())
        {
          bool |= localBuilder.changed();
          localObject = localBuilder.build();
        }
        if (localObject == null) {
          throw new RuntimeException("Unable to build table - " + localBuilder);
        }
        l1 += ((Table)localObject).calculatedChecksum();
        localTreeMap.put(Integer.valueOf(((Table)localObject).header().tag()), localObject);
      }
      paramFont.checksum = (l1 & 0xFFFFFFFF);
      return localTreeMap;
    }
    
    private static void interRelateBuilders(Map<Integer, Table.Builder<? extends Table>> paramMap)
    {
      FontHeaderTable.Builder localBuilder = (FontHeaderTable.Builder)paramMap.get(Integer.valueOf(Tag.head));
      HorizontalHeaderTable.Builder localBuilder1 = (HorizontalHeaderTable.Builder)paramMap.get(Integer.valueOf(Tag.hhea));
      MaximumProfileTable.Builder localBuilder2 = (MaximumProfileTable.Builder)paramMap.get(Integer.valueOf(Tag.maxp));
      LocaTable.Builder localBuilder3 = (LocaTable.Builder)paramMap.get(Integer.valueOf(Tag.loca));
      HorizontalMetricsTable.Builder localBuilder4 = (HorizontalMetricsTable.Builder)paramMap.get(Integer.valueOf(Tag.hmtx));
      HorizontalDeviceMetricsTable.Builder localBuilder5 = (HorizontalDeviceMetricsTable.Builder)paramMap.get(Integer.valueOf(Tag.hdmx));
      if (localBuilder4 != null)
      {
        if (localBuilder2 != null) {
          localBuilder4.setNumGlyphs(localBuilder2.numGlyphs());
        }
        if (localBuilder1 != null) {
          localBuilder4.setNumberOfHMetrics(localBuilder1.numberOfHMetrics());
        }
      }
      if (localBuilder3 != null)
      {
        if (localBuilder2 != null) {
          localBuilder3.setNumGlyphs(localBuilder2.numGlyphs());
        }
        if (localBuilder != null) {
          localBuilder3.setFormatVersion(localBuilder.indexToLocFormat());
        }
      }
      if ((localBuilder5 != null) && (localBuilder2 != null)) {
        localBuilder5.setNumGlyphs(localBuilder2.numGlyphs());
      }
    }
    
    private SortedSet<Header> readHeader(FontInputStream paramFontInputStream)
      throws IOException
    {
      TreeSet localTreeSet = new TreeSet(Header.COMPARATOR_BY_OFFSET);
      this.sfntVersion = paramFontInputStream.readFixed();
      this.numTables = paramFontInputStream.readUShort();
      this.searchRange = paramFontInputStream.readUShort();
      this.entrySelector = paramFontInputStream.readUShort();
      this.rangeShift = paramFontInputStream.readUShort();
      for (int i = 0; i < this.numTables; i++)
      {
        Header localHeader = new Header(paramFontInputStream.readULongAsInt(), paramFontInputStream.readULong(), paramFontInputStream.readULongAsInt(), paramFontInputStream.readULongAsInt());
        localTreeSet.add(localHeader);
      }
      return localTreeSet;
    }
    
    private Map<Header, WritableFontData> loadTableData(SortedSet<Header> paramSortedSet, FontInputStream paramFontInputStream)
      throws IOException
    {
      HashMap localHashMap = new HashMap(paramSortedSet.size());
      Font.logger.fine("########  Reading Table Data");
      Iterator localIterator = paramSortedSet.iterator();
      while (localIterator.hasNext())
      {
        Header localHeader = (Header)localIterator.next();
        paramFontInputStream.skip(localHeader.offset() - paramFontInputStream.position());
        Font.logger.finer("\t" + localHeader);
        Font.logger.finest("\t\tStream Position = " + Integer.toHexString((int)paramFontInputStream.position()));
        FontInputStream localFontInputStream = new FontInputStream(paramFontInputStream, localHeader.length());
        WritableFontData localWritableFontData = WritableFontData.createWritableFontData(localHeader.length());
        localWritableFontData.copyFrom(localFontInputStream, localHeader.length());
        localHashMap.put(localHeader, localWritableFontData);
      }
      return localHashMap;
    }
    
    private SortedSet<Header> readHeader(ReadableFontData paramReadableFontData, int paramInt)
    {
      TreeSet localTreeSet = new TreeSet(Header.COMPARATOR_BY_OFFSET);
      this.sfntVersion = paramReadableFontData.readFixed(paramInt + Font.Offset.access$000(Font.Offset.sfntVersion));
      this.numTables = paramReadableFontData.readUShort(paramInt + Font.Offset.access$000(Font.Offset.numTables));
      this.searchRange = paramReadableFontData.readUShort(paramInt + Font.Offset.access$000(Font.Offset.searchRange));
      this.entrySelector = paramReadableFontData.readUShort(paramInt + Font.Offset.access$000(Font.Offset.entrySelector));
      this.rangeShift = paramReadableFontData.readUShort(paramInt + Font.Offset.access$000(Font.Offset.rangeShift));
      int i = paramInt + Font.Offset.access$000(Font.Offset.tableRecordBegin);
      int j = 0;
      while (j < this.numTables)
      {
        Header localHeader = new Header(paramReadableFontData.readULongAsInt(i + Font.Offset.access$000(Font.Offset.tableTag)), paramReadableFontData.readULong(i + Font.Offset.access$000(Font.Offset.tableCheckSum)), paramReadableFontData.readULongAsInt(i + Font.Offset.access$000(Font.Offset.tableOffset)), paramReadableFontData.readULongAsInt(i + Font.Offset.access$000(Font.Offset.tableLength)));
        localTreeSet.add(localHeader);
        j++;
        i += Font.Offset.access$000(Font.Offset.tableRecordSize);
      }
      return localTreeSet;
    }
    
    private Map<Header, WritableFontData> loadTableData(SortedSet<Header> paramSortedSet, WritableFontData paramWritableFontData)
    {
      HashMap localHashMap = new HashMap(paramSortedSet.size());
      Font.logger.fine("########  Reading Table Data");
      Iterator localIterator = paramSortedSet.iterator();
      while (localIterator.hasNext())
      {
        Header localHeader = (Header)localIterator.next();
        WritableFontData localWritableFontData = paramWritableFontData.slice(localHeader.offset(), localHeader.length());
        localHashMap.put(localHeader, localWritableFontData);
      }
      return localHashMap;
    }
  }
  
  public static enum MacintoshEncodingId
  {
    Unknown(-1),  Roman(0),  Japanese(1),  ChineseTraditional(2),  Korean(3),  Arabic(4),  Hebrew(5),  Greek(6),  Russian(7),  RSymbol(8),  Devanagari(9),  Gurmukhi(10),  Gujarati(11),  Oriya(12),  Bengali(13),  Tamil(14),  Telugu(15),  Kannada(16),  Malayalam(17),  Sinhalese(18),  Burmese(19),  Khmer(20),  Thai(21),  Laotian(22),  Georgian(23),  Armenian(24),  ChineseSimplified(25),  Tibetan(26),  Mongolian(27),  Geez(28),  Slavic(29),  Vietnamese(30),  Sindhi(31),  Uninterpreted(32);
    
    private final int value;
    
    private MacintoshEncodingId(int paramInt)
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
    
    public static MacintoshEncodingId valueOf(int paramInt)
    {
      for (MacintoshEncodingId localMacintoshEncodingId : ) {
        if (localMacintoshEncodingId.equals(paramInt)) {
          return localMacintoshEncodingId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum WindowsEncodingId
  {
    Unknown(-1),  Symbol(0),  UnicodeUCS2(1),  ShiftJIS(2),  PRC(3),  Big5(4),  Wansung(5),  Johab(6),  UnicodeUCS4(10);
    
    private final int value;
    
    private WindowsEncodingId(int paramInt)
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
    
    public static WindowsEncodingId valueOf(int paramInt)
    {
      for (WindowsEncodingId localWindowsEncodingId : ) {
        if (localWindowsEncodingId.equals(paramInt)) {
          return localWindowsEncodingId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum UnicodeEncodingId
  {
    Unknown(-1),  Unicode1_0(0),  Unicode1_1(1),  ISO10646(2),  Unicode2_0_BMP(3),  Unicode2_0(4),  UnicodeVariationSequences(5);
    
    private final int value;
    
    private UnicodeEncodingId(int paramInt)
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
    
    public static UnicodeEncodingId valueOf(int paramInt)
    {
      for (UnicodeEncodingId localUnicodeEncodingId : ) {
        if (localUnicodeEncodingId.equals(paramInt)) {
          return localUnicodeEncodingId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum PlatformId
  {
    Unknown(-1),  Unicode(0),  Macintosh(1),  ISO(2),  Windows(3),  Custom(4);
    
    private final int value;
    
    private PlatformId(int paramInt)
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
    
    public static PlatformId valueOf(int paramInt)
    {
      for (PlatformId localPlatformId : ) {
        if (localPlatformId.equals(paramInt)) {
          return localPlatformId;
        }
      }
      return Unknown;
    }
  }
  
  private static enum Offset
  {
    sfntVersion(0),  numTables(4),  searchRange(6),  entrySelector(8),  rangeShift(10),  tableRecordBegin(12),  sfntHeaderSize(12),  tableTag(0),  tableCheckSum(4),  tableOffset(8),  tableLength(12),  tableRecordSize(16);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\Font.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */