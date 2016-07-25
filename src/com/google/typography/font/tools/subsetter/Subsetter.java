package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.Table;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.core.CMapTable.CMapId;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Subsetter
{
  protected final Font font;
  private FontFactory fontFactory;
  protected Set<TableSubsetter> tableSubsetters;
  private Set<Integer> removeTables;
  private List<Integer> newToOldGlyphs;
  private List<CMapTable.CMapId> cmapIds;
  private Map<Integer, Integer> oldToNewGlyphs = null;
  
  protected Subsetter(Font paramFont, FontFactory paramFontFactory)
  {
    this.font = paramFont;
    this.fontFactory = paramFontFactory;
  }
  
  public void setGlyphs(List<Integer> paramList)
  {
    this.newToOldGlyphs = new ArrayList(paramList);
  }
  
  public void setCMaps(List<CMapTable.CMapId> paramList, int paramInt)
  {
    this.cmapIds = new ArrayList();
    CMapTable localCMapTable = (CMapTable)this.font.getTable(Tag.cmap);
    if (localCMapTable == null) {
      throw new InvalidParameterException("Font has no cmap table.");
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      CMapTable.CMapId localCMapId = (CMapTable.CMapId)localIterator.next();
      CMap localCMap = localCMapTable.cmap(localCMapId);
      if (localCMap != null)
      {
        this.cmapIds.add(localCMap.cmapId());
        paramInt--;
        if (paramInt <= 0) {
          break;
        }
      }
    }
    if (this.cmapIds.size() == 0)
    {
      this.cmapIds = null;
      throw new InvalidParameterException("CMap Id settings would generate font with no cmap sub-table.");
    }
  }
  
  public void setRemoveTables(Set<Integer> paramSet)
  {
    this.removeTables = new HashSet(paramSet);
  }
  
  public Font.Builder subset()
    throws IOException
  {
    Font.Builder localBuilder = this.fontFactory.newFontBuilder();
    setUpTables(localBuilder);
    TreeSet localTreeSet = new TreeSet(this.font.tableMap().keySet());
    if (this.removeTables != null) {
      localTreeSet.removeAll(this.removeTables);
    }
    Iterator localIterator = this.tableSubsetters.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (TableSubsetter)localIterator.next();
      boolean bool = ((TableSubsetter)localObject).subset(this, this.font, localBuilder);
      if (bool) {
        localTreeSet.removeAll(((TableSubsetter)localObject).tagsHandled());
      }
    }
    localIterator = localTreeSet.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Integer)localIterator.next();
      Table localTable = this.font.getTable(((Integer)localObject).intValue());
      if (localTable != null) {
        localBuilder.newTableBuilder(((Integer)localObject).intValue(), localTable.readFontData());
      }
    }
    return localBuilder;
  }
  
  List<Integer> glyphMappingTable()
  {
    return this.newToOldGlyphs;
  }
  
  Map<Integer, Integer> getInverseMapping()
  {
    if (this.oldToNewGlyphs == null)
    {
      this.oldToNewGlyphs = new HashMap();
      List localList = glyphMappingTable();
      for (int i = 0; i < localList.size(); i++) {
        this.oldToNewGlyphs.put(localList.get(i), Integer.valueOf(i));
      }
    }
    return this.oldToNewGlyphs;
  }
  
  List<CMapTable.CMapId> cmapId()
  {
    return this.cmapIds;
  }
  
  protected void setUpTables(Font.Builder paramBuilder) {}
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\Subsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */