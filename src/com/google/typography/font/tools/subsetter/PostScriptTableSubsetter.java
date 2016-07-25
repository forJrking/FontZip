package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.PostScriptTable;
import java.util.ArrayList;
import java.util.List;

public class PostScriptTableSubsetter
  extends TableSubsetterImpl
{
  protected PostScriptTableSubsetter()
  {
    super(new Integer[] { Integer.valueOf(Tag.post) });
  }
  
  public boolean subset(Subsetter paramSubsetter, Font paramFont, Font.Builder paramBuilder)
  {
    List localList = paramSubsetter.glyphMappingTable();
    if (localList == null) {
      return false;
    }
    PostScriptTableBuilder localPostScriptTableBuilder = new PostScriptTableBuilder();
    PostScriptTable localPostScriptTable = (PostScriptTable)paramFont.getTable(Tag.post);
    localPostScriptTableBuilder.initV1From(localPostScriptTable);
    if ((localPostScriptTable.version() == 65536) || (localPostScriptTable.version() == 131072))
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < localList.size(); i++) {
        localArrayList.add(localPostScriptTable.glyphName(((Integer)localList.get(i)).intValue()));
      }
      localPostScriptTableBuilder.setNames(localArrayList);
    }
    paramBuilder.newTableBuilder(Tag.post, localPostScriptTableBuilder.build());
    return true;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\google\typography\font\tools\subsetter\PostScriptTableSubsetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */