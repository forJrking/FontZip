package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.PostScriptTable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PostScriptTableBuilder
{
  private static final int VERSION_2 = 131072;
  private static final int NUM_STANDARD_NAMES = 258;
  private static final int V1_TABLE_SIZE = 32;
  private static final String[] STANDARD_NAMES = { ".notdef", ".null", "nonmarkingreturn", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quotesingle", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "grave", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "Adieresis", "Aring", "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute", "agrave", "acircumflex", "adieresis", "atilde", "aring", "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis", "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute", "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave", "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling", "section", "bullet", "paragraph", "germandbls", "registered", "copyright", "trademark", "acute", "dieresis", "notequal", "AE", "Oslash", "infinity", "plusminus", "lessequal", "greaterequal", "yen", "mu", "partialdiff", "summation", "product", "pi", "integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash", "questiondown", "exclamdown", "logicalnot", "radical", "florin", "approxequal", "Delta", "guillemotleft", "guillemotright", "ellipsis", "nonbreakingspace", "Agrave", "Atilde", "Otilde", "OE", "oe", "endash", "emdash", "quotedblleft", "quotedblright", "quoteleft", "quoteright", "divide", "lozenge", "ydieresis", "Ydieresis", "fraction", "currency", "guilsinglleft", "guilsinglright", "fi", "fl", "daggerdbl", "periodcentered", "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex", "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex", "apple", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi", "circumflex", "tilde", "macron", "breve", "dotaccent", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash", "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth", "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply", "onesuperior", "twosuperior", "threesuperior", "onehalf", "onequarter", "threequarters", "franc", "Gbreve", "gbreve", "Idotaccent", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron", "ccaron", "dcroat" };
  private static final Map<String, Integer> INVERTED_STANDARD_NAMES = invertNameMap(STANDARD_NAMES);
  private final WritableFontData v1Data = WritableFontData.createWritableFontData(32);
  private List<String> names;
  
  private static Map<String, Integer> invertNameMap(String[] paramArrayOfString)
  {
    HashMap localHashMap = new HashMap();
    for (int i = 0; i < paramArrayOfString.length; i++) {
      localHashMap.put(paramArrayOfString[i], Integer.valueOf(i));
    }
    return localHashMap;
  }
  
  public void initV1From(PostScriptTable paramPostScriptTable)
  {
    paramPostScriptTable.readFontData().slice(0, 32).copyTo(this.v1Data);
  }
  
  public void setNames(List<String> paramList)
  {
    this.names = paramList;
  }
  
  public ReadableFontData build()
  {
    if (this.names == null) {
      return this.v1Data;
    }
    ArrayList localArrayList = new ArrayList();
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = this.names.size();
    int j = 258;
    Object localObject = this.names.iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str = (String)((Iterator)localObject).next();
      int m;
      if (INVERTED_STANDARD_NAMES.containsKey(str))
      {
        m = ((Integer)INVERTED_STANDARD_NAMES.get(str)).intValue();
      }
      else
      {
        m = j++;
        localByteArrayOutputStream.write(str.length());
        try
        {
          localByteArrayOutputStream.write(str.getBytes("ISO-8859-1"));
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException) {}catch (IOException localIOException)
        {
          throw new RuntimeException("Unable to write post table data", localIOException);
        }
      }
      localArrayList.add(Integer.valueOf(m));
    }
    localObject = localByteArrayOutputStream.toByteArray();
    int k = 34 + 2 * i + localObject.length;
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(k);
    this.v1Data.copyTo(localWritableFontData);
    localWritableFontData.writeFixed(Offset.version.offset, 131072);
    localWritableFontData.writeUShort(Offset.numberOfGlyphs.offset, i);
    int n = Offset.glyphNameIndex.offset;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      n += localWritableFontData.writeUShort(n, localInteger.intValue());
    }
    if (localObject.length > 0) {
      localWritableFontData.writeBytes(n, (byte[])localObject, 0, localObject.length);
    }
    return localWritableFontData;
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


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\PostScriptTableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */