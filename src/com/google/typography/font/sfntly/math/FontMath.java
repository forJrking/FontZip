package com.google.typography.font.sfntly.math;

public final class FontMath
{
  public static int log2(int paramInt)
  {
    for (int i = 0; paramInt != 0; i++) {
      paramInt >>= 1;
    }
    return i - 1;
  }
  
  public static int paddingRequired(int paramInt1, int paramInt2)
  {
    int i = paramInt2 - paramInt1 % paramInt2;
    return i == paramInt2 ? 0 : i;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\sfntly\math\FontMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */