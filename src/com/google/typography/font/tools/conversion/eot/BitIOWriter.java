package com.google.typography.font.tools.conversion.eot;

import java.io.ByteArrayOutputStream;

public class BitIOWriter
{
  private ByteArrayOutputStream buf = new ByteArrayOutputStream();
  private byte byteBuf = 0;
  private int bitCount = 0;
  
  public void writeBit(int paramInt)
  {
    this.byteBuf = ((byte)(this.byteBuf | paramInt << 7 - this.bitCount));
    this.bitCount += 1;
    if (this.bitCount == 8)
    {
      this.buf.write(this.byteBuf);
      this.byteBuf = 0;
      this.bitCount = 0;
    }
  }
  
  public void writeBit(boolean paramBoolean)
  {
    writeBit(paramBoolean ? 1 : 0);
  }
  
  public void writeValue(int paramInt1, int paramInt2)
  {
    for (int i = paramInt2 - 1; i >= 0; i--) {
      writeBit(paramInt1 >> i & 0x1);
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


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\BitIOWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */