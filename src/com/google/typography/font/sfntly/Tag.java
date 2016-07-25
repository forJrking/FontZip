package com.google.typography.font.sfntly;

import java.io.UnsupportedEncodingException;

public final class Tag
{
  public static final int ttcf = intValue(new byte[] { 116, 116, 99, 102 });
  public static final int cmap = intValue(new byte[] { 99, 109, 97, 112 });
  public static final int head = intValue(new byte[] { 104, 101, 97, 100 });
  public static final int hhea = intValue(new byte[] { 104, 104, 101, 97 });
  public static final int hmtx = intValue(new byte[] { 104, 109, 116, 120 });
  public static final int maxp = intValue(new byte[] { 109, 97, 120, 112 });
  public static final int name = intValue(new byte[] { 110, 97, 109, 101 });
  public static final int OS_2 = intValue(new byte[] { 79, 83, 47, 50 });
  public static final int post = intValue(new byte[] { 112, 111, 115, 116 });
  public static final int cvt = intValue(new byte[] { 99, 118, 116, 32 });
  public static final int fpgm = intValue(new byte[] { 102, 112, 103, 109 });
  public static final int glyf = intValue(new byte[] { 103, 108, 121, 102 });
  public static final int loca = intValue(new byte[] { 108, 111, 99, 97 });
  public static final int prep = intValue(new byte[] { 112, 114, 101, 112 });
  public static final int CFF = intValue(new byte[] { 67, 70, 70, 32 });
  public static final int VORG = intValue(new byte[] { 86, 79, 82, 71 });
  public static final int EBDT = intValue(new byte[] { 69, 66, 68, 84 });
  public static final int EBLC = intValue(new byte[] { 69, 66, 76, 67 });
  public static final int EBSC = intValue(new byte[] { 69, 66, 83, 67 });
  public static final int BASE = intValue(new byte[] { 66, 65, 83, 69 });
  public static final int GDEF = intValue(new byte[] { 71, 68, 69, 70 });
  public static final int GPOS = intValue(new byte[] { 71, 80, 79, 83 });
  public static final int GSUB = intValue(new byte[] { 71, 83, 85, 66 });
  public static final int JSTF = intValue(new byte[] { 74, 83, 84, 70 });
  public static final int DSIG = intValue(new byte[] { 68, 83, 73, 71 });
  public static final int gasp = intValue(new byte[] { 103, 97, 115, 112 });
  public static final int hdmx = intValue(new byte[] { 104, 100, 109, 120 });
  public static final int kern = intValue(new byte[] { 107, 101, 114, 110 });
  public static final int LTSH = intValue(new byte[] { 76, 84, 83, 72 });
  public static final int PCLT = intValue(new byte[] { 80, 67, 76, 84 });
  public static final int VDMX = intValue(new byte[] { 86, 68, 77, 88 });
  public static final int vhea = intValue(new byte[] { 118, 104, 101, 97 });
  public static final int vmtx = intValue(new byte[] { 118, 109, 116, 120 });
  public static final int bsln = intValue(new byte[] { 98, 115, 108, 110 });
  public static final int feat = intValue(new byte[] { 102, 101, 97, 116 });
  public static final int lcar = intValue(new byte[] { 108, 99, 97, 114 });
  public static final int morx = intValue(new byte[] { 109, 111, 114, 120 });
  public static final int opbd = intValue(new byte[] { 111, 112, 98, 100 });
  public static final int prop = intValue(new byte[] { 112, 114, 111, 112 });
  public static final int Feat = intValue(new byte[] { 70, 101, 97, 116 });
  public static final int Glat = intValue(new byte[] { 71, 108, 97, 116 });
  public static final int Gloc = intValue(new byte[] { 71, 108, 111, 99 });
  public static final int Sile = intValue(new byte[] { 83, 105, 108, 101 });
  public static final int Silf = intValue(new byte[] { 83, 105, 108, 102 });
  public static final int bhed = intValue(new byte[] { 98, 104, 101, 100 });
  public static final int bdat = intValue(new byte[] { 98, 100, 97, 116 });
  public static final int bloc = intValue(new byte[] { 98, 108, 111, 99 });
  
  public static int intValue(byte[] paramArrayOfByte)
  {
    return paramArrayOfByte[0] << 24 | paramArrayOfByte[1] << 16 | paramArrayOfByte[2] << 8 | paramArrayOfByte[3];
  }
  
  public static byte[] byteValue(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(0xFF & paramInt >> 24));
    arrayOfByte[1] = ((byte)(0xFF & paramInt >> 16));
    arrayOfByte[2] = ((byte)(0xFF & paramInt >> 8));
    arrayOfByte[3] = ((byte)(0xFF & paramInt));
    return arrayOfByte;
  }
  
  public static String stringValue(int paramInt)
  {
    String str;
    try
    {
      str = new String(byteValue(paramInt), "US-ASCII");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      return "";
    }
    return str;
  }
  
  public static int intValue(String paramString)
  {
    byte[] arrayOfByte = null;
    try
    {
      arrayOfByte = paramString.substring(0, 4).getBytes("US-ASCII");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      return 0;
    }
    return intValue(arrayOfByte);
  }
  
  public static boolean isHeaderTable(int paramInt)
  {
    return (paramInt == head) || (paramInt == bhed);
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\Tag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */