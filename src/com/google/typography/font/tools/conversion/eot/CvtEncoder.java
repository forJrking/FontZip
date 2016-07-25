package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.table.truetype.ControlValueTable;
import java.io.ByteArrayOutputStream;

public class CvtEncoder
{
  private static final int CVT_POS8 = 255;
  private static final int CVT_POS1 = 248;
  private static final int CVT_NEG8 = 247;
  private static final int CVT_NEG1 = 240;
  private static final int CVT_NEG0 = 239;
  private static final int CVT_WORDCODE = 238;
  private static final int CVT_LOWESTCODE = 238;
  private ByteArrayOutputStream cvtStream = new ByteArrayOutputStream();
  
  public void encode(ControlValueTable paramControlValueTable)
  {
    int i = paramControlValueTable.fwordCount();
    this.cvtStream.write(i >> 8);
    this.cvtStream.write(i & 0xFF);
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = paramControlValueTable.fword(k * 2);
      int n = (short)(m - j);
      int i1 = Math.abs(n);
      int i2 = i1 / 238;
      if (i2 <= 8)
      {
        if (n < 0)
        {
          this.cvtStream.write(239 + i2);
          this.cvtStream.write(i1 - i2 * 238);
        }
        else
        {
          if (i2 > 0) {
            this.cvtStream.write(248 + i2 - 1);
          }
          this.cvtStream.write(i1 - i2 * 238);
        }
      }
      else
      {
        this.cvtStream.write(238);
        this.cvtStream.write(n >> 8);
        this.cvtStream.write(n & 0xFF);
      }
      j = m;
    }
  }
  
  public byte[] toByteArray()
  {
    return this.cvtStream.toByteArray();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\CvtEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */