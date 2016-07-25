package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.Font.PlatformId;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTableContainerTable;
import com.google.typography.font.sfntly.table.SubTableContainerTable.Builder;
import com.ibm.icu.charset.CharsetICU;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public final class NameTable
  extends SubTableContainerTable
  implements Iterable<NameEntry>
{
  private NameTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int format()
  {
    return this.data.readUShort(Offset.format.offset);
  }
  
  public int nameCount()
  {
    return this.data.readUShort(Offset.count.offset);
  }
  
  private int stringOffset()
  {
    return this.data.readUShort(Offset.stringOffset.offset);
  }
  
  private int offsetForNameRecord(int paramInt)
  {
    return Offset.nameRecordStart.offset + paramInt * Offset.nameRecordSize.offset;
  }
  
  public int platformId(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordPlatformId.offset + offsetForNameRecord(paramInt));
  }
  
  public int encodingId(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordEncodingId.offset + offsetForNameRecord(paramInt));
  }
  
  public int languageId(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordLanguageId.offset + offsetForNameRecord(paramInt));
  }
  
  public int nameId(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordNameId.offset + offsetForNameRecord(paramInt));
  }
  
  private int nameLength(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordStringLength.offset + offsetForNameRecord(paramInt));
  }
  
  private int nameOffset(int paramInt)
  {
    return this.data.readUShort(Offset.nameRecordStringOffset.offset + offsetForNameRecord(paramInt)) + stringOffset();
  }
  
  public byte[] nameAsBytes(int paramInt)
  {
    int i = nameLength(paramInt);
    byte[] arrayOfByte = new byte[i];
    this.data.readBytes(nameOffset(paramInt), arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  public byte[] nameAsBytes(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    NameEntry localNameEntry = nameEntry(paramInt1, paramInt2, paramInt3, paramInt4);
    if (localNameEntry != null) {
      return localNameEntry.nameAsBytes();
    }
    return null;
  }
  
  public String name(int paramInt)
  {
    return convertFromNameBytes(nameAsBytes(paramInt), platformId(paramInt), encodingId(paramInt));
  }
  
  public String name(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    NameEntry localNameEntry = nameEntry(paramInt1, paramInt2, paramInt3, paramInt4);
    if (localNameEntry != null) {
      return localNameEntry.name();
    }
    return null;
  }
  
  public NameEntry nameEntry(int paramInt)
  {
    return new NameEntry(platformId(paramInt), encodingId(paramInt), languageId(paramInt), nameId(paramInt), nameAsBytes(paramInt));
  }
  
  public NameEntry nameEntry(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
  {
    Iterator localIterator = iterator(new NameEntryFilter()
    {
      public boolean accept(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
      {
        return (paramAnonymousInt1 == paramInt1) && (paramAnonymousInt2 == paramInt2) && (paramAnonymousInt3 == paramInt3) && (paramAnonymousInt4 == paramInt4);
      }
    });
    if (localIterator.hasNext()) {
      return (NameEntry)localIterator.next();
    }
    return null;
  }
  
  public Set<NameEntry> names()
  {
    HashSet localHashSet = new HashSet(nameCount());
    Iterator localIterator = iterator();
    while (localIterator.hasNext())
    {
      NameEntry localNameEntry = (NameEntry)localIterator.next();
      localHashSet.add(localNameEntry);
    }
    return localHashSet;
  }
  
  public Iterator<NameEntry> iterator()
  {
    return new NameEntryIterator(null);
  }
  
  public Iterator<NameEntry> iterator(NameEntryFilter paramNameEntryFilter)
  {
    return new NameEntryIterator(paramNameEntryFilter, null);
  }
  
  private static String getEncodingName(int paramInt1, int paramInt2)
  {
    String str = null;
    switch (Font.PlatformId.valueOf(paramInt1))
    {
    case Unicode: 
      str = "UTF-16BE";
      break;
    case Macintosh: 
      switch (com.google.typography.font.sfntly.Font.MacintoshEncodingId.valueOf(paramInt2))
      {
      case Roman: 
        str = "MacRoman";
        break;
      case Japanese: 
        str = "Shift_JIS";
        break;
      case ChineseTraditional: 
        str = "Big5";
        break;
      case Korean: 
        str = "EUC-KR";
        break;
      case Arabic: 
        str = "MacArabic";
        break;
      case Hebrew: 
        str = "MacHebrew";
        break;
      case Greek: 
        str = "MacGreek";
        break;
      case Russian: 
        str = "MacCyrillic";
        break;
      case RSymbol: 
        str = "MacSymbol";
        break;
      case Devanagari: 
        break;
      case Gurmukhi: 
        break;
      case Gujarati: 
        break;
      case Oriya: 
        break;
      case Bengali: 
        break;
      case Tamil: 
        break;
      case Telugu: 
        break;
      case Kannada: 
        break;
      case Malayalam: 
        break;
      case Sinhalese: 
        break;
      case Burmese: 
        break;
      case Khmer: 
        break;
      case Thai: 
        str = "MacThai";
        break;
      case Laotian: 
        break;
      case Georgian: 
        str = "MacCyrillic";
        break;
      case Armenian: 
        break;
      case ChineseSimplified: 
        str = "EUC-CN";
        break;
      case Tibetan: 
        break;
      case Mongolian: 
        str = "MacCyrillic";
        break;
      case Geez: 
        break;
      case Slavic: 
        str = "MacCyrillic";
        break;
      case Vietnamese: 
        break;
      case Sindhi: 
        
      }
      break;
    case ISO: 
      break;
    case Windows: 
      switch (com.google.typography.font.sfntly.Font.WindowsEncodingId.valueOf(paramInt2))
      {
      case Symbol: 
        str = "UTF-16BE";
        break;
      case UnicodeUCS2: 
        str = "UTF-16BE";
        break;
      case ShiftJIS: 
        str = "windows-933";
        break;
      case PRC: 
        str = "windows-936";
        break;
      case Big5: 
        str = "windows-950";
        break;
      case Wansung: 
        str = "windows-949";
        break;
      case Johab: 
        str = "ms1361";
        break;
      case UnicodeUCS4: 
        str = "UCS-4";
      }
      break;
    case Custom: 
      break;
    }
    return str;
  }
  
  private static Charset getCharset(int paramInt1, int paramInt2)
  {
    String str = getEncodingName(paramInt1, paramInt2);
    if (str == null) {
      return null;
    }
    Charset localCharset = null;
    try
    {
      localCharset = CharsetICU.forNameICU(str);
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
      return null;
    }
    return localCharset;
  }
  
  private static byte[] convertToNameBytes(String paramString, int paramInt1, int paramInt2)
  {
    Charset localCharset = getCharset(paramInt1, paramInt2);
    if (localCharset == null) {
      return null;
    }
    ByteBuffer localByteBuffer = localCharset.encode(paramString);
    return localByteBuffer.array();
  }
  
  private static String convertFromNameBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return convertFromNameBytes(ByteBuffer.wrap(paramArrayOfByte), paramInt1, paramInt2);
  }
  
  private static String convertFromNameBytes(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    Charset localCharset = getCharset(paramInt1, paramInt2);
    if (localCharset == null) {
      return Integer.toHexString(paramInt1);
    }
    CharBuffer localCharBuffer = localCharset.decode(paramByteBuffer);
    return localCharBuffer.toString();
  }
  
  public static class Builder
    extends SubTableContainerTable.Builder<NameTable>
  {
    private Map<NameTable.NameEntryId, NameTable.NameEntryBuilder> nameEntryMap;
    
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
    
    private void initialize(ReadableFontData paramReadableFontData)
    {
      this.nameEntryMap = new TreeMap();
      if (paramReadableFontData != null)
      {
        NameTable localNameTable = new NameTable(header(), paramReadableFontData, null);
        Iterator localIterator = localNameTable.iterator();
        while (localIterator.hasNext())
        {
          NameTable.NameEntry localNameEntry = (NameTable.NameEntry)localIterator.next();
          NameTable.NameEntryBuilder localNameEntryBuilder = new NameTable.NameEntryBuilder(localNameEntry);
          this.nameEntryMap.put(localNameEntryBuilder.getNameEntryId(), localNameEntryBuilder);
        }
      }
    }
    
    private Map<NameTable.NameEntryId, NameTable.NameEntryBuilder> getNameBuilders()
    {
      if (this.nameEntryMap == null) {
        initialize(super.internalReadData());
      }
      super.setModelChanged();
      return this.nameEntryMap;
    }
    
    public void revertNames()
    {
      this.nameEntryMap = null;
      setModelChanged(false);
    }
    
    public int builderCount()
    {
      return getNameBuilders().size();
    }
    
    public void clear()
    {
      getNameBuilders().clear();
    }
    
    public boolean has(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      NameTable.NameEntryId localNameEntryId = new NameTable.NameEntryId(paramInt1, paramInt2, paramInt3, paramInt4);
      return getNameBuilders().containsKey(localNameEntryId);
    }
    
    public NameTable.NameEntryBuilder nameBuilder(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      NameTable.NameEntryId localNameEntryId = new NameTable.NameEntryId(paramInt1, paramInt2, paramInt3, paramInt4);
      NameTable.NameEntryBuilder localNameEntryBuilder = (NameTable.NameEntryBuilder)getNameBuilders().get(localNameEntryId);
      if (localNameEntryBuilder == null)
      {
        localNameEntryBuilder = new NameTable.NameEntryBuilder(localNameEntryId);
        getNameBuilders().put(localNameEntryId, localNameEntryBuilder);
      }
      return localNameEntryBuilder;
    }
    
    public boolean remove(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      NameTable.NameEntryId localNameEntryId = new NameTable.NameEntryId(paramInt1, paramInt2, paramInt3, paramInt4);
      return getNameBuilders().remove(localNameEntryId) != null;
    }
    
    protected NameTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new NameTable(header(), paramReadableFontData, null);
    }
    
    protected void subDataSet()
    {
      this.nameEntryMap = null;
      super.setModelChanged(false);
    }
    
    protected int subDataSizeToSerialize()
    {
      if ((this.nameEntryMap == null) || (this.nameEntryMap.size() == 0)) {
        return 0;
      }
      int i = NameTable.Offset.access$000(NameTable.Offset.nameRecordStart) + this.nameEntryMap.size() * NameTable.Offset.access$000(NameTable.Offset.nameRecordSize);
      Iterator localIterator = this.nameEntryMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        i += ((NameTable.NameEntryBuilder)localEntry.getValue()).nameAsBytes().length;
      }
      return i;
    }
    
    protected boolean subReadyToSerialize()
    {
      return (this.nameEntryMap != null) && (this.nameEntryMap.size() != 0);
    }
    
    protected int subSerialize(WritableFontData paramWritableFontData)
    {
      int i = NameTable.Offset.access$000(NameTable.Offset.nameRecordStart) + this.nameEntryMap.size() * NameTable.Offset.access$000(NameTable.Offset.nameRecordSize);
      paramWritableFontData.writeUShort(NameTable.Offset.access$000(NameTable.Offset.format), 0);
      paramWritableFontData.writeUShort(NameTable.Offset.access$000(NameTable.Offset.count), this.nameEntryMap.size());
      paramWritableFontData.writeUShort(NameTable.Offset.access$000(NameTable.Offset.stringOffset), i);
      int j = NameTable.Offset.access$000(NameTable.Offset.nameRecordStart);
      int k = 0;
      Iterator localIterator = this.nameEntryMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordPlatformId), ((NameTable.NameEntryId)localEntry.getKey()).getPlatformId());
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordEncodingId), ((NameTable.NameEntryId)localEntry.getKey()).getEncodingId());
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordLanguageId), ((NameTable.NameEntryId)localEntry.getKey()).getLanguageId());
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordNameId), ((NameTable.NameEntryId)localEntry.getKey()).getNameId());
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordStringLength), ((NameTable.NameEntryBuilder)localEntry.getValue()).nameAsBytes().length);
        paramWritableFontData.writeUShort(j + NameTable.Offset.access$000(NameTable.Offset.nameRecordStringOffset), k);
        j += NameTable.Offset.access$000(NameTable.Offset.nameRecordSize);
        byte[] arrayOfByte = ((NameTable.NameEntryBuilder)localEntry.getValue()).nameAsBytes();
        if (arrayOfByte.length > 0) {
          k += paramWritableFontData.writeBytes(k + i, ((NameTable.NameEntryBuilder)localEntry.getValue()).nameAsBytes());
        }
      }
      return k + i;
    }
  }
  
  protected class NameEntryIterator
    implements Iterator<NameTable.NameEntry>
  {
    private int nameIndex = 0;
    private NameTable.NameEntryFilter filter = null;
    
    private NameEntryIterator() {}
    
    private NameEntryIterator(NameTable.NameEntryFilter paramNameEntryFilter)
    {
      this.filter = paramNameEntryFilter;
    }
    
    public boolean hasNext()
    {
      if (this.filter == null) {
        return this.nameIndex < NameTable.this.nameCount();
      }
      while (this.nameIndex < NameTable.this.nameCount())
      {
        if (this.filter.accept(NameTable.this.platformId(this.nameIndex), NameTable.this.encodingId(this.nameIndex), NameTable.this.languageId(this.nameIndex), NameTable.this.nameId(this.nameIndex))) {
          return true;
        }
        this.nameIndex += 1;
      }
      return false;
    }
    
    public NameTable.NameEntry next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return NameTable.this.nameEntry(this.nameIndex++);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Cannot remove a CMap table from an existing font.");
    }
  }
  
  public static abstract interface NameEntryFilter
  {
    public abstract boolean accept(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
  
  public static class NameEntryBuilder
    extends NameTable.NameEntry
  {
    protected NameEntryBuilder() {}
    
    protected NameEntryBuilder(NameTable.NameEntryId paramNameEntryId, byte[] paramArrayOfByte)
    {
      super(paramArrayOfByte);
    }
    
    protected NameEntryBuilder(NameTable.NameEntryId paramNameEntryId)
    {
      this(paramNameEntryId, null);
    }
    
    protected NameEntryBuilder(NameTable.NameEntry paramNameEntry)
    {
      this(paramNameEntry.getNameEntryId(), paramNameEntry.nameAsBytes());
    }
    
    public void setName(String paramString)
    {
      if (paramString == null)
      {
        this.nameBytes = new byte[0];
        return;
      }
      this.nameBytes = NameTable.convertToNameBytes(paramString, this.nameEntryId.getPlatformId(), this.nameEntryId.getEncodingId());
    }
    
    public void setName(byte[] paramArrayOfByte)
    {
      this.nameBytes = Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
    }
    
    public void setName(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      this.nameBytes = Arrays.copyOfRange(paramArrayOfByte, paramInt1, paramInt1 + paramInt2);
    }
  }
  
  public static class NameEntry
  {
    NameTable.NameEntryId nameEntryId;
    protected int length;
    protected byte[] nameBytes;
    
    protected NameEntry() {}
    
    protected NameEntry(NameTable.NameEntryId paramNameEntryId, byte[] paramArrayOfByte)
    {
      this.nameEntryId = paramNameEntryId;
      this.nameBytes = paramArrayOfByte;
    }
    
    protected NameEntry(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    {
      this(new NameTable.NameEntryId(paramInt1, paramInt2, paramInt3, paramInt4), paramArrayOfByte);
    }
    
    protected NameTable.NameEntryId getNameEntryId()
    {
      return this.nameEntryId;
    }
    
    public int platformId()
    {
      return this.nameEntryId.getPlatformId();
    }
    
    public int encodingId()
    {
      return this.nameEntryId.getEncodingId();
    }
    
    public int languageId()
    {
      return this.nameEntryId.getLanguageId();
    }
    
    public int nameId()
    {
      return this.nameEntryId.getNameId();
    }
    
    public byte[] nameAsBytes()
    {
      return this.nameBytes;
    }
    
    public String name()
    {
      return NameTable.convertFromNameBytes(this.nameBytes, platformId(), encodingId());
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[");
      localStringBuilder.append(this.nameEntryId);
      localStringBuilder.append(", \"");
      String str = name();
      localStringBuilder.append(name());
      localStringBuilder.append("\"]");
      return localStringBuilder.toString();
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof NameEntry)) {
        return false;
      }
      NameEntry localNameEntry = (NameEntry)paramObject;
      if (!this.nameEntryId.equals(localNameEntry.nameEntryId)) {
        return false;
      }
      if (this.nameBytes.length != localNameEntry.nameBytes.length) {
        return false;
      }
      for (int i = 0; i < this.nameBytes.length; i++) {
        if (this.nameBytes[i] != localNameEntry.nameBytes[i]) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int i = this.nameEntryId.hashCode();
      for (int j = 0; j < this.nameBytes.length; j += 4) {
        for (int k = 0; (k < 4) && (k + j < this.nameBytes.length); k++) {
          i |= this.nameBytes[k] << k * 8;
        }
      }
      return i;
    }
  }
  
  private static class NameEntryId
    implements Comparable<NameEntryId>
  {
    protected int platformId;
    protected int encodingId;
    protected int languageId;
    protected int nameId;
    
    protected NameEntryId(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.platformId = paramInt1;
      this.encodingId = paramInt2;
      this.languageId = paramInt3;
      this.nameId = paramInt4;
    }
    
    protected int getPlatformId()
    {
      return this.platformId;
    }
    
    protected int getEncodingId()
    {
      return this.encodingId;
    }
    
    protected int getLanguageId()
    {
      return this.languageId;
    }
    
    protected int getNameId()
    {
      return this.nameId;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof NameEntryId)) {
        return false;
      }
      NameEntryId localNameEntryId = (NameEntryId)paramObject;
      return (this.encodingId == localNameEntryId.encodingId) && (this.languageId == localNameEntryId.languageId) && (this.platformId == localNameEntryId.platformId) && (this.nameId == localNameEntryId.nameId);
    }
    
    public int hashCode()
    {
      return (this.encodingId & 0x3F) << 26 | (this.nameId & 0x3F) << 16 | (this.platformId & 0xF) << 12 | this.languageId & 0xFF;
    }
    
    public int compareTo(NameEntryId paramNameEntryId)
    {
      if (this.platformId != paramNameEntryId.platformId) {
        return this.platformId - paramNameEntryId.platformId;
      }
      if (this.encodingId != paramNameEntryId.encodingId) {
        return this.encodingId - paramNameEntryId.encodingId;
      }
      if (this.languageId != paramNameEntryId.languageId) {
        return this.languageId - paramNameEntryId.languageId;
      }
      return this.nameId - paramNameEntryId.nameId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("P=");
      localStringBuilder.append(Font.PlatformId.valueOf(this.platformId));
      localStringBuilder.append(", E=0x");
      localStringBuilder.append(Integer.toHexString(this.encodingId));
      localStringBuilder.append(", L=0x");
      localStringBuilder.append(Integer.toHexString(this.languageId));
      localStringBuilder.append(", N=");
      NameTable.NameId localNameId = NameTable.NameId.valueOf(this.nameId);
      if (localNameId != null)
      {
        localStringBuilder.append(NameTable.NameId.valueOf(this.nameId));
      }
      else
      {
        localStringBuilder.append("0x");
        localStringBuilder.append(Integer.toHexString(this.nameId));
      }
      return localStringBuilder.toString();
    }
  }
  
  public static enum WindowsLanguageId
  {
    Unknown(-1),  Afrikaans_SouthAfrica(1078),  Albanian_Albania(1052),  Alsatian_France(1156),  Amharic_Ethiopia(1118),  Arabic_Algeria(5121),  Arabic_Bahrain(15361),  Arabic_Egypt(3073),  Arabic_Iraq(2049),  Arabic_Jordan(11265),  Arabic_Kuwait(13313),  Arabic_Lebanon(12289),  Arabic_Libya(4097),  Arabic_Morocco(6145),  Arabic_Oman(8193),  Arabic_Qatar(16385),  Arabic_SaudiArabia(1025),  Arabic_Syria(10241),  Arabic_Tunisia(7169),  Arabic_UAE(14337),  Arabic_Yemen(9217),  Armenian_Armenia(1067),  Assamese_India(1101),  Azeri_Cyrillic_Azerbaijan(2092),  Azeri_Latin_Azerbaijan(1068),  Bashkir_Russia(1133),  Basque_Basque(1069),  Belarusian_Belarus(1059),  Bengali_Bangladesh(2117),  Bengali_India(1093),  Bosnian_Cyrillic_BosniaAndHerzegovina(8218),  Bosnian_Latin_BosniaAndHerzegovina(5146),  Breton_France(1150),  Bulgarian_Bulgaria(1026),  Catalan_Catalan(1027),  Chinese_HongKongSAR(3076),  Chinese_MacaoSAR(5124),  Chinese_PeoplesRepublicOfChina(2052),  Chinese_Singapore(4100),  Chinese_Taiwan(1028),  Corsican_France(1155),  Croatian_Croatia(1050),  Croatian_Latin_BosniaAndHerzegovina(4122),  Czech_CzechRepublic(1029),  Danish_Denmark(1030),  Dari_Afghanistan(1164),  Divehi_Maldives(1125),  Dutch_Belgium(2067),  Dutch_Netherlands(1043),  English_Australia(3081),  English_Belize(10249),  English_Canada(4105),  English_Caribbean(9225),  English_India(16393),  English_Ireland(6153),  English_Jamaica(8201),  English_Malaysia(17417),  English_NewZealand(5129),  English_RepublicOfThePhilippines(13321),  English_Singapore(18441),  English_SouthAfrica(7177),  English_TrinidadAndTobago(11273),  English_UnitedKingdom(2057),  English_UnitedStates(1033),  English_Zimbabwe(12297),  Estonian_Estonia(1061),  Faroese_FaroeIslands(1080),  Filipino_Philippines(1124),  Finnish_Finland(1035),  French_Belgium(2060),  French_Canada(3084),  French_France(1036),  French_Luxembourg(5132),  French_PrincipalityOfMonoco(6156),  French_Switzerland(4108),  Frisian_Netherlands(1122),  Galician_Galician(1110),  Georgian_Georgia(1079),  German_Austria(3079),  German_Germany(1031),  German_Liechtenstein(5127),  German_Luxembourg(4103),  German_Switzerland(2055),  Greek_Greece(1032),  Greenlandic_Greenland(1135),  Gujarati_India(1095),  Hausa_Latin_Nigeria(1128),  Hebrew_Israel(1037),  Hindi_India(1081),  Hungarian_Hungary(1038),  Icelandic_Iceland(1039),  Igbo_Nigeria(1136),  Indonesian_Indonesia(1057),  Inuktitut_Canada(1117),  Inuktitut_Latin_Canada(2141),  Irish_Ireland(2108),  isiXhosa_SouthAfrica(1076),  isiZulu_SouthAfrica(1077),  Italian_Italy(1040),  Italian_Switzerland(2064),  Japanese_Japan(1041),  Kannada_India(1099),  Kazakh_Kazakhstan(1087),  Khmer_Cambodia(1107),  Kiche_Guatemala(1158),  Kinyarwanda_Rwanda(1159),  Kiswahili_Kenya(1089),  Konkani_India(1111),  Korean_Korea(1042),  Kyrgyz_Kyrgyzstan(1088),  Lao_LaoPDR(1108),  Latvian_Latvia(1062),  Lithuanian_Lithuania(1063),  LowerSorbian_Germany(2094),  Luxembourgish_Luxembourg(1134),  Macedonian_FYROM_FormerYugoslavRepublicOfMacedonia(1071),  Malay_BruneiDarussalam(2110),  Malay_Malaysia(1086),  Malayalam_India(1100),  Maltese_Malta(1082),  Maori_NewZealand(1153),  Mapudungun_Chile(1146),  Marathi_India(1102),  Mohawk_Mohawk(1148),  Mongolian_Cyrillic_Mongolia(1104),  Mongolian_Traditional_PeoplesRepublicOfChina(2128),  Nepali_Nepal(1121),  Norwegian_Bokmal_Norway(1044),  Norwegian_Nynorsk_Norway(2068),  Occitan_France(1154),  Oriya_India(1096),  Pashto_Afghanistan(1123),  Polish_Poland(1045),  Portuguese_Brazil(1046),  Portuguese_Portugal(2070),  Punjabi_India(1094),  Quechua_Bolivia(1131),  Quechua_Ecuador(2155),  Quechua_Peru(3179),  Romanian_Romania(1048),  Romansh_Switzerland(1047),  Russian_Russia(1049),  Sami_Inari_Finland(9275),  Sami_Lule_Norway(4155),  Sami_Lule_Sweden(5179),  Sami_Northern_Finland(3131),  Sami_Northern_Norway(1083),  Sami_Northern_Sweden(2107),  Sami_Skolt_Finland(8251),  Sami_Southern_Norway(6203),  Sami_Southern_Sweden(7227),  Sanskrit_India(1103),  Serbian_Cyrillic_BosniaAndHerzegovina(7194),  Serbian_Cyrillic_Serbia(3098),  Serbian_Latin_BosniaAndHerzegovina(6170),  Serbian_Latin_Serbia(2074),  SesothoSaLeboa_SouthAfrica(1132),  Setswana_SouthAfrica(1074),  Sinhala_SriLanka(1115),  Slovak_Slovakia(1051),  Slovenian_Slovenia(1060),  Spanish_Argentina(11274),  Spanish_Bolivia(16394),  Spanish_Chile(13322),  Spanish_Colombia(9226),  Spanish_CostaRica(5130),  Spanish_DominicanRepublic(7178),  Spanish_Ecuador(12298),  Spanish_ElSalvador(17418),  Spanish_Guatemala(4106),  Spanish_Honduras(18442),  Spanish_Mexico(2058),  Spanish_Nicaragua(19466),  Spanish_Panama(6154),  Spanish_Paraguay(15370),  Spanish_Peru(10250),  Spanish_PuertoRico(20490),  Spanish_ModernSort_Spain(3082),  Spanish_TraditionalSort_Spain(1034),  Spanish_UnitedStates(21514),  Spanish_Uruguay(14346),  Spanish_Venezuela(8202),  Sweden_Finland(2077),  Swedish_Sweden(1053),  Syriac_Syria(1114),  Tajik_Cyrillic_Tajikistan(1064),  Tamazight_Latin_Algeria(2143),  Tamil_India(1097),  Tatar_Russia(1092),  Telugu_India(1098),  Thai_Thailand(1054),  Tibetan_PRC(1105),  Turkish_Turkey(1055),  Turkmen_Turkmenistan(1090),  Uighur_PRC(1152),  Ukrainian_Ukraine(1058),  UpperSorbian_Germany(1070),  Urdu_IslamicRepublicOfPakistan(1056),  Uzbek_Cyrillic_Uzbekistan(2115),  Uzbek_Latin_Uzbekistan(1091),  Vietnamese_Vietnam(1066),  Welsh_UnitedKingdom(1106),  Wolof_Senegal(1096),  Yakut_Russia(1157),  Yi_PRC(1144),  Yoruba_Nigeria(1130);
    
    private final int value;
    
    private WindowsLanguageId(int paramInt)
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
    
    public static WindowsLanguageId valueOf(int paramInt)
    {
      for (WindowsLanguageId localWindowsLanguageId : ) {
        if (localWindowsLanguageId.equals(paramInt)) {
          return localWindowsLanguageId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum MacintoshLanguageId
  {
    Unknown(-1),  English(0),  French(1),  German(2),  Italian(3),  Dutch(4),  Swedish(5),  Spanish(6),  Danish(7),  Portuguese(8),  Norwegian(9),  Hebrew(10),  Japanese(11),  Arabic(12),  Finnish(13),  Greek(14),  Icelandic(15),  Maltese(16),  Turkish(17),  Croatian(18),  Chinese_Traditional(19),  Urdu(20),  Hindi(21),  Thai(22),  Korean(23),  Lithuanian(24),  Polish(25),  Hungarian(26),  Estonian(27),  Latvian(28),  Sami(29),  Faroese(30),  FarsiPersian(31),  Russian(32),  Chinese_Simplified(33),  Flemish(34),  IrishGaelic(35),  Albanian(36),  Romanian(37),  Czech(38),  Slovak(39),  Slovenian(40),  Yiddish(41),  Serbian(42),  Macedonian(43),  Bulgarian(44),  Ukrainian(45),  Byelorussian(46),  Uzbek(47),  Kazakh(48),  Azerbaijani_Cyrillic(49),  Azerbaijani_Arabic(50),  Armenian(51),  Georgian(52),  Moldavian(53),  Kirghiz(54),  Tajiki(55),  Turkmen(56),  Mongolian_Mongolian(57),  Mongolian_Cyrillic(58),  Pashto(59),  Kurdish(60),  Kashmiri(61),  Sindhi(62),  Tibetan(63),  Nepali(64),  Sanskrit(65),  Marathi(66),  Bengali(67),  Assamese(68),  Gujarati(69),  Punjabi(70),  Oriya(71),  Malayalam(72),  Kannada(73),  Tamil(74),  Telugu(75),  Sinhalese(76),  Burmese(77),  Khmer(78),  Lao(79),  Vietnamese(80),  Indonesian(81),  Tagalong(82),  Malay_Roman(83),  Malay_Arabic(84),  Amharic(85),  Tigrinya(86),  Galla(87),  Somali(88),  Swahili(89),  KinyarwandaRuanda(90),  Rundi(91),  NyanjaChewa(92),  Malagasy(93),  Esperanto(94),  Welsh(128),  Basque(129),  Catalan(130),  Latin(131),  Quenchua(132),  Guarani(133),  Aymara(134),  Tatar(135),  Uighur(136),  Dzongkha(137),  Javanese_Roman(138),  Sundanese_Roman(139),  Galician(140),  Afrikaans(141),  Breton(142),  Inuktitut(143),  ScottishGaelic(144),  ManxGaelic(145),  IrishGaelic_WithDotAbove(146),  Tongan(147),  Greek_Polytonic(148),  Greenlandic(149),  Azerbaijani_Roman(150);
    
    private final int value;
    
    private MacintoshLanguageId(int paramInt)
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
    
    public static MacintoshLanguageId valueOf(int paramInt)
    {
      for (MacintoshLanguageId localMacintoshLanguageId : ) {
        if (localMacintoshLanguageId.equals(paramInt)) {
          return localMacintoshLanguageId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum UnicodeLanguageId
  {
    Unknown(-1),  All(0);
    
    private final int value;
    
    private UnicodeLanguageId(int paramInt)
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
    
    public static UnicodeLanguageId valueOf(int paramInt)
    {
      for (UnicodeLanguageId localUnicodeLanguageId : ) {
        if (localUnicodeLanguageId.equals(paramInt)) {
          return localUnicodeLanguageId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum NameId
  {
    Unknown(-1),  CopyrightNotice(0),  FontFamilyName(1),  FontSubfamilyName(2),  UniqueFontIdentifier(3),  FullFontName(4),  VersionString(5),  PostscriptName(6),  Trademark(7),  ManufacturerName(8),  Designer(9),  Description(10),  VendorURL(11),  DesignerURL(12),  LicenseDescription(13),  LicenseInfoURL(14),  Reserved15(15),  PreferredFamily(16),  PreferredSubfamily(17),  CompatibleFullName(18),  SampleText(19),  PostscriptCID(20),  WWSFamilyName(21),  WWSSubfamilyName(22);
    
    private final int value;
    
    private NameId(int paramInt)
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
    
    public static NameId valueOf(int paramInt)
    {
      for (NameId localNameId : ) {
        if (localNameId.equals(paramInt)) {
          return localNameId;
        }
      }
      return Unknown;
    }
  }
  
  public static enum Offset
  {
    format(0),  count(2),  stringOffset(4),  nameRecordStart(6),  langTagCount(0),  langTagRecord(2),  nameRecordSize(12),  nameRecordPlatformId(0),  nameRecordEncodingId(2),  nameRecordLanguageId(4),  nameRecordNameId(6),  nameRecordStringLength(8),  nameRecordStringOffset(10);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\NameTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */