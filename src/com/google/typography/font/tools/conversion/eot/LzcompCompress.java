package com.google.typography.font.tools.conversion.eot;

public class LzcompCompress
{
  private static final int MAX_2BYTE_DIST = 512;
  private static final int DIST_MIN = 1;
  private static final int DIST_WIDTH = 3;
  private static final int LEN_MIN = 2;
  private static final int LEN_MIN3 = 3;
  private static final int LEN_WIDTH = 3;
  private static final int BIT_RANGE = 2;
  private static final int PRELOAD_SIZE = 7168;
  private static final int DEFAULT_MAX_COPY_DIST = Integer.MAX_VALUE;
  private BitIOWriter bits = new BitIOWriter();
  private boolean usingRunLength = false;
  private int length1;
  private int maxCopyDist = Integer.MAX_VALUE;
  private HuffmanEncoder distEncoder;
  private HuffmanEncoder lenEncoder;
  private HuffmanEncoder symEncoder;
  private int numDistRanges;
  private int distMax;
  private int dup2;
  private int dup4;
  private int dup6;
  private int numSyms;
  private byte[] buf;
  private HashNode[] hashTable;
  
  private void write(byte[] paramArrayOfByte)
  {
    this.bits.writeBit(this.usingRunLength);
    this.length1 = paramArrayOfByte.length;
    setDistRange(this.length1);
    this.distEncoder = new HuffmanEncoder(this.bits, 8);
    this.lenEncoder = new HuffmanEncoder(this.bits, 8);
    this.symEncoder = new HuffmanEncoder(this.bits, this.numSyms);
    this.buf = new byte[7168 + this.length1];
    System.arraycopy(paramArrayOfByte, 0, this.buf, 7168, this.length1);
    encode();
    this.bits.flush();
  }
  
  void setDistRange(int paramInt)
  {
    this.numDistRanges = 1;
    for (this.distMax = (1 + (1 << 3 * this.numDistRanges) - 1); this.distMax < this.length1; this.distMax = (1 + (1 << 3 * this.numDistRanges) - 1)) {
      this.numDistRanges += 1;
    }
    this.dup2 = (256 + 8 * this.numDistRanges);
    this.dup4 = (this.dup2 + 1);
    this.dup6 = (this.dup4 + 1);
    this.numSyms = (this.dup6 + 1);
  }
  
  private void encode()
  {
    int i = this.length1 + 7168;
    initializeModel();
    this.bits.writeValue(this.length1, 24);
    int j = this.length1 + 7168;
    int[] arrayOfInt = new int[1];
    int k = 7168;
    while (k < j)
    {
      int m = k;
      int n = makeCopyDecision(k++, arrayOfInt);
      int i1;
      if (n > 0)
      {
        i1 = getNumDistRanges(arrayOfInt[0]);
        encodeLength(n, arrayOfInt[0], i1);
        encodeDistance2(arrayOfInt[0], i1);
        for (int i2 = 1; i2 < n; i2++) {
          updateModel(k++);
        }
      }
      else
      {
        i1 = this.buf[m];
        if ((m >= 2) && (i1 == this.buf[(m - 2)])) {
          this.symEncoder.writeSymbol(this.dup2);
        } else if ((m >= 4) && (i1 == this.buf[(m - 4)])) {
          this.symEncoder.writeSymbol(this.dup4);
        } else if ((m >= 6) && (i1 == this.buf[(m - 6)])) {
          this.symEncoder.writeSymbol(this.dup6);
        } else {
          this.symEncoder.writeSymbol(this.buf[m] & 0xFF);
        }
      }
    }
  }
  
  void initializeModel()
  {
    this.hashTable = new HashNode[65536];
    int i = 0;
    for (int j = 0; j < 32; j++) {
      for (int k = 0; k < 96; k++)
      {
        this.buf[i] = ((byte)j);
        updateModel(i++);
        this.buf[i] = ((byte)k);
        updateModel(i++);
      }
    }
    for (j = 0; (i < 7168) && (j < 256); j++)
    {
      this.buf[i] = ((byte)j);
      updateModel(i++);
      this.buf[i] = ((byte)j);
      updateModel(i++);
      this.buf[i] = ((byte)j);
      updateModel(i++);
      this.buf[i] = ((byte)j);
      updateModel(i++);
    }
  }
  
  private int makeCopyDecision(int paramInt, int[] paramArrayOfInt)
  {
    int[] arrayOfInt1 = new int[1];
    int[] arrayOfInt2 = new int[1];
    int[] arrayOfInt3 = new int[1];
    int i = paramInt;
    int j = findMatch(paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3);
    updateModel(paramInt++);
    if (arrayOfInt2[0] > 0)
    {
      int[] arrayOfInt4 = new int[1];
      int[] arrayOfInt5 = new int[1];
      int[] arrayOfInt6 = new int[1];
      int k = findMatch(paramInt, arrayOfInt4, arrayOfInt5, arrayOfInt6);
      int m = this.symEncoder.writeSymbolCost(this.buf[i] & 0xFF);
      if ((arrayOfInt5[0] >= arrayOfInt2[0]) && (arrayOfInt3[0] > (arrayOfInt6[0] * k + m) / (k + 1)))
      {
        j = 0;
      }
      else if (j > 3)
      {
        k = findMatch(i + j, arrayOfInt4, arrayOfInt5, arrayOfInt6);
        if (k >= 2)
        {
          int[] arrayOfInt7 = new int[1];
          int[] arrayOfInt8 = new int[1];
          int[] arrayOfInt9 = new int[1];
          int i1 = findMatch(i + j - 1, arrayOfInt7, arrayOfInt8, arrayOfInt9);
          if ((i1 > k) && (arrayOfInt9[0] < arrayOfInt6[0]))
          {
            int i2 = getNumDistRanges(arrayOfInt1[0] + 1);
            int i3 = encodeLengthCost(j - 1, arrayOfInt1[0] + 1, i2);
            int i4 = encodeDistance2Cost(arrayOfInt1[0] + 1, i2);
            int i5 = i3 + i4 + arrayOfInt9[0] * i1;
            int i6 = arrayOfInt3[0] * j + arrayOfInt6[0] * k;
            if (i6 / (j + k) > i5 / (j - 1 + i1))
            {
              j--;
              arrayOfInt1[0] += 1;
            }
          }
        }
      }
      if (j == 2)
      {
        int n;
        if ((i >= 2) && (this.buf[i] == this.buf[(i - 2)]))
        {
          n = this.symEncoder.writeSymbolCost(this.dup2);
          if (arrayOfInt3[0] * 2 > n + this.symEncoder.writeSymbolCost(this.buf[(i + 1)] & 0xFF)) {
            j = 0;
          }
        }
        else if ((i >= 1) && (i + 1 < this.buf.length) && (this.buf[(i + 1)] == this.buf[(i - 1)]))
        {
          n = this.symEncoder.writeSymbolCost(this.dup2);
          if (arrayOfInt3[0] * 2 > m + n) {
            j = 0;
          }
        }
      }
    }
    paramArrayOfInt[0] = arrayOfInt1[0];
    return j;
  }
  
  int findMatch(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    int[] arrayOfInt = new int[33];
    int i = this.buf.length - paramInt;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    if (i > 1)
    {
      int i2 = (this.buf[paramInt] & 0xFF) << 8 | this.buf[(paramInt + 1)] & 0xFF;
      Object localObject = null;
      int i3 = 0;
      for (HashNode localHashNode = this.hashTable[i2]; localHashNode != null; localHashNode = localHashNode.next)
      {
        int i4 = localHashNode.index;
        int i5 = paramInt - i4;
        i3++;
        if ((i3 > 256) || (i5 > this.maxCopyDist))
        {
          if (this.hashTable[i2] == localHashNode)
          {
            this.hashTable[i2] = null;
            break;
          }
          ((HashNode)localObject).next = null;
          break;
        }
        int i6 = paramInt - i4;
        if (i < i6) {
          i6 = i;
        }
        if (i6 >= 2)
        {
          i4 += 2;
          int i7 = 2;
          for (i7 = 2; (i7 < i6) && (this.buf[i4] == this.buf[(paramInt + i7)]); i7++) {
            i4++;
          }
          if (i7 >= 2)
          {
            i5 = i5 - i7 + 1;
            if ((i5 <= this.distMax) && ((i7 != 2) || (i5 < 512)) && ((i7 > j) || (i5 <= k) || ((i7 > j - 2) && ((i5 <= k << 3) || ((i7 >= j) && (i5 <= k << 4))))))
            {
              int i8 = 0;
              int i9;
              int i10;
              if (i7 > i1)
              {
                i9 = i7;
                if (i9 > 32) {
                  i9 = 32;
                }
                for (i4 = i1; i4 < i9; i4++)
                {
                  i10 = this.buf[(paramInt + i4)];
                  arrayOfInt[(i4 + 1)] = (arrayOfInt[i4] + this.symEncoder.writeSymbolCost(i10 & 0xFF));
                }
                i1 = i9;
                if (i7 > 32)
                {
                  i8 = arrayOfInt[32];
                  i8 += i8 / 32 * (i7 - 32);
                }
                else
                {
                  i8 = arrayOfInt[i7];
                }
              }
              else
              {
                i8 = arrayOfInt[i7];
              }
              if (i8 > m)
              {
                i9 = getNumDistRanges(i5);
                i10 = encodeLengthCost(i7, i5, i9);
                if (i8 - i10 - (i9 << 16) > m)
                {
                  i10 += encodeDistance2Cost(i5, i9);
                  int i11 = i8 - i10;
                  if (i11 > m)
                  {
                    m = i11;
                    j = i7;
                    k = i5;
                    n = i10;
                  }
                }
              }
            }
          }
        }
        localObject = localHashNode;
      }
    }
    paramArrayOfInt3[0] = (j > 0 ? n / j : 0);
    paramArrayOfInt1[0] = k;
    paramArrayOfInt2[0] = m;
    return j;
  }
  
  private int getNumDistRanges(int paramInt)
  {
    int i = HuffmanEncoder.bitsUsed(paramInt - 1);
    int j = (i + 3 - 1) / 3;
    return j;
  }
  
  private void encodeLength(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 >= 512) {
      paramInt1 -= 3;
    } else {
      paramInt1 -= 2;
    }
    int i = HuffmanEncoder.bitsUsed(paramInt1);
    for (int j = 2; j < i; j += 2) {}
    int k = 1 << j - 1;
    int m = i > 2 ? 2 : 0;
    if ((paramInt1 & k) != 0) {
      m |= 0x1;
    }
    k >>= 1;
    m <<= 1;
    if ((paramInt1 & k) != 0) {
      m |= 0x1;
    }
    k >>= 1;
    this.symEncoder.writeSymbol(256 + m + (paramInt3 - 1) * 8);
    for (j = i - 2; j >= 1; j -= 2)
    {
      m = j > 2 ? 2 : 0;
      if ((paramInt1 & k) != 0) {
        m |= 0x1;
      }
      k >>= 1;
      m <<= 1;
      if ((paramInt1 & k) != 0) {
        m |= 0x1;
      }
      k >>= 1;
      this.lenEncoder.writeSymbol(m);
    }
  }
  
  private int encodeLengthCost(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 >= 512) {
      paramInt1 -= 3;
    } else {
      paramInt1 -= 2;
    }
    int i = HuffmanEncoder.bitsUsed(paramInt1);
    for (int j = 2; j < i; j += 2) {}
    int k = 1 << j - 1;
    int m = i > 2 ? 2 : 0;
    if ((paramInt1 & k) != 0) {
      m |= 0x1;
    }
    k >>= 1;
    m <<= 1;
    if ((paramInt1 & k) != 0) {
      m |= 0x1;
    }
    k >>= 1;
    int n = this.symEncoder.writeSymbolCost(256 + m + (paramInt3 - 1) * 8);
    for (j = i - 2; j >= 1; j -= 2)
    {
      m = j > 2 ? 2 : 0;
      if ((paramInt1 & k) != 0) {
        m |= 0x1;
      }
      k >>= 1;
      m <<= 1;
      if ((paramInt1 & k) != 0) {
        m |= 0x1;
      }
      k >>= 1;
      n += this.lenEncoder.writeSymbolCost(m);
    }
    return n;
  }
  
  private void encodeDistance2(int paramInt1, int paramInt2)
  {
    
    for (int i = (paramInt2 - 1) * 3; i >= 0; i -= 3) {
      this.distEncoder.writeSymbol(paramInt1 >> i & 0x7);
    }
  }
  
  private int encodeDistance2Cost(int paramInt1, int paramInt2)
  {
    int i = 0;
    paramInt1--;
    for (int j = (paramInt2 - 1) * 3; j >= 0; j -= 3) {
      i += this.distEncoder.writeSymbolCost(paramInt1 >> j & 0x7);
    }
    return i;
  }
  
  private void updateModel(int paramInt)
  {
    int i = this.buf[paramInt];
    if (paramInt > 0)
    {
      HashNode localHashNode = new HashNode(null);
      int j = this.buf[(paramInt - 1)];
      int k = (j & 0xFF) << 8 | i & 0xFF;
      localHashNode.index = (paramInt - 1);
      localHashNode.next = this.hashTable[k];
      this.hashTable[k] = localHashNode;
    }
  }
  
  private byte[] toByteArray()
  {
    return this.bits.toByteArray();
  }
  
  public static byte[] compress(byte[] paramArrayOfByte)
  {
    LzcompCompress localLzcompCompress = new LzcompCompress();
    localLzcompCompress.write(paramArrayOfByte);
    return localLzcompCompress.toByteArray();
  }
  
  public static int getPreloadSize()
  {
    return 7168;
  }
  
  private static class HashNode
  {
    int index;
    HashNode next;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\LzcompCompress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */