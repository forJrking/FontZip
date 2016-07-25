package com.google.typography.font.tools.conversion.eot;

import java.io.ByteArrayOutputStream;

public class MagnitudeDependentWriter
{
  private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
  private byte byteBuf = 0;
  private int bitCount = 0;
  
  private void writeBit(int paramInt)
  {
    this.byteBuf = ((byte)(this.byteBuf | paramInt << this.bitCount));
    this.bitCount += 1;
    if (this.bitCount == 8)
    {
      this.buf.write(this.byteBuf);
      this.byteBuf = 0;
      this.bitCount = 0;
    }
  }
  
  public void writeValue(int paramInt)
  {
    if (paramInt == 0)
    {
      writeBit(0);
    }
    else
    {
      int i = Math.abs(paramInt);
      for (int j = 0; j < i; j++) {
        writeBit(1);
      }
      writeBit(0);
      writeBit(paramInt > 0 ? 0 : 1);
    }
  }
  
  public void flush()
  {
    if (this.bitCount > 0)
    {
      this.buf.write(this.byteBuf);
      this.byteBuf = 0;
      this.bitCount = 0;
    }
  }
  
  public byte[] toByteArray()
  {
    return this.buf.toByteArray();
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\MagnitudeDependentWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */