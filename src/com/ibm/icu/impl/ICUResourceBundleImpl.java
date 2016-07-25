/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import com.ibm.icu.util.UResourceBundleIterator;
/*     */ import com.ibm.icu.util.UResourceTypeMismatchException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ICUResourceBundleImpl
/*     */   extends ICUResourceBundle
/*     */ {
/*     */   protected ICUResourceBundleImpl(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container)
/*     */   {
/*  21 */     super(reader, key, resPath, resource, container);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final ICUResourceBundle createBundleObject(String _key, int _resource, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias)
/*     */   {
/*  28 */     if (isAlias != null) {
/*  29 */       isAlias[0] = false;
/*     */     }
/*  31 */     String _resPath = this.resPath + "/" + _key;
/*  32 */     switch (ICUResourceBundleReader.RES_GET_TYPE(_resource)) {
/*     */     case 0: 
/*     */     case 6: 
/*  35 */       return new ResourceString(this.reader, _key, _resPath, _resource, this);
/*     */     case 1: 
/*  37 */       return new ResourceBinary(this.reader, _key, _resPath, _resource, this);
/*     */     case 3: 
/*  39 */       if (isAlias != null) {
/*  40 */         isAlias[0] = true;
/*     */       }
/*  42 */       return findResource(_key, _resPath, _resource, table, requested);
/*     */     case 7: 
/*  44 */       return new ResourceInt(this.reader, _key, _resPath, _resource, this);
/*     */     case 14: 
/*  46 */       return new ResourceIntVector(this.reader, _key, _resPath, _resource, this);
/*     */     case 8: 
/*     */     case 9: 
/*  49 */       return new ResourceArray(this.reader, _key, _resPath, _resource, this);
/*     */     case 2: 
/*     */     case 4: 
/*     */     case 5: 
/*  53 */       return new ResourceTable(this.reader, _key, _resPath, _resource, this);
/*     */     }
/*  55 */     throw new IllegalStateException("The resource type is unknown");
/*     */   }
/*     */   
/*     */   private static final class ResourceBinary
/*     */     extends ICUResourceBundleImpl
/*     */   {
/*     */     public ByteBuffer getBinary()
/*     */     {
/*  63 */       return this.reader.getBinary(this.resource);
/*     */     }
/*     */     
/*  66 */     public byte[] getBinary(byte[] ba) { return this.reader.getBinary(this.resource, ba); }
/*     */     
/*     */     ResourceBinary(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container)
/*     */     {
/*  70 */       super(key, resPath, resource, container);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ResourceInt extends ICUResourceBundleImpl {
/*  75 */     public int getInt() { return ICUResourceBundleReader.RES_GET_INT(this.resource); }
/*     */     
/*     */     public int getUInt() {
/*  78 */       return ICUResourceBundleReader.RES_GET_UINT(this.resource);
/*     */     }
/*     */     
/*     */ 
/*  82 */     ResourceInt(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container) { super(key, resPath, resource, container); }
/*     */   }
/*     */   
/*     */   private static final class ResourceString extends ICUResourceBundleImpl {
/*     */     private String value;
/*     */     
/*  88 */     public String getString() { return this.value; }
/*     */     
/*     */     ResourceString(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container)
/*     */     {
/*  92 */       super(key, resPath, resource, container);
/*  93 */       this.value = reader.getString(resource);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ResourceIntVector extends ICUResourceBundleImpl { private int[] value;
/*     */     
/*  99 */     public int[] getIntVector() { return this.value; }
/*     */     
/*     */     ResourceIntVector(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container)
/*     */     {
/* 103 */       super(key, resPath, resource, container);
/* 104 */       this.value = reader.getIntVector(resource);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ResourceContainer extends ICUResourceBundleImpl
/*     */   {
/*     */     protected ICUResourceBundleReader.Container value;
/*     */     
/*     */     public int getSize()
/*     */     {
/* 114 */       return this.value.getSize();
/*     */     }
/*     */     
/* 117 */     protected int getContainerResource(int index) { return this.value.getContainerResource(index); }
/*     */     
/*     */     protected UResourceBundle createBundleObject(int index, String resKey, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias)
/*     */     {
/* 121 */       int item = getContainerResource(index);
/* 122 */       if (item == -1) {
/* 123 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 125 */       return createBundleObject(resKey, item, table, requested, isAlias);
/*     */     }
/*     */     
/*     */ 
/* 129 */     ResourceContainer(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container) { super(key, resPath, resource, container); }
/*     */   }
/*     */   
/*     */   private static class ResourceArray extends ICUResourceBundleImpl.ResourceContainer {
/*     */     protected String[] handleGetStringArray() {
/* 134 */       String[] strings = new String[this.value.getSize()];
/* 135 */       UResourceBundleIterator iter = getIterator();
/* 136 */       int i = 0;
/* 137 */       while (iter.hasNext()) {
/* 138 */         strings[(i++)] = iter.next().getString();
/*     */       }
/* 140 */       return strings;
/*     */     }
/*     */     
/* 143 */     public String[] getStringArray() { return handleGetStringArray(); }
/*     */     
/*     */ 
/*     */     protected UResourceBundle handleGetImpl(String indexStr, HashMap<String, String> table, UResourceBundle requested, int[] index, boolean[] isAlias)
/*     */     {
/* 148 */       int i = indexStr.length() > 0 ? Integer.valueOf(indexStr).intValue() : -1;
/* 149 */       if (index != null) {
/* 150 */         index[0] = i;
/*     */       }
/* 152 */       if (i < 0) {
/* 153 */         throw new UResourceTypeMismatchException("Could not get the correct value for index: " + index);
/*     */       }
/* 155 */       return createBundleObject(i, indexStr, table, requested, isAlias);
/*     */     }
/*     */     
/*     */     protected UResourceBundle handleGetImpl(int index, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias) {
/* 159 */       return createBundleObject(index, Integer.toString(index), table, requested, isAlias);
/*     */     }
/*     */     
/*     */     ResourceArray(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container) {
/* 163 */       super(key, resPath, resource, container);
/* 164 */       this.value = reader.getArray(resource);
/* 165 */       createLookupCache();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ResourceTable extends ICUResourceBundleImpl.ResourceContainer {
/* 170 */     protected String getKey(int index) { return ((ICUResourceBundleReader.Table)this.value).getKey(index); }
/*     */     
/*     */     protected Set<String> handleKeySet() {
/* 173 */       TreeSet<String> keySet = new TreeSet();
/* 174 */       ICUResourceBundleReader.Table table = (ICUResourceBundleReader.Table)this.value;
/* 175 */       for (int i = 0; i < table.getSize(); i++) {
/* 176 */         keySet.add(table.getKey(i));
/*     */       }
/* 178 */       return keySet;
/*     */     }
/*     */     
/* 181 */     protected int getTableResource(String resKey) { return ((ICUResourceBundleReader.Table)this.value).getTableResource(resKey); }
/*     */     
/*     */     protected int getTableResource(int index) {
/* 184 */       return getContainerResource(index);
/*     */     }
/*     */     
/*     */     protected UResourceBundle handleGetImpl(String resKey, HashMap<String, String> table, UResourceBundle requested, int[] index, boolean[] isAlias)
/*     */     {
/* 189 */       int i = ((ICUResourceBundleReader.Table)this.value).findTableItem(resKey);
/* 190 */       if (index != null) {
/* 191 */         index[0] = i;
/*     */       }
/* 193 */       if (i < 0) {
/* 194 */         return null;
/*     */       }
/* 196 */       return createBundleObject(i, resKey, table, requested, isAlias);
/*     */     }
/*     */     
/*     */     protected UResourceBundle handleGetImpl(int index, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias) {
/* 200 */       String itemKey = ((ICUResourceBundleReader.Table)this.value).getKey(index);
/* 201 */       if (itemKey == null) {
/* 202 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 204 */       return createBundleObject(index, itemKey, table, requested, isAlias);
/*     */     }
/*     */     
/*     */     ResourceTable(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundleImpl container) {
/* 208 */       super(key, resPath, resource, container);
/* 209 */       this.value = reader.getTable(resource);
/* 210 */       createLookupCache();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUResourceBundleImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */