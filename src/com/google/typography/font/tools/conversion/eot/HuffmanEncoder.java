package com.google.typography.font.tools.conversion.eot;

public class HuffmanEncoder
{
  private static final int ROOT = 1;
  private TreeNode[] tree;
  private short[] symbolIndex;
  private int bitCount2;
  private int range;
  private BitIOWriter bits;
  
  public HuffmanEncoder(BitIOWriter paramBitIOWriter, int paramInt)
  {
    this.bits = paramBitIOWriter;
    this.range = paramInt;
    bitsUsed(paramInt - 1);
    if ((paramInt > 256) && (paramInt < 512)) {
      this.bitCount2 = bitsUsed(paramInt - 257);
    } else {
      this.bitCount2 = 0;
    }
    this.symbolIndex = new short[paramInt];
    int i = 2 * paramInt;
    this.tree = new TreeNode[i];
    for (int j = 0; j < i; j++) {
      this.tree[j] = new TreeNode(null);
    }
    for (j = 2; j < i; j++)
    {
      this.tree[j].up = ((short)(j / 2));
      this.tree[j].weight = 1;
    }
    for (j = 1; j < paramInt; j++)
    {
      this.tree[j].left = ((short)(2 * j));
      this.tree[j].right = ((short)(2 * j + 1));
    }
    for (j = 0; j < paramInt; j++)
    {
      this.tree[j].code = -1;
      this.tree[(paramInt + j)].code = ((short)j);
      this.tree[(paramInt + j)].left = -1;
      this.tree[(paramInt + j)].right = -1;
      this.symbolIndex[j] = ((short)(paramInt + j));
    }
    initWeight(1);
    if (this.bitCount2 != 0)
    {
      updateWeight(this.symbolIndex['Ā']);
      updateWeight(this.symbolIndex['ā']);
      for (j = 0; j < 12; j++) {
        updateWeight(this.symbolIndex[(paramInt - 3)]);
      }
      for (j = 0; j < 6; j++) {
        updateWeight(this.symbolIndex[(paramInt - 2)]);
      }
    }
    else
    {
      for (j = 0; j < 2; j++) {
        for (int k = 0; k < paramInt; k++) {
          updateWeight(this.symbolIndex[k]);
        }
      }
    }
  }
  
  String checkTree()
  {
    for (int i = 1; i < this.range; i++) {
      if (this.tree[i].code < 0)
      {
        if (this.tree[this.tree[i].left].up != i) {
          return "tree[tree[" + i + "].left].up == " + this.tree[this.tree[i].left].up + ", expected " + i;
        }
        if (this.tree[this.tree[i].right].up != i) {
          return "tree[tree[" + i + "].right].up == " + this.tree[this.tree[i].right].up + ", expected " + i;
        }
      }
    }
    for (i = 1; i < this.range; i++) {
      if ((this.tree[i].code < 0) && (this.tree[i].weight != this.tree[this.tree[i].left].weight + this.tree[this.tree[i].right].weight)) {
        return "tree[" + i + "].weight == " + this.tree[i].weight + ", expected " + this.tree[this.tree[i].left].weight + " + " + this.tree[this.tree[i].right].weight;
      }
    }
    i = this.range * 2 - 1;
    for (int j = 1; j < i; j++) {
      if (this.tree[j].weight < this.tree[(j + 1)].weight) {
        return "tree[" + j + "].weight == " + this.tree[j].weight + ", tree[" + (j + 1) + ".weight == " + this.tree[(j + 1)].weight + ", not >=";
      }
    }
    int k;
    for (j = 2; j < i; j++) {
      if (this.tree[j].code < 0)
      {
        k = this.tree[j].left;
        int m = this.tree[j].right;
        if ((k - m != 1) && (k - m != -1)) {
          return "tree[" + j + "].left == " + this.tree[j].left + ", tree[" + j + "].right] == " + this.tree[j].right + ", siblings not adjacent";
        }
      }
    }
    for (j = 2; j < this.range * 2; j++)
    {
      k = this.tree[j].up;
      if ((this.tree[k].left != j) && (this.tree[k].right != j)) {
        return "tree[" + k + "].left != " + j + " && tree[" + k + "].right != " + j;
      }
    }
    return null;
  }
  
  private int initWeight(int paramInt)
  {
    if (this.tree[paramInt].code < 0) {
      this.tree[paramInt].weight = (initWeight(this.tree[paramInt].left) + initWeight(this.tree[paramInt].right));
    }
    return this.tree[paramInt].weight;
  }
  
  private void updateWeight(int paramInt)
  {
    while (paramInt != 1)
    {
      int i = this.tree[paramInt].weight;
      int j = paramInt - 1;
      if (this.tree[j].weight == i)
      {
        do
        {
          j--;
        } while (this.tree[j].weight == i);
        j++;
        if (j > 1)
        {
          swapNodes(paramInt, j);
          paramInt = j;
        }
      }
      i++;
      this.tree[paramInt].weight = i;
      paramInt = this.tree[paramInt].up;
    }
    this.tree[paramInt].weight += 1;
  }
  
  private void swapNodes(int paramInt1, int paramInt2)
  {
    short s1 = this.tree[paramInt1].up;
    short s2 = this.tree[paramInt2].up;
    TreeNode localTreeNode = this.tree[paramInt1];
    this.tree[paramInt1] = this.tree[paramInt2];
    this.tree[paramInt2] = localTreeNode;
    this.tree[paramInt1].up = s1;
    this.tree[paramInt2].up = s2;
    int i = this.tree[paramInt1].code;
    if (i < 0)
    {
      this.tree[this.tree[paramInt1].left].up = ((short)paramInt1);
      this.tree[this.tree[paramInt1].right].up = ((short)paramInt1);
    }
    else
    {
      this.symbolIndex[i] = ((short)paramInt1);
    }
    i = this.tree[paramInt2].code;
    if (i < 0)
    {
      this.tree[this.tree[paramInt2].left].up = ((short)paramInt2);
      this.tree[this.tree[paramInt2].right].up = ((short)paramInt2);
    }
    else
    {
      this.symbolIndex[i] = ((short)paramInt2);
    }
  }
  
  public int writeSymbolCost(int paramInt)
  {
    int i = this.symbolIndex[paramInt];
    int j = 0;
    do
    {
      j++;
      i = this.tree[i].up;
    } while (i != 1);
    return j << 16;
  }
  
  public void writeSymbol(int paramInt)
  {
    int i = this.symbolIndex[paramInt];
    int j = i;
    boolean[] arrayOfBoolean = new boolean[50];
    int k = 0;
    do
    {
      int m = this.tree[i].up;
      arrayOfBoolean[(k++)] = (this.tree[m].right == i ? 1 : false);
      i = m;
    } while (i != 1);
    do
    {
      this.bits.writeBit(arrayOfBoolean[(--k)]);
    } while (k > 0);
    updateWeight(j);
  }
  
  public static int bitsUsed(int paramInt)
  {
    for (int i = 32; (i > 1) && ((paramInt & 1 << i - 1) == 0); i--) {}
    return i;
  }
  
  private static class TreeNode
  {
    short up;
    short left;
    short right;
    short code;
    int weight;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\conversion\eot\HuffmanEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */