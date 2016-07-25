package com.google.typography.font.sfntly.table;

import com.google.typography.font.sfntly.Tag;
import java.util.Comparator;

public final class Header
{
  private final int tag;
  private final int offset;
  private final boolean offsetValid;
  private final int length;
  private final boolean lengthValid;
  private final long checksum;
  private final boolean checksumValid;
  public static final Comparator<Header> COMPARATOR_BY_OFFSET = new Comparator()
  {
    public int compare(Header paramAnonymousHeader1, Header paramAnonymousHeader2)
    {
      return paramAnonymousHeader1.offset - paramAnonymousHeader2.offset;
    }
  };
  public static final Comparator<Header> COMPARATOR_BY_TAG = new Comparator()
  {
    public int compare(Header paramAnonymousHeader1, Header paramAnonymousHeader2)
    {
      return paramAnonymousHeader1.tag - paramAnonymousHeader2.tag;
    }
  };
  
  public Header(int paramInt1, long paramLong, int paramInt2, int paramInt3)
  {
    this.tag = paramInt1;
    this.checksum = paramLong;
    this.checksumValid = true;
    this.offset = paramInt2;
    this.offsetValid = true;
    this.length = paramInt3;
    this.lengthValid = true;
  }
  
  public Header(int paramInt1, int paramInt2)
  {
    this.tag = paramInt1;
    this.checksum = 0L;
    this.checksumValid = false;
    this.offset = 0;
    this.offsetValid = false;
    this.length = paramInt2;
    this.lengthValid = true;
  }
  
  public Header(int paramInt)
  {
    this.tag = paramInt;
    this.checksum = 0L;
    this.checksumValid = false;
    this.offset = 0;
    this.offsetValid = false;
    this.length = 0;
    this.lengthValid = true;
  }
  
  public int tag()
  {
    return this.tag;
  }
  
  public int offset()
  {
    return this.offset;
  }
  
  public boolean offsetValid()
  {
    return this.offsetValid;
  }
  
  public int length()
  {
    return this.length;
  }
  
  public boolean lengthValid()
  {
    return this.lengthValid;
  }
  
  public long checksum()
  {
    return this.checksum;
  }
  
  public boolean checksumValid()
  {
    return this.checksumValid;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Header)) {
      return false;
    }
    return ((Header)paramObject).tag == this.tag;
  }
  
  public int hashCode()
  {
    return this.tag;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(Tag.stringValue(this.tag));
    localStringBuilder.append(", ");
    localStringBuilder.append(Long.toHexString(this.checksum));
    localStringBuilder.append(", ");
    localStringBuilder.append(Integer.toHexString(this.offset));
    localStringBuilder.append(", ");
    localStringBuilder.append(Integer.toHexString(this.length));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\table\Header.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */