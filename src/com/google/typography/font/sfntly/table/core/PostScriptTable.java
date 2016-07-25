package com.google.typography.font.sfntly.table.core;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.TableBasedTableBuilder;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class PostScriptTable
  extends Table
{
  private static final int VERSION_1 = 65536;
  private static final int VERSION_2 = 131072;
  private static final int NUM_STANDARD_NAMES = 258;
  private AtomicReference<List<String>> names = new AtomicReference();
  private static final String[] STANDARD_NAMES = { ".notdef", ".null", "nonmarkingreturn", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quotesingle", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "grave", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "Adieresis", "Aring", "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute", "agrave", "acircumflex", "adieresis", "atilde", "aring", "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis", "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute", "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave", "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling", "section", "bullet", "paragraph", "germandbls", "registered", "copyright", "trademark", "acute", "dieresis", "notequal", "AE", "Oslash", "infinity", "plusminus", "lessequal", "greaterequal", "yen", "mu", "partialdiff", "summation", "product", "pi", "integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash", "questiondown", "exclamdown", "logicalnot", "radical", "florin", "approxequal", "Delta", "guillemotleft", "guillemotright", "ellipsis", "nonbreakingspace", "Agrave", "Atilde", "Otilde", "OE", "oe", "endash", "emdash", "quotedblleft", "quotedblright", "quoteleft", "quoteright", "divide", "lozenge", "ydieresis", "Ydieresis", "fraction", "currency", "guilsinglleft", "guilsinglright", "fi", "fl", "daggerdbl", "periodcentered", "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex", "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex", "apple", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi", "circumflex", "tilde", "macron", "breve", "dotaccent", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash", "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth", "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply", "onesuperior", "twosuperior", "threesuperior", "onehalf", "onequarter", "threequarters", "franc", "Gbreve", "gbreve", "Idotaccent", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron", "ccaron", "dcroat" };
  
  private PostScriptTable(Header paramHeader, ReadableFontData paramReadableFontData)
  {
    super(paramHeader, paramReadableFontData);
  }
  
  public int version()
  {
    return this.data.readFixed(Offset.version.offset);
  }
  
  public int italicAngle()
  {
    return this.data.readFixed(Offset.italicAngle.offset);
  }
  
  public int underlinePosition()
  {
    return this.data.readFWord(Offset.underlinePosition.offset);
  }
  
  public long isFixedPitchRaw()
  {
    return this.data.readULong(Offset.isFixedPitch.offset);
  }
  
  public boolean isFixedPitch()
  {
    return isFixedPitchRaw() != 0L;
  }
  
  public long minMemType42()
  {
    return this.data.readULong(Offset.minMemType42.offset);
  }
  
  public long maxMemType42()
  {
    return this.data.readULong(Offset.maxMemType42.offset);
  }
  
  public long minMemType1()
  {
    return this.data.readULong(Offset.minMemType1.offset);
  }
  
  public long maxMemType1()
  {
    return this.data.readULong(Offset.maxMemType1.offset);
  }
  
  public int numberOfGlyphs()
  {
    if (version() == 65536) {
      return 258;
    }
    if (version() == 131072) {
      return this.data.readUShort(Offset.numberOfGlyphs.offset);
    }
    return -1;
  }
  
  public String glyphName(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= numberOfGlyphs())) {
      throw new IndexOutOfBoundsException();
    }
    int i = 0;
    if (version() == 65536) {
      i = paramInt;
    } else if (version() == 131072) {
      i = this.data.readUShort(Offset.glyphNameIndex.offset + 2 * paramInt);
    }
    if (i < 258) {
      return STANDARD_NAMES[i];
    }
    return (String)getNames().get(i - 258);
  }
  
  private List<String> getNames()
  {
    List localList = (List)this.names.get();
    if ((localList == null) && (version() == 131072)) {
      synchronized (this.names)
      {
        localList = (List)this.names.get();
        if (localList == null)
        {
          localList = parse();
          this.names.compareAndSet(null, localList);
        }
      }
    }
    return localList;
  }
  
  private List<String> parse()
  {
    ArrayList localArrayList = null;
    if (version() == 131072)
    {
      localArrayList = new ArrayList();
      int i = Offset.glyphNameIndex.offset + 2 * numberOfGlyphs();
      while (i < dataLength())
      {
        int j = this.data.readUByte(i);
        byte[] arrayOfByte = new byte[j];
        this.data.readBytes(i + 1, arrayOfByte, 0, j);
        try
        {
          localArrayList.add(new String(arrayOfByte, "ISO-8859-1"));
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
        i += 1 + j;
      }
    }
    else if (version() == 65536)
    {
      throw new IllegalStateException("Not meaningful to parse version 1 table");
    }
    return localArrayList;
  }
  
  public static class Builder
    extends TableBasedTableBuilder<PostScriptTable>
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
    
    protected PostScriptTable subBuildTable(ReadableFontData paramReadableFontData)
    {
      return new PostScriptTable(header(), paramReadableFontData, null);
    }
  }
  
  private static enum Offset
  {
    version(0),  italicAngle(4),  underlinePosition(8),  underlineThickness(10),  isFixedPitch(12),  minMemType42(16),  maxMemType42(20),  minMemType1(24),  maxMemType1(28),  numberOfGlyphs(32),  glyphNameIndex(34);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\core\PostScriptTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */