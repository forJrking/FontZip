/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class Region
/*     */   implements Comparable<Region>
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int UNDEFINED_NUMERIC_CODE = -1;
/*     */   private String id;
/*     */   private int code;
/*     */   private RegionType type;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static enum RegionType
/*     */   {
/*  77 */     UNKNOWN, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */     TERRITORY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */     WORLD, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */     CONTINENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     SUBCONTINENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     GROUPING, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     DEPRECATED;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private RegionType() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */   private static boolean hasData = false;
/* 133 */   private static boolean hasContainmentData = false;
/*     */   
/* 135 */   private static Map<String, Integer> regionIndexMap = null;
/* 136 */   private static Map<Integer, Integer> numericIndexMap = null;
/* 137 */   private static Map<String, String> territoryAliasMap = null;
/* 138 */   private static Map<String, Integer> numericCodeMap = null;
/* 139 */   private static Region[] regions = null;
/* 140 */   private static BitSet[] subRegionData = null;
/* 141 */   private static Integer[] containingRegionData = null;
/* 142 */   private static ArrayList<Set<Region>> availableRegions = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String UNKNOWN_REGION_ID = "ZZ";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String WORLD_ID = "001";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized void initRegionData()
/*     */   {
/* 165 */     if (hasData) {
/* 166 */       return;
/*     */     }
/*     */     
/* 169 */     territoryAliasMap = new HashMap();
/* 170 */     numericCodeMap = new HashMap();
/* 171 */     regionIndexMap = new HashMap();
/* 172 */     numericIndexMap = new HashMap();
/* 173 */     availableRegions = new ArrayList(RegionType.values().length);
/*     */     
/* 175 */     for (int i = 0; i < RegionType.values().length; i++) {
/* 176 */       availableRegions.add(null);
/*     */     }
/* 178 */     UResourceBundle regionCodes = null;
/* 179 */     UResourceBundle territoryAlias = null;
/* 180 */     UResourceBundle codeMappings = null;
/* 181 */     UResourceBundle worldContainment = null;
/* 182 */     UResourceBundle territoryContainment = null;
/* 183 */     UResourceBundle groupingContainment = null;
/* 184 */     UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "metadata", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */     
/*     */ 
/*     */ 
/* 188 */     regionCodes = rb.get("regionCodes");
/* 189 */     territoryAlias = rb.get("territoryAlias");
/*     */     
/* 191 */     UResourceBundle rb2 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */     
/*     */ 
/*     */ 
/* 195 */     codeMappings = rb2.get("codeMappings");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 200 */     territoryContainment = rb2.get("territoryContainment");
/* 201 */     worldContainment = territoryContainment.get("001");
/* 202 */     groupingContainment = territoryContainment.get("grouping");
/*     */     
/* 204 */     String[] continentsArr = worldContainment.getStringArray();
/* 205 */     List<String> continents = Arrays.asList(continentsArr);
/* 206 */     String[] groupingArr = groupingContainment.getStringArray();
/* 207 */     List<String> groupings = Arrays.asList(groupingArr);
/*     */     
/*     */ 
/*     */ 
/* 211 */     for (int i = 0; i < codeMappings.getSize(); i++) {
/* 212 */       UResourceBundle mapping = codeMappings.get(i);
/* 213 */       if (mapping.getType() == 8) {
/* 214 */         String[] codeStrings = mapping.getStringArray();
/* 215 */         if (!territoryAliasMap.containsKey(codeStrings[1])) {
/* 216 */           territoryAliasMap.put(codeStrings[1], codeStrings[0]);
/*     */         }
/* 218 */         territoryAliasMap.put(codeStrings[2], codeStrings[0]);
/* 219 */         numericCodeMap.put(codeStrings[0], Integer.valueOf(codeStrings[1]));
/*     */       }
/*     */     }
/*     */     
/* 223 */     for (int i = 0; i < territoryAlias.getSize(); i++) {
/* 224 */       UResourceBundle res = territoryAlias.get(i);
/* 225 */       String key = res.getKey();
/* 226 */       String value = res.getString();
/* 227 */       if (!territoryAliasMap.containsKey(key)) {
/* 228 */         territoryAliasMap.put(key, value);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 233 */     regions = new Region[regionCodes.getSize()];
/* 234 */     for (int i = 0; i < regions.length; i++) {
/* 235 */       regions[i] = new Region();
/* 236 */       String id = regionCodes.getString(i);
/* 237 */       regions[i].id = id;
/* 238 */       regionIndexMap.put(id, Integer.valueOf(i));
/*     */       
/* 240 */       if (id.matches("[0-9]{3}")) {
/* 241 */         regions[i].code = Integer.valueOf(id).intValue();
/* 242 */         numericIndexMap.put(Integer.valueOf(regions[i].code), Integer.valueOf(i));
/* 243 */       } else if (numericCodeMap.containsKey(id)) {
/* 244 */         regions[i].code = ((Integer)numericCodeMap.get(id)).intValue();
/* 245 */         if (!numericIndexMap.containsKey(Integer.valueOf(regions[i].code))) {
/* 246 */           numericIndexMap.put(Integer.valueOf(regions[i].code), Integer.valueOf(i));
/*     */         }
/*     */       } else {
/* 249 */         regions[i].code = -1;
/*     */       }
/*     */       
/* 252 */       if (territoryAliasMap.containsKey(id)) {
/* 253 */         regions[i].type = RegionType.DEPRECATED;
/* 254 */       } else if (id.equals("001")) {
/* 255 */         regions[i].type = RegionType.WORLD;
/* 256 */       } else if (id.equals("ZZ")) {
/* 257 */         regions[i].type = RegionType.UNKNOWN;
/* 258 */       } else if (continents.contains(id)) {
/* 259 */         regions[i].type = RegionType.CONTINENT;
/* 260 */       } else if (groupings.contains(id)) {
/* 261 */         regions[i].type = RegionType.GROUPING;
/* 262 */       } else if (id.matches("[0-9]{3}|QO")) {
/* 263 */         regions[i].type = RegionType.SUBCONTINENT;
/*     */       } else {
/* 265 */         regions[i].type = RegionType.TERRITORY;
/*     */       }
/*     */     }
/*     */     
/* 269 */     hasData = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized void initContainmentData()
/*     */   {
/* 287 */     if (hasContainmentData) {
/* 288 */       return;
/*     */     }
/*     */     
/* 291 */     initRegionData();
/* 292 */     subRegionData = new BitSet[regions.length];
/* 293 */     containingRegionData = new Integer[regions.length];
/* 294 */     for (int i = 0; i < regions.length; i++) {
/* 295 */       subRegionData[i] = new BitSet(regions.length);
/* 296 */       containingRegionData[i] = null;
/*     */     }
/* 298 */     UResourceBundle territoryContainment = null;
/*     */     
/* 300 */     UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */     
/*     */ 
/*     */ 
/* 304 */     territoryContainment = rb.get("territoryContainment");
/*     */     
/*     */ 
/*     */ 
/* 308 */     for (int i = 0; i < territoryContainment.getSize(); i++) {
/* 309 */       UResourceBundle mapping = territoryContainment.get(i);
/* 310 */       String parent = mapping.getKey();
/* 311 */       Integer parentRegionIndex = (Integer)regionIndexMap.get(parent);
/* 312 */       for (int j = 0; j < mapping.getSize(); j++) {
/* 313 */         String child = mapping.getString(j);
/* 314 */         Integer childRegionIndex = (Integer)regionIndexMap.get(child);
/* 315 */         if ((parentRegionIndex != null) && (childRegionIndex != null)) {
/* 316 */           subRegionData[parentRegionIndex.intValue()].set(childRegionIndex.intValue());
/*     */           
/*     */ 
/* 319 */           if (!regions[parentRegionIndex.intValue()].isOfType(RegionType.GROUPING)) {
/* 320 */             containingRegionData[childRegionIndex.intValue()] = parentRegionIndex;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 325 */     hasContainmentData = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Region get(String id)
/*     */   {
/* 340 */     if (id == null) {
/* 341 */       throw new NullPointerException();
/*     */     }
/* 343 */     String canonicalID = canonicalize(id);
/* 344 */     if ((canonicalID.equals("ZZ")) && (!id.equals("ZZ"))) {
/* 345 */       throw new IllegalArgumentException("Unknown region id: " + id);
/*     */     }
/*     */     
/* 348 */     return regions[((Integer)regionIndexMap.get(canonicalID)).intValue()];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Region get(int code)
/*     */   {
/* 361 */     Integer index = (Integer)numericIndexMap.get(Integer.valueOf(code));
/* 362 */     if (index != null) {
/* 363 */       Region r = regions[index.intValue()];
/*     */       
/*     */ 
/* 366 */       return get(r.id);
/*     */     }
/* 368 */     throw new IllegalArgumentException("Unknown region code: " + code);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static String canonicalize(String id)
/*     */   {
/* 384 */     initRegionData();
/* 385 */     String result = (String)territoryAliasMap.get(id);
/* 386 */     if ((result != null) && (regionIndexMap.containsKey(result))) {
/* 387 */       return result;
/*     */     }
/*     */     
/* 390 */     if (regionIndexMap.containsKey(id)) {
/* 391 */       return id;
/*     */     }
/* 393 */     return "ZZ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static boolean isCanonical(String id)
/*     */   {
/* 404 */     return canonicalize(id).equals(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Set<Region> getAvailable(RegionType type)
/*     */   {
/*     */     
/*     */     
/*     */ 
/* 418 */     if (availableRegions.get(type.ordinal()) == null) {
/* 419 */       Set<Region> result = new TreeSet();
/* 420 */       for (Region r : regions) {
/* 421 */         if (r.type == type) {
/* 422 */           result.add(r);
/*     */         }
/*     */       }
/* 425 */       availableRegions.set(type.ordinal(), Collections.unmodifiableSet(result));
/*     */     }
/* 427 */     return (Set)availableRegions.get(type.ordinal());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Region getContainingRegion()
/*     */   {
/* 441 */     initContainmentData();
/* 442 */     Integer index = (Integer)regionIndexMap.get(this.id);
/* 443 */     assert (index != null);
/* 444 */     if (containingRegionData[index.intValue()] == null) {
/* 445 */       return null;
/*     */     }
/* 447 */     return regions[containingRegionData[index.intValue()].intValue()];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Set<Region> getSubRegions()
/*     */   {
/* 466 */     initContainmentData();
/*     */     
/* 468 */     Set<Region> result = new TreeSet();
/* 469 */     Integer index = (Integer)regionIndexMap.get(this.id);
/* 470 */     BitSet contains = subRegionData[index.intValue()];
/* 471 */     for (int i = contains.nextSetBit(0); i >= 0; i = contains.nextSetBit(i + 1)) {
/* 472 */       result.add(regions[i]);
/*     */     }
/* 474 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Set<Region> getContainedTerritories()
/*     */   {
/* 490 */     initContainmentData();
/* 491 */     Set<Region> result = new TreeSet();
/* 492 */     Set<Region> subRegions = getSubRegions();
/* 493 */     Iterator<Region> it = subRegions.iterator();
/* 494 */     while (it.hasNext()) {
/* 495 */       Region r = (Region)it.next();
/* 496 */       if (r.isOfType(RegionType.TERRITORY)) {
/* 497 */         result.add(r);
/* 498 */       } else if ((r.isOfType(RegionType.CONTINENT)) || (r.isOfType(RegionType.SUBCONTINENT))) {
/* 499 */         result.addAll(r.getContainedTerritories());
/*     */       }
/*     */     }
/* 502 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public String toString()
/*     */   {
/* 514 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getNumericCode()
/*     */   {
/* 528 */     return this.code;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public RegionType getType()
/*     */   {
/* 540 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public boolean isOfType(RegionType type)
/*     */   {
/* 552 */     return this.type.equals(type);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int compareTo(Region other)
/*     */   {
/* 561 */     return this.id.compareTo(other.id);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Region.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */