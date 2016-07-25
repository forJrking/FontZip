/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class BidiLine
/*      */ {
/*      */   static void setTrailingWSStart(Bidi bidi)
/*      */   {
/*   85 */     byte[] dirProps = bidi.dirProps;
/*   86 */     byte[] levels = bidi.levels;
/*   87 */     int start = bidi.length;
/*   88 */     byte paraLevel = bidi.paraLevel;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   96 */     if (Bidi.NoContextRTL(dirProps[(start - 1)]) == 7) {
/*   97 */       bidi.trailingWSStart = start;
/*   98 */       return;
/*      */     }
/*      */     
/*      */ 
/*  102 */     while ((start > 0) && ((Bidi.DirPropFlagNC(dirProps[(start - 1)]) & Bidi.MASK_WS) != 0)) {
/*  103 */       start--;
/*      */     }
/*      */     
/*      */ 
/*  107 */     while ((start > 0) && (levels[(start - 1)] == paraLevel)) {
/*  108 */       start--;
/*      */     }
/*      */     
/*  111 */     bidi.trailingWSStart = start;
/*      */   }
/*      */   
/*      */ 
/*      */   static Bidi setLine(Bidi paraBidi, int start, int limit)
/*      */   {
/*  117 */     Bidi lineBidi = new Bidi();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  125 */     int length = lineBidi.length = lineBidi.originalLength = lineBidi.resultLength = limit - start;
/*      */     
/*      */ 
/*  128 */     lineBidi.text = new char[length];
/*  129 */     System.arraycopy(paraBidi.text, start, lineBidi.text, 0, length);
/*  130 */     lineBidi.paraLevel = paraBidi.GetParaLevelAt(start);
/*  131 */     lineBidi.paraCount = paraBidi.paraCount;
/*  132 */     lineBidi.runs = new BidiRun[0];
/*  133 */     lineBidi.reorderingMode = paraBidi.reorderingMode;
/*  134 */     lineBidi.reorderingOptions = paraBidi.reorderingOptions;
/*  135 */     if (paraBidi.controlCount > 0)
/*      */     {
/*  137 */       for (int j = start; j < limit; j++) {
/*  138 */         if (Bidi.IsBidiControlChar(paraBidi.text[j])) {
/*  139 */           lineBidi.controlCount += 1;
/*      */         }
/*      */       }
/*  142 */       lineBidi.resultLength -= lineBidi.controlCount;
/*      */     }
/*      */     
/*  145 */     lineBidi.getDirPropsMemory(length);
/*  146 */     lineBidi.dirProps = lineBidi.dirPropsMemory;
/*  147 */     System.arraycopy(paraBidi.dirProps, start, lineBidi.dirProps, 0, length);
/*      */     
/*      */ 
/*  150 */     lineBidi.getLevelsMemory(length);
/*  151 */     lineBidi.levels = lineBidi.levelsMemory;
/*  152 */     System.arraycopy(paraBidi.levels, start, lineBidi.levels, 0, length);
/*      */     
/*  154 */     lineBidi.runCount = -1;
/*      */     
/*  156 */     if (paraBidi.direction != 2)
/*      */     {
/*  158 */       lineBidi.direction = paraBidi.direction;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  165 */       if (paraBidi.trailingWSStart <= start) {
/*  166 */         lineBidi.trailingWSStart = 0;
/*  167 */       } else if (paraBidi.trailingWSStart < limit) {
/*  168 */         paraBidi.trailingWSStart -= start;
/*      */       } else {
/*  170 */         lineBidi.trailingWSStart = length;
/*      */       }
/*      */     } else {
/*  173 */       byte[] levels = lineBidi.levels;
/*      */       
/*      */ 
/*      */ 
/*  177 */       setTrailingWSStart(lineBidi);
/*  178 */       int trailingWSStart = lineBidi.trailingWSStart;
/*      */       
/*      */ 
/*  181 */       if (trailingWSStart == 0)
/*      */       {
/*  183 */         lineBidi.direction = ((byte)(lineBidi.paraLevel & 0x1));
/*      */       }
/*      */       else {
/*  186 */         byte level = (byte)(levels[0] & 0x1);
/*      */         
/*      */ 
/*      */ 
/*  190 */         if ((trailingWSStart < length) && ((lineBidi.paraLevel & 0x1) != level))
/*      */         {
/*      */ 
/*      */ 
/*  194 */           lineBidi.direction = 2;
/*      */         }
/*      */         else
/*      */         {
/*  198 */           for (int i = 1;; i++) {
/*  199 */             if (i == trailingWSStart)
/*      */             {
/*  201 */               lineBidi.direction = level;
/*  202 */               break; }
/*  203 */             if ((levels[i] & 0x1) != level) {
/*  204 */               lineBidi.direction = 2;
/*  205 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  211 */       switch (lineBidi.direction)
/*      */       {
/*      */       case 0: 
/*  214 */         lineBidi.paraLevel = ((byte)(lineBidi.paraLevel + 1 & 0xFFFFFFFE));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  219 */         lineBidi.trailingWSStart = 0;
/*  220 */         break;
/*      */       
/*      */       case 1: 
/*  223 */         Bidi tmp475_473 = lineBidi;tmp475_473.paraLevel = ((byte)(tmp475_473.paraLevel | 0x1));
/*      */         
/*      */ 
/*      */ 
/*  227 */         lineBidi.trailingWSStart = 0;
/*  228 */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  233 */     lineBidi.paraBidi = paraBidi;
/*  234 */     return lineBidi;
/*      */   }
/*      */   
/*      */ 
/*      */   static byte getLevelAt(Bidi bidi, int charIndex)
/*      */   {
/*  240 */     if ((bidi.direction != 2) || (charIndex >= bidi.trailingWSStart)) {
/*  241 */       return bidi.GetParaLevelAt(charIndex);
/*      */     }
/*  243 */     return bidi.levels[charIndex];
/*      */   }
/*      */   
/*      */ 
/*      */   static byte[] getLevels(Bidi bidi)
/*      */   {
/*  249 */     int start = bidi.trailingWSStart;
/*  250 */     int length = bidi.length;
/*      */     
/*  252 */     if (start != length)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  263 */       Arrays.fill(bidi.levels, start, length, bidi.paraLevel);
/*      */       
/*      */ 
/*  266 */       bidi.trailingWSStart = length;
/*      */     }
/*  268 */     if (length < bidi.levels.length) {
/*  269 */       byte[] levels = new byte[length];
/*  270 */       System.arraycopy(bidi.levels, 0, levels, 0, length);
/*  271 */       return levels;
/*      */     }
/*  273 */     return bidi.levels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static BidiRun getLogicalRun(Bidi bidi, int logicalPosition)
/*      */   {
/*  281 */     BidiRun newRun = new BidiRun();
/*  282 */     getRuns(bidi);
/*  283 */     int runCount = bidi.runCount;
/*  284 */     int visualStart = 0;int logicalLimit = 0;
/*  285 */     BidiRun iRun = bidi.runs[0];
/*      */     
/*  287 */     for (int i = 0; i < runCount; i++) {
/*  288 */       iRun = bidi.runs[i];
/*  289 */       logicalLimit = iRun.start + iRun.limit - visualStart;
/*  290 */       if ((logicalPosition >= iRun.start) && (logicalPosition < logicalLimit)) {
/*      */         break;
/*      */       }
/*      */       
/*  294 */       visualStart = iRun.limit;
/*      */     }
/*  296 */     newRun.start = iRun.start;
/*  297 */     newRun.limit = logicalLimit;
/*  298 */     newRun.level = iRun.level;
/*  299 */     return newRun;
/*      */   }
/*      */   
/*      */   static BidiRun getVisualRun(Bidi bidi, int runIndex)
/*      */   {
/*  304 */     int start = bidi.runs[runIndex].start;
/*      */     
/*  306 */     byte level = bidi.runs[runIndex].level;
/*      */     int limit;
/*  308 */     int limit; if (runIndex > 0) {
/*  309 */       limit = start + bidi.runs[runIndex].limit - bidi.runs[(runIndex - 1)].limit;
/*      */     }
/*      */     else
/*      */     {
/*  313 */       limit = start + bidi.runs[0].limit;
/*      */     }
/*  315 */     return new BidiRun(start, limit, level);
/*      */   }
/*      */   
/*      */ 
/*      */   static void getSingleRun(Bidi bidi, byte level)
/*      */   {
/*  321 */     bidi.runs = bidi.simpleRuns;
/*  322 */     bidi.runCount = 1;
/*      */     
/*      */ 
/*  325 */     bidi.runs[0] = new BidiRun(0, bidi.length, level);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void reorderLine(Bidi bidi, byte minLevel, byte maxLevel)
/*      */   {
/*  364 */     if (maxLevel <= (minLevel | 0x1)) {
/*  365 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  378 */     minLevel = (byte)(minLevel + 1);
/*      */     
/*  380 */     BidiRun[] runs = bidi.runs;
/*  381 */     byte[] levels = bidi.levels;
/*  382 */     int runCount = bidi.runCount;
/*      */     
/*      */ 
/*  385 */     if (bidi.trailingWSStart < bidi.length) {
/*  386 */       runCount--;
/*      */     }
/*      */     
/*  389 */     maxLevel = (byte)(maxLevel - 1); if (maxLevel >= minLevel) {
/*  390 */       int firstRun = 0;
/*      */       
/*      */ 
/*      */ 
/*      */       for (;;)
/*      */       {
/*  396 */         if ((firstRun < runCount) && (levels[runs[firstRun].start] < maxLevel)) {
/*  397 */           firstRun++;
/*      */         } else {
/*  399 */           if (firstRun >= runCount) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*  404 */           int limitRun = firstRun;
/*  405 */           do { limitRun++; } while ((limitRun < runCount) && (levels[runs[limitRun].start] >= maxLevel));
/*      */           
/*      */ 
/*  408 */           int endRun = limitRun - 1;
/*  409 */           while (firstRun < endRun) {
/*  410 */             BidiRun tempRun = runs[firstRun];
/*  411 */             runs[firstRun] = runs[endRun];
/*  412 */             runs[endRun] = tempRun;
/*  413 */             firstRun++;
/*  414 */             endRun--;
/*      */           }
/*      */           
/*  417 */           if (limitRun == runCount) {
/*      */             break;
/*      */           }
/*  420 */           firstRun = limitRun + 1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  426 */     if ((minLevel & 0x1) == 0) {
/*  427 */       int firstRun = 0;
/*      */       
/*      */ 
/*  430 */       if (bidi.trailingWSStart == bidi.length) {
/*  431 */         runCount--;
/*      */       }
/*      */       
/*      */ 
/*  435 */       while (firstRun < runCount) {
/*  436 */         BidiRun tempRun = runs[firstRun];
/*  437 */         runs[firstRun] = runs[runCount];
/*  438 */         runs[runCount] = tempRun;
/*  439 */         firstRun++;
/*  440 */         runCount--;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static int getRunFromLogicalIndex(Bidi bidi, int logicalIndex)
/*      */   {
/*  448 */     BidiRun[] runs = bidi.runs;
/*  449 */     int runCount = bidi.runCount;int visualStart = 0;
/*      */     
/*  451 */     for (int i = 0; i < runCount; i++) {
/*  452 */       int length = runs[i].limit - visualStart;
/*  453 */       int logicalStart = runs[i].start;
/*  454 */       if ((logicalIndex >= logicalStart) && (logicalIndex < logicalStart + length)) {
/*  455 */         return i;
/*      */       }
/*  457 */       visualStart += length;
/*      */     }
/*      */     
/*      */ 
/*  461 */     throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void getRuns(Bidi bidi)
/*      */   {
/*  481 */     if (bidi.runCount >= 0) {
/*  482 */       return;
/*      */     }
/*  484 */     if (bidi.direction != 2)
/*      */     {
/*      */ 
/*  487 */       getSingleRun(bidi, bidi.paraLevel);
/*      */     }
/*      */     else {
/*  490 */       int length = bidi.length;
/*  491 */       byte[] levels = bidi.levels;
/*      */       
/*  493 */       byte level = 126;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  505 */       int limit = bidi.trailingWSStart;
/*      */       
/*  507 */       int runCount = 0;
/*  508 */       for (int i = 0; i < limit; i++)
/*      */       {
/*  510 */         if (levels[i] != level) {
/*  511 */           runCount++;
/*  512 */           level = levels[i];
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */       if ((runCount == 1) && (limit == length))
/*      */       {
/*  522 */         getSingleRun(bidi, levels[0]);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  527 */         byte minLevel = 62;
/*  528 */         byte maxLevel = 0;
/*      */         
/*      */ 
/*  531 */         if (limit < length) {
/*  532 */           runCount++;
/*      */         }
/*      */         
/*      */ 
/*  536 */         bidi.getRunsMemory(runCount);
/*  537 */         BidiRun[] runs = bidi.runsMemory;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  545 */         int runIndex = 0;
/*      */         
/*      */ 
/*  548 */         i = 0;
/*      */         do
/*      */         {
/*  551 */           int start = i;
/*  552 */           level = levels[i];
/*  553 */           if (level < minLevel) {
/*  554 */             minLevel = level;
/*      */           }
/*  556 */           if (level > maxLevel) {
/*  557 */             maxLevel = level;
/*      */           }
/*      */           do
/*      */           {
/*  561 */             i++; } while ((i < limit) && (levels[i] == level));
/*      */           
/*      */ 
/*  564 */           runs[runIndex] = new BidiRun(start, i - start, level);
/*  565 */           runIndex++;
/*  566 */         } while (i < limit);
/*      */         
/*  568 */         if (limit < length)
/*      */         {
/*  570 */           runs[runIndex] = new BidiRun(limit, length - limit, bidi.paraLevel);
/*      */           
/*      */ 
/*  573 */           if (bidi.paraLevel < minLevel) {
/*  574 */             minLevel = bidi.paraLevel;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  579 */         bidi.runs = runs;
/*  580 */         bidi.runCount = runCount;
/*      */         
/*  582 */         reorderLine(bidi, minLevel, maxLevel);
/*      */         
/*      */ 
/*      */ 
/*  586 */         limit = 0;
/*  587 */         for (i = 0; i < runCount; i++) {
/*  588 */           runs[i].level = levels[runs[i].start];
/*  589 */           limit = runs[i].limit += limit;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  596 */         if (runIndex < runCount) {
/*  597 */           int trailingRun = (bidi.paraLevel & 0x1) != 0 ? 0 : runIndex;
/*  598 */           runs[trailingRun].level = bidi.paraLevel;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  604 */     if (bidi.insertPoints.size > 0)
/*      */     {
/*      */ 
/*  607 */       for (int ip = 0; ip < bidi.insertPoints.size; ip++) {
/*  608 */         Bidi.Point point = bidi.insertPoints.points[ip];
/*  609 */         int runIndex = getRunFromLogicalIndex(bidi, point.pos);
/*  610 */         bidi.runs[runIndex].insertRemove |= point.flag;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  615 */     if (bidi.controlCount > 0)
/*      */     {
/*      */ 
/*  618 */       for (int ic = 0; ic < bidi.length; ic++) {
/*  619 */         char c = bidi.text[ic];
/*  620 */         if (Bidi.IsBidiControlChar(c)) {
/*  621 */           int runIndex = getRunFromLogicalIndex(bidi, ic);
/*  622 */           bidi.runs[runIndex].insertRemove -= 1;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static int[] prepareReorder(byte[] levels, byte[] pMinLevel, byte[] pMaxLevel)
/*      */   {
/*  633 */     if ((levels == null) || (levels.length <= 0)) {
/*  634 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  638 */     byte minLevel = 62;
/*  639 */     byte maxLevel = 0;
/*  640 */     for (int start = levels.length; start > 0;) {
/*  641 */       byte level = levels[(--start)];
/*  642 */       if (level > 62) {
/*  643 */         return null;
/*      */       }
/*  645 */       if (level < minLevel) {
/*  646 */         minLevel = level;
/*      */       }
/*  648 */       if (level > maxLevel) {
/*  649 */         maxLevel = level;
/*      */       }
/*      */     }
/*  652 */     pMinLevel[0] = minLevel;
/*  653 */     pMaxLevel[0] = maxLevel;
/*      */     
/*      */ 
/*  656 */     int[] indexMap = new int[levels.length];
/*  657 */     for (start = levels.length; start > 0;) {
/*  658 */       start--;
/*  659 */       indexMap[start] = start;
/*      */     }
/*      */     
/*  662 */     return indexMap;
/*      */   }
/*      */   
/*      */   static int[] reorderLogical(byte[] levels)
/*      */   {
/*  667 */     byte[] aMinLevel = new byte[1];
/*  668 */     byte[] aMaxLevel = new byte[1];
/*      */     
/*      */ 
/*  671 */     int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
/*  672 */     if (indexMap == null) {
/*  673 */       return null;
/*      */     }
/*      */     
/*  676 */     byte minLevel = aMinLevel[0];
/*  677 */     byte maxLevel = aMaxLevel[0];
/*      */     
/*      */ 
/*  680 */     if ((minLevel == maxLevel) && ((minLevel & 0x1) == 0)) {
/*  681 */       return indexMap;
/*      */     }
/*      */     
/*      */ 
/*  685 */     minLevel = (byte)(minLevel | 0x1);
/*      */     
/*      */     do
/*      */     {
/*  689 */       int start = 0;
/*      */       
/*      */ 
/*      */ 
/*      */       for (;;)
/*      */       {
/*  695 */         if ((start < levels.length) && (levels[start] < maxLevel)) {
/*  696 */           start++;
/*      */         } else {
/*  698 */           if (start >= levels.length) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*  703 */           int limit = start; do { limit++; } while ((limit < levels.length) && (levels[limit] >= maxLevel));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  716 */           int sumOfSosEos = start + limit - 1;
/*      */           
/*      */           do
/*      */           {
/*  720 */             indexMap[start] = (sumOfSosEos - indexMap[start]);
/*  721 */             start++; } while (start < limit);
/*      */           
/*      */ 
/*  724 */           if (limit == levels.length) {
/*      */             break;
/*      */           }
/*  727 */           start = limit + 1;
/*      */         }
/*      */       }
/*  730 */       maxLevel = (byte)(maxLevel - 1); } while (maxLevel >= minLevel);
/*  731 */     return indexMap;
/*      */   }
/*      */   
/*      */   static int[] reorderVisual(byte[] levels)
/*      */   {
/*  736 */     byte[] aMinLevel = new byte[1];
/*  737 */     byte[] aMaxLevel = new byte[1];
/*      */     
/*      */ 
/*      */ 
/*  741 */     int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
/*  742 */     if (indexMap == null) {
/*  743 */       return null;
/*      */     }
/*      */     
/*  746 */     byte minLevel = aMinLevel[0];
/*  747 */     byte maxLevel = aMaxLevel[0];
/*      */     
/*      */ 
/*  750 */     if ((minLevel == maxLevel) && ((minLevel & 0x1) == 0)) {
/*  751 */       return indexMap;
/*      */     }
/*      */     
/*      */ 
/*  755 */     minLevel = (byte)(minLevel | 0x1);
/*      */     
/*      */     do
/*      */     {
/*  759 */       int start = 0;
/*      */       
/*      */ 
/*      */ 
/*      */       for (;;)
/*      */       {
/*  765 */         if ((start < levels.length) && (levels[start] < maxLevel)) {
/*  766 */           start++;
/*      */         } else {
/*  768 */           if (start >= levels.length) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*  773 */           int limit = start; do { limit++; } while ((limit < levels.length) && (levels[limit] >= maxLevel));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  781 */           int end = limit - 1;
/*  782 */           while (start < end) {
/*  783 */             int temp = indexMap[start];
/*  784 */             indexMap[start] = indexMap[end];
/*  785 */             indexMap[end] = temp;
/*      */             
/*  787 */             start++;
/*  788 */             end--;
/*      */           }
/*      */           
/*  791 */           if (limit == levels.length) {
/*      */             break;
/*      */           }
/*  794 */           start = limit + 1;
/*      */         }
/*      */       }
/*  797 */       maxLevel = (byte)(maxLevel - 1); } while (maxLevel >= minLevel);
/*      */     
/*  799 */     return indexMap;
/*      */   }
/*      */   
/*      */   static int getVisualIndex(Bidi bidi, int logicalIndex)
/*      */   {
/*  804 */     int visualIndex = -1;
/*      */     
/*      */ 
/*  807 */     switch (bidi.direction) {
/*      */     case 0: 
/*  809 */       visualIndex = logicalIndex;
/*  810 */       break;
/*      */     case 1: 
/*  812 */       visualIndex = bidi.length - logicalIndex - 1;
/*  813 */       break;
/*      */     default: 
/*  815 */       getRuns(bidi);
/*  816 */       BidiRun[] runs = bidi.runs;
/*  817 */       int visualStart = 0;
/*      */       
/*      */ 
/*  820 */       for (int i = 0; i < bidi.runCount; i++) {
/*  821 */         int length = runs[i].limit - visualStart;
/*  822 */         int offset = logicalIndex - runs[i].start;
/*  823 */         if ((offset >= 0) && (offset < length)) {
/*  824 */           if (runs[i].isEvenRun())
/*      */           {
/*  826 */             visualIndex = visualStart + offset; break;
/*      */           }
/*      */           
/*  829 */           visualIndex = visualStart + length - offset - 1;
/*      */           
/*  831 */           break;
/*      */         }
/*  833 */         visualStart += length;
/*      */       }
/*  835 */       if (i >= bidi.runCount) {
/*  836 */         return -1;
/*      */       }
/*      */       break;
/*      */     }
/*  840 */     if (bidi.insertPoints.size > 0)
/*      */     {
/*  842 */       BidiRun[] runs = bidi.runs;
/*      */       
/*  844 */       int visualStart = 0;int markFound = 0;
/*  845 */       int length; for (int i = 0;; visualStart += length) {
/*  846 */         length = runs[i].limit - visualStart;
/*  847 */         int insertRemove = runs[i].insertRemove;
/*  848 */         if ((insertRemove & 0x5) > 0) {
/*  849 */           markFound++;
/*      */         }
/*      */         
/*  852 */         if (visualIndex < runs[i].limit) {
/*  853 */           return visualIndex + markFound;
/*      */         }
/*  855 */         if ((insertRemove & 0xA) > 0) {
/*  856 */           markFound++;
/*      */         }
/*  845 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  860 */     if (bidi.controlCount > 0)
/*      */     {
/*  862 */       BidiRun[] runs = bidi.runs;
/*      */       
/*  864 */       int visualStart = 0;int controlFound = 0;
/*  865 */       char uchar = bidi.text[logicalIndex];
/*      */       
/*  867 */       if (Bidi.IsBidiControlChar(uchar)) {
/*  868 */         return -1;
/*      */       }
/*      */       int length;
/*  871 */       for (int i = 0;; visualStart += length) {
/*  872 */         length = runs[i].limit - visualStart;
/*  873 */         int insertRemove = runs[i].insertRemove;
/*      */         
/*  875 */         if (visualIndex >= runs[i].limit) {
/*  876 */           controlFound -= insertRemove;
/*      */         }
/*      */         else
/*      */         {
/*  880 */           if (insertRemove == 0)
/*  881 */             return visualIndex - controlFound;
/*      */           int limit;
/*  883 */           int start; int limit; if (runs[i].isEvenRun())
/*      */           {
/*  885 */             int start = runs[i].start;
/*  886 */             limit = logicalIndex;
/*      */           }
/*      */           else {
/*  889 */             start = logicalIndex + 1;
/*  890 */             limit = runs[i].start + length;
/*      */           }
/*  892 */           for (int j = start; j < limit; j++) {
/*  893 */             uchar = bidi.text[j];
/*  894 */             if (Bidi.IsBidiControlChar(uchar)) {
/*  895 */               controlFound++;
/*      */             }
/*      */           }
/*  898 */           return visualIndex - controlFound;
/*      */         }
/*  871 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  902 */     return visualIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static int getLogicalIndex(Bidi bidi, int visualIndex)
/*      */   {
/*  910 */     BidiRun[] runs = bidi.runs;
/*  911 */     int runCount = bidi.runCount;
/*  912 */     if (bidi.insertPoints.size > 0)
/*      */     {
/*  914 */       int markFound = 0;
/*  915 */       int visualStart = 0;
/*      */       int length;
/*  917 */       for (int i = 0;; visualStart += length) {
/*  918 */         length = runs[i].limit - visualStart;
/*  919 */         int insertRemove = runs[i].insertRemove;
/*  920 */         if ((insertRemove & 0x5) > 0) {
/*  921 */           if (visualIndex <= visualStart + markFound) {
/*  922 */             return -1;
/*      */           }
/*  924 */           markFound++;
/*      */         }
/*      */         
/*  927 */         if (visualIndex < runs[i].limit + markFound) {
/*  928 */           visualIndex -= markFound;
/*  929 */           break;
/*      */         }
/*  931 */         if ((insertRemove & 0xA) > 0) {
/*  932 */           if (visualIndex == visualStart + length + markFound) {
/*  933 */             return -1;
/*      */           }
/*  935 */           markFound++;
/*      */         }
/*  917 */         i++;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  939 */     else if (bidi.controlCount > 0)
/*      */     {
/*  941 */       int controlFound = 0;
/*  942 */       int visualStart = 0;
/*      */       
/*      */       int length;
/*      */       
/*  946 */       for (int i = 0;; visualStart += length) {
/*  947 */         length = runs[i].limit - visualStart;
/*  948 */         int insertRemove = runs[i].insertRemove;
/*      */         
/*  950 */         if (visualIndex >= runs[i].limit - controlFound + insertRemove) {
/*  951 */           controlFound -= insertRemove;
/*      */         }
/*      */         else
/*      */         {
/*  955 */           if (insertRemove == 0) {
/*  956 */             visualIndex += controlFound;
/*  957 */             break;
/*      */           }
/*      */           
/*  960 */           int logicalStart = runs[i].start;
/*  961 */           boolean evenRun = runs[i].isEvenRun();
/*  962 */           int logicalEnd = logicalStart + length - 1;
/*  963 */           for (int j = 0; j < length; j++) {
/*  964 */             int k = evenRun ? logicalStart + j : logicalEnd - j;
/*  965 */             char uchar = bidi.text[k];
/*  966 */             if (Bidi.IsBidiControlChar(uchar)) {
/*  967 */               controlFound++;
/*      */             }
/*  969 */             if (visualIndex + controlFound == visualStart + j) {
/*      */               break;
/*      */             }
/*      */           }
/*  973 */           visualIndex += controlFound;
/*  974 */           break;
/*      */         }
/*  946 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  978 */     if (runCount <= 10)
/*      */     {
/*  980 */       for (int i = 0; visualIndex >= runs[i].limit; i++) {}
/*      */     }
/*      */     
/*  983 */     int begin = 0;int limit = runCount;
/*      */     int i;
/*      */     for (;;)
/*      */     {
/*  987 */       i = (begin + limit) / 2;
/*  988 */       if (visualIndex >= runs[i].limit) {
/*  989 */         begin = i + 1;
/*  990 */       } else { if ((i == 0) || (visualIndex >= runs[(i - 1)].limit)) {
/*      */           break;
/*      */         }
/*  993 */         limit = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  998 */     int start = runs[i].start;
/*  999 */     if (runs[i].isEvenRun())
/*      */     {
/*      */ 
/* 1002 */       if (i > 0) {
/* 1003 */         visualIndex -= runs[(i - 1)].limit;
/*      */       }
/* 1005 */       return start + visualIndex;
/*      */     }
/*      */     
/* 1008 */     return start + runs[i].limit - visualIndex - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static int[] getLogicalMap(Bidi bidi)
/*      */   {
/* 1015 */     BidiRun[] runs = bidi.runs;
/*      */     
/* 1017 */     int[] indexMap = new int[bidi.length];
/* 1018 */     if (bidi.length > bidi.resultLength) {
/* 1019 */       Arrays.fill(indexMap, -1);
/*      */     }
/*      */     
/* 1022 */     int visualStart = 0;
/* 1023 */     for (int j = 0; j < bidi.runCount; j++) {
/* 1024 */       int logicalStart = runs[j].start;
/* 1025 */       int visualLimit = runs[j].limit;
/* 1026 */       if (runs[j].isEvenRun()) {
/*      */         do {
/* 1028 */           indexMap[(logicalStart++)] = (visualStart++);
/* 1029 */         } while (visualStart < visualLimit);
/*      */       } else {
/* 1031 */         logicalStart += visualLimit - visualStart;
/*      */         do {
/* 1033 */           indexMap[(--logicalStart)] = (visualStart++);
/* 1034 */         } while (visualStart < visualLimit);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1039 */     if (bidi.insertPoints.size > 0) {
/* 1040 */       int markFound = 0;int runCount = bidi.runCount;
/*      */       
/* 1042 */       runs = bidi.runs;
/* 1043 */       visualStart = 0;
/*      */       int length;
/* 1045 */       for (int i = 0; i < runCount; visualStart += length) {
/* 1046 */         length = runs[i].limit - visualStart;
/* 1047 */         int insertRemove = runs[i].insertRemove;
/* 1048 */         if ((insertRemove & 0x5) > 0) {
/* 1049 */           markFound++;
/*      */         }
/* 1051 */         if (markFound > 0) {
/* 1052 */           int logicalStart = runs[i].start;
/* 1053 */           int logicalLimit = logicalStart + length;
/* 1054 */           for (int j = logicalStart; j < logicalLimit; j++) {
/* 1055 */             indexMap[j] += markFound;
/*      */           }
/*      */         }
/* 1058 */         if ((insertRemove & 0xA) > 0) {
/* 1059 */           markFound++;
/*      */         }
/* 1045 */         i++;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 1063 */     else if (bidi.controlCount > 0) {
/* 1064 */       int controlFound = 0;int runCount = bidi.runCount;
/*      */       
/*      */ 
/*      */ 
/* 1068 */       runs = bidi.runs;
/* 1069 */       visualStart = 0;
/*      */       int length;
/* 1071 */       for (int i = 0; i < runCount; visualStart += length) {
/* 1072 */         length = runs[i].limit - visualStart;
/* 1073 */         int insertRemove = runs[i].insertRemove;
/*      */         
/* 1075 */         if (controlFound - insertRemove != 0)
/*      */         {
/*      */ 
/* 1078 */           int logicalStart = runs[i].start;
/* 1079 */           boolean evenRun = runs[i].isEvenRun();
/* 1080 */           int logicalLimit = logicalStart + length;
/*      */           
/* 1082 */           if (insertRemove == 0) {
/* 1083 */             for (int j = logicalStart; j < logicalLimit; j++) {
/* 1084 */               indexMap[j] -= controlFound;
/*      */             }
/*      */           }
/*      */           
/* 1088 */           for (int j = 0; j < length; j++) {
/* 1089 */             int k = evenRun ? logicalStart + j : logicalLimit - j - 1;
/* 1090 */             char uchar = bidi.text[k];
/* 1091 */             if (Bidi.IsBidiControlChar(uchar)) {
/* 1092 */               controlFound++;
/* 1093 */               indexMap[k] = -1;
/*      */             }
/*      */             else {
/* 1096 */               indexMap[k] -= controlFound;
/*      */             }
/*      */           }
/*      */         }
/* 1071 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1100 */     return indexMap;
/*      */   }
/*      */   
/*      */ 
/*      */   static int[] getVisualMap(Bidi bidi)
/*      */   {
/* 1106 */     BidiRun[] runs = bidi.runs;
/*      */     
/* 1108 */     int allocLength = bidi.length > bidi.resultLength ? bidi.length : bidi.resultLength;
/*      */     
/* 1110 */     int[] indexMap = new int[allocLength];
/*      */     
/* 1112 */     int visualStart = 0;
/* 1113 */     int idx = 0;
/* 1114 */     for (int j = 0; j < bidi.runCount; j++) {
/* 1115 */       int logicalStart = runs[j].start;
/* 1116 */       int visualLimit = runs[j].limit;
/* 1117 */       if (runs[j].isEvenRun()) {
/*      */         do {
/* 1119 */           indexMap[(idx++)] = (logicalStart++);
/* 1120 */           visualStart++; } while (visualStart < visualLimit);
/*      */       } else {
/* 1122 */         logicalStart += visualLimit - visualStart;
/*      */         do {
/* 1124 */           indexMap[(idx++)] = (--logicalStart);
/* 1125 */           visualStart++; } while (visualStart < visualLimit);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1130 */     if (bidi.insertPoints.size > 0) {
/* 1131 */       int markFound = 0;int runCount = bidi.runCount;
/*      */       
/* 1133 */       runs = bidi.runs;
/*      */       
/* 1135 */       for (int i = 0; i < runCount; i++) {
/* 1136 */         int insertRemove = runs[i].insertRemove;
/* 1137 */         if ((insertRemove & 0x5) > 0) {
/* 1138 */           markFound++;
/*      */         }
/* 1140 */         if ((insertRemove & 0xA) > 0) {
/* 1141 */           markFound++;
/*      */         }
/*      */       }
/*      */       
/* 1145 */       int k = bidi.resultLength;
/* 1146 */       for (i = runCount - 1; (i >= 0) && (markFound > 0); i--) {
/* 1147 */         int insertRemove = runs[i].insertRemove;
/* 1148 */         if ((insertRemove & 0xA) > 0) {
/* 1149 */           indexMap[(--k)] = -1;
/* 1150 */           markFound--;
/*      */         }
/* 1152 */         visualStart = i > 0 ? runs[(i - 1)].limit : 0;
/* 1153 */         for (int j = runs[i].limit - 1; (j >= visualStart) && (markFound > 0); j--) {
/* 1154 */           indexMap[(--k)] = indexMap[j];
/*      */         }
/* 1156 */         if ((insertRemove & 0x5) > 0) {
/* 1157 */           indexMap[(--k)] = -1;
/* 1158 */           markFound--;
/*      */         }
/*      */       }
/*      */     }
/* 1162 */     else if (bidi.controlCount > 0) {
/* 1163 */       int runCount = bidi.runCount;
/*      */       
/*      */ 
/*      */ 
/* 1167 */       runs = bidi.runs;
/* 1168 */       visualStart = 0;
/*      */       
/* 1170 */       int k = 0;
/* 1171 */       int length; for (int i = 0; i < runCount; visualStart += length) {
/* 1172 */         length = runs[i].limit - visualStart;
/* 1173 */         int insertRemove = runs[i].insertRemove;
/*      */         
/* 1175 */         if ((insertRemove == 0) && (k == visualStart)) {
/* 1176 */           k += length;
/*      */         }
/*      */         else
/*      */         {
/* 1180 */           if (insertRemove == 0) {
/* 1181 */             int visualLimit = runs[i].limit;
/* 1182 */             for (int j = visualStart; j < visualLimit; j++) {
/* 1183 */               indexMap[(k++)] = indexMap[j];
/*      */             }
/*      */           }
/*      */           
/* 1187 */           int logicalStart = runs[i].start;
/* 1188 */           boolean evenRun = runs[i].isEvenRun();
/* 1189 */           int logicalEnd = logicalStart + length - 1;
/* 1190 */           for (int j = 0; j < length; j++) {
/* 1191 */             int m = evenRun ? logicalStart + j : logicalEnd - j;
/* 1192 */             char uchar = bidi.text[m];
/* 1193 */             if (!Bidi.IsBidiControlChar(uchar)) {
/* 1194 */               indexMap[(k++)] = m;
/*      */             }
/*      */           }
/*      */         }
/* 1171 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1199 */     if (allocLength == bidi.resultLength) {
/* 1200 */       return indexMap;
/*      */     }
/* 1202 */     int[] newMap = new int[bidi.resultLength];
/* 1203 */     System.arraycopy(indexMap, 0, newMap, 0, bidi.resultLength);
/* 1204 */     return newMap;
/*      */   }
/*      */   
/*      */   static int[] invertMap(int[] srcMap)
/*      */   {
/* 1209 */     int srcLength = srcMap.length;
/* 1210 */     int destLength = -1;int count = 0;
/*      */     
/*      */ 
/* 1213 */     for (int i = 0; i < srcLength; i++) {
/* 1214 */       int srcEntry = srcMap[i];
/* 1215 */       if (srcEntry > destLength) {
/* 1216 */         destLength = srcEntry;
/*      */       }
/* 1218 */       if (srcEntry >= 0) {
/* 1219 */         count++;
/*      */       }
/*      */     }
/* 1222 */     destLength++;
/* 1223 */     int[] destMap = new int[destLength];
/* 1224 */     if (count < destLength)
/*      */     {
/* 1226 */       Arrays.fill(destMap, -1);
/*      */     }
/* 1228 */     for (i = 0; i < srcLength; i++) {
/* 1229 */       int srcEntry = srcMap[i];
/* 1230 */       if (srcEntry >= 0) {
/* 1231 */         destMap[srcEntry] = i;
/*      */       }
/*      */     }
/* 1234 */     return destMap;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BidiLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */