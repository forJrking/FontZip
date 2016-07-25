package com.google.typography.font.sfntly;

import com.google.typography.font.sfntly.data.FontData.DataSize;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class FontFactory
{
  private static final int LOOKAHEAD_SIZE = 4;
  private boolean fingerprint = false;
  List<Integer> tableOrdering;
  
  public static FontFactory getInstance()
  {
    return new FontFactory();
  }
  
  public void fingerprintFont(boolean paramBoolean)
  {
    this.fingerprint = paramBoolean;
  }
  
  public boolean fingerprintFont()
  {
    return this.fingerprint;
  }
  
  public Font[] loadFonts(InputStream paramInputStream)
    throws IOException
  {
    PushbackInputStream localPushbackInputStream = new PushbackInputStream(new BufferedInputStream(paramInputStream), 4);
    if (isCollection(localPushbackInputStream)) {
      return loadCollection(localPushbackInputStream);
    }
    return new Font[] { loadSingleOTF(localPushbackInputStream) };
  }
  
  public Font.Builder[] loadFontsForBuilding(InputStream paramInputStream)
    throws IOException
  {
    PushbackInputStream localPushbackInputStream = new PushbackInputStream(new BufferedInputStream(paramInputStream), 4);
    if (isCollection(localPushbackInputStream)) {
      return loadCollectionForBuilding(localPushbackInputStream);
    }
    return new Font.Builder[] { loadSingleOTFForBuilding(localPushbackInputStream) };
  }
  
  private Font loadSingleOTF(InputStream paramInputStream)
    throws IOException
  {
    return loadSingleOTFForBuilding(paramInputStream).build();
  }
  
  private Font[] loadCollection(InputStream paramInputStream)
    throws IOException
  {
    Font.Builder[] arrayOfBuilder = loadCollectionForBuilding(paramInputStream);
    Font[] arrayOfFont = new Font[arrayOfBuilder.length];
    for (int i = 0; i < arrayOfFont.length; i++) {
      arrayOfFont[i] = arrayOfBuilder[i].build();
    }
    return arrayOfFont;
  }
  
  private Font.Builder loadSingleOTFForBuilding(InputStream paramInputStream)
    throws IOException
  {
    MessageDigest localMessageDigest = null;
    if (fingerprintFont())
    {
      try
      {
        localMessageDigest = MessageDigest.getInstance("SHA-1");
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new IOException("Unable to get requested message digest algorithm.", localNoSuchAlgorithmException);
      }
      localObject = new DigestInputStream(paramInputStream, localMessageDigest);
      paramInputStream = (InputStream)localObject;
    }
    Object localObject = Font.Builder.getOTFBuilder(this, paramInputStream);
    if (fingerprintFont()) {
      ((Font.Builder)localObject).setDigest(localMessageDigest.digest());
    }
    return (Font.Builder)localObject;
  }
  
  private Font.Builder[] loadCollectionForBuilding(InputStream paramInputStream)
    throws IOException
  {
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramInputStream.available());
    localWritableFontData.copyFrom(paramInputStream);
    return loadCollectionForBuilding(localWritableFontData);
  }
  
  private static boolean isCollection(PushbackInputStream paramPushbackInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4];
    paramPushbackInputStream.read(arrayOfByte);
    paramPushbackInputStream.unread(arrayOfByte);
    return Tag.ttcf == Tag.intValue(arrayOfByte);
  }
  
  public Font[] loadFonts(byte[] paramArrayOfByte)
    throws IOException
  {
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramArrayOfByte);
    if (isCollection(localWritableFontData)) {
      return loadCollection(localWritableFontData);
    }
    return new Font[] { loadSingleOTF(localWritableFontData) };
  }
  
  public Font.Builder[] loadFontsForBuilding(byte[] paramArrayOfByte)
    throws IOException
  {
    WritableFontData localWritableFontData = WritableFontData.createWritableFontData(paramArrayOfByte);
    if (isCollection(localWritableFontData)) {
      return loadCollectionForBuilding(localWritableFontData);
    }
    return new Font.Builder[] { loadSingleOTFForBuilding(localWritableFontData, 0) };
  }
  
  private Font loadSingleOTF(WritableFontData paramWritableFontData)
    throws IOException
  {
    return loadSingleOTFForBuilding(paramWritableFontData, 0).build();
  }
  
  private Font[] loadCollection(WritableFontData paramWritableFontData)
    throws IOException
  {
    Font.Builder[] arrayOfBuilder = loadCollectionForBuilding(paramWritableFontData);
    Font[] arrayOfFont = new Font[arrayOfBuilder.length];
    for (int i = 0; i < arrayOfFont.length; i++) {
      arrayOfFont[i] = arrayOfBuilder[i].build();
    }
    return arrayOfFont;
  }
  
  private Font.Builder loadSingleOTFForBuilding(WritableFontData paramWritableFontData, int paramInt)
    throws IOException
  {
    Object localObject = null;
    if (fingerprintFont()) {}
    Font.Builder localBuilder = Font.Builder.getOTFBuilder(this, paramWritableFontData, paramInt);
    return localBuilder;
  }
  
  private Font.Builder[] loadCollectionForBuilding(WritableFontData paramWritableFontData)
    throws IOException
  {
    int i = paramWritableFontData.readULongAsInt(Offset.TTCTag.offset);
    long l = paramWritableFontData.readFixed(Offset.Version.offset);
    int j = paramWritableFontData.readULongAsInt(Offset.numFonts.offset);
    Font.Builder[] arrayOfBuilder = new Font.Builder[j];
    int k = Offset.OffsetTable.offset;
    int m = 0;
    while (m < j)
    {
      int n = paramWritableFontData.readULongAsInt(k);
      arrayOfBuilder[m] = loadSingleOTFForBuilding(paramWritableFontData, n);
      m++;
      k += FontData.DataSize.ULONG.size();
    }
    return arrayOfBuilder;
  }
  
  private static boolean isCollection(ReadableFontData paramReadableFontData)
  {
    byte[] arrayOfByte = new byte[4];
    paramReadableFontData.readBytes(0, arrayOfByte, 0, arrayOfByte.length);
    return Tag.ttcf == Tag.intValue(arrayOfByte);
  }
  
  public void serializeFont(Font paramFont, OutputStream paramOutputStream)
    throws IOException
  {
    paramFont.serialize(paramOutputStream, this.tableOrdering);
  }
  
  public void setSerializationTableOrdering(List<Integer> paramList)
  {
    this.tableOrdering = new ArrayList(paramList);
  }
  
  public Font.Builder newFontBuilder()
  {
    return Font.Builder.getOTFBuilder(this);
  }
  
  private static enum Offset
  {
    TTCTag(0),  Version(4),  numFonts(8),  OffsetTable(12),  ulDsigTag(0),  ulDsigLength(4),  ulDsigOffset(8);
    
    private final int offset;
    
    private Offset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\FontFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */