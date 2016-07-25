package com.google.typography.font.tools.sfnttool;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.CMapTable.CMapId;
import com.google.typography.font.tools.conversion.eot.EOTWriter;
import com.google.typography.font.tools.conversion.woff.WoffWriter;
import com.google.typography.font.tools.subsetter.HintStripper;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import com.google.typography.font.tools.subsetter.Subsetter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SfntTool
{
  private boolean strip = false;
  private String subsetString = null;
  private boolean woff = false;
  private boolean eot = false;
  private boolean mtx = false;
  
  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    SfntTool localSfntTool = new SfntTool();
    File localFile1 = null;
    File localFile2 = null;
    int i = 0;
    int j = 1;
    for (int k = 0; k < paramArrayOfString.length; k++)
    {
      String str = null;
      if (paramArrayOfString[k].charAt(0) == '-') {
        str = paramArrayOfString[k].substring(1);
      }
      if (str != null)
      {
        if ((str.equals("help")) || (str.equals("?")))
        {
          printUsage();
          System.exit(0);
        }
        else if ((str.equals("b")) || (str.equals("bench")))
        {
          j = 10000;
        }
        else if ((str.equals("h")) || (str.equals("hints")))
        {
          localSfntTool.strip = true;
        }
        else if ((str.equals("s")) || (str.equals("string")))
        {
          localSfntTool.subsetString = paramArrayOfString[(k + 1)];
          k++;
        }
        else if ((str.equals("w")) || (str.equals("woff")))
        {
          localSfntTool.woff = true;
        }
        else if ((str.equals("e")) || (str.equals("eot")))
        {
          localSfntTool.eot = true;
        }
        else if ((str.equals("x")) || (str.equals("mtx")))
        {
          localSfntTool.mtx = true;
        }
        else
        {
          printUsage();
          System.exit(1);
        }
      }
      else if (localFile1 == null)
      {
        localFile1 = new File(paramArrayOfString[k]);
      }
      else
      {
        localFile2 = new File(paramArrayOfString[k]);
        break;
      }
    }
    if ((localSfntTool.woff) && (localSfntTool.eot))
    {
      System.out.println("WOFF and EOT options are mutually exclusive");
      System.exit(1);
    }
    if ((localFile1 != null) && (localFile2 != null)) {
      localSfntTool.subsetFontFile(localFile1, localFile2, j);
    } else {
      printUsage();
    }
  }
  
  private static final void printUsage()
  {
    System.out.println("Subset [-?|-h|-help] [-b] [-s string] fontfile outfile");
    System.out.println("Prototype font subsetter");
    System.out.println("\t-?,-help\tprint this help information");
    System.out.println("\t-s,-string\t String to subset");
    System.out.println("\t-b,-bench\t Benchmark (run 10000 iterations)");
    System.out.println("\t-h,-hints\t Strip hints");
    System.out.println("\t-w,-woff\t Output WOFF format");
    System.out.println("\t-e,-eot\t Output EOT format");
    System.out.println("\t-x,-mtx\t Enable Microtype Express compression for EOT format");
  }
  
  public void subsetFontFile(File paramFile1, File paramFile2, int paramInt)
    throws IOException
  {
    FontFactory localFontFactory = FontFactory.getInstance();
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramFile1);
      byte[] arrayOfByte = new byte[(int)paramFile1.length()];
      localFileInputStream.read(arrayOfByte);
      Font[] arrayOfFont = null;
      arrayOfFont = localFontFactory.loadFonts(arrayOfByte);
      Font localFont1 = arrayOfFont[0];
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(CMapTable.CMapId.WINDOWS_BMP);
      Object localObject1 = null;
      for (int i = 0; i < paramInt; i++)
      {
        Font localFont2 = localFont1;
        Object localObject3;
        if (this.subsetString != null)
        {
          localObject2 = new RenumberingSubsetter(localFont2, localFontFactory);
          ((Subsetter)localObject2).setCMaps(localArrayList, 1);
          localObject3 = GlyphCoverage.getGlyphCoverage(localFont1, this.subsetString);
          ((Subsetter)localObject2).setGlyphs((List)localObject3);
          HashSet localHashSet = new HashSet();
          localHashSet.add(Integer.valueOf(Tag.GDEF));
          localHashSet.add(Integer.valueOf(Tag.GPOS));
          localHashSet.add(Integer.valueOf(Tag.GSUB));
          localHashSet.add(Integer.valueOf(Tag.kern));
          localHashSet.add(Integer.valueOf(Tag.hdmx));
          localHashSet.add(Integer.valueOf(Tag.vmtx));
          localHashSet.add(Integer.valueOf(Tag.VDMX));
          localHashSet.add(Integer.valueOf(Tag.LTSH));
          localHashSet.add(Integer.valueOf(Tag.DSIG));
          localHashSet.add(Integer.valueOf(Tag.intValue(new byte[] { 109, 111, 114, 116 })));
          localHashSet.add(Integer.valueOf(Tag.intValue(new byte[] { 109, 111, 114, 120 })));
          ((Subsetter)localObject2).setRemoveTables(localHashSet);
          localFont2 = ((Subsetter)localObject2).subset().build();
        }
        if (this.strip)
        {
          localObject2 = new HintStripper(localFont2, localFontFactory);
          localObject3 = new HashSet();
          ((Set)localObject3).add(Integer.valueOf(Tag.fpgm));
          ((Set)localObject3).add(Integer.valueOf(Tag.prep));
          ((Set)localObject3).add(Integer.valueOf(Tag.cvt));
          ((Set)localObject3).add(Integer.valueOf(Tag.hdmx));
          ((Set)localObject3).add(Integer.valueOf(Tag.VDMX));
          ((Set)localObject3).add(Integer.valueOf(Tag.LTSH));
          ((Set)localObject3).add(Integer.valueOf(Tag.DSIG));
          ((Subsetter)localObject2).setRemoveTables((Set)localObject3);
          localFont2 = ((Subsetter)localObject2).subset().build();
        }
        Object localObject2 = new FileOutputStream(paramFile2);
        if (this.woff)
        {
          localObject3 = new WoffWriter().convert(localFont2);
          ((WritableFontData)localObject3).copyTo((OutputStream)localObject2);
        }
        else if (this.eot)
        {
          localObject3 = new EOTWriter(this.mtx).convert(localFont2);
          ((WritableFontData)localObject3).copyTo((OutputStream)localObject2);
        }
        else
        {
          localFontFactory.serializeFont(localFont2, (OutputStream)localObject2);
        }
      }
    }
    finally
    {
      if (localFileInputStream != null) {
        localFileInputStream.close();
      }
    }
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\sfnttool\SfntTool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */