package com.google.typography.font.sfntly.math;

public final class Fixed1616
{
  public static int integral(int paramInt)
  {
    return paramInt >> 16 & 0xFFFF;
  }
  
  public static int fractional(int paramInt)
  {
    return paramInt & 0xFFFF;
  }
  
  public static int fixed(int paramInt1, int paramInt2)
  {
    return (paramInt1 & 0xFFFF) << 16 | paramInt2 & 0xFFFF;
  }
  
  public static double doubleValue(int paramInt)
  {
    return paramInt / 65536.0D;
  }
  
  public static String toString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(integral(paramInt));
    localStringBuilder.append(".");
    localStringBuilder.append(fractional(paramInt));
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\math\Fixed1616.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */