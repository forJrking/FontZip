/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.Grego;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.StringTokenizer;
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
/*      */ public class VTimeZone
/*      */   extends BasicTimeZone
/*      */ {
/*      */   private static final long serialVersionUID = -6851467294127795902L;
/*      */   private BasicTimeZone tz;
/*      */   private List<String> vtzlines;
/*      */   
/*      */   public static VTimeZone create(String tzid)
/*      */   {
/*   50 */     VTimeZone vtz = new VTimeZone();
/*   51 */     vtz.tz = ((BasicTimeZone)TimeZone.getTimeZone(tzid, 0));
/*   52 */     vtz.olsonzid = vtz.tz.getID();
/*   53 */     vtz.setID(tzid);
/*      */     
/*   55 */     return vtz;
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
/*      */   public static VTimeZone create(Reader reader)
/*      */   {
/*   68 */     VTimeZone vtz = new VTimeZone();
/*   69 */     if (vtz.load(reader)) {
/*   70 */       return vtz;
/*      */     }
/*   72 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds)
/*      */   {
/*   81 */     return this.tz.getOffset(era, year, month, day, dayOfWeek, milliseconds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getOffset(long date, boolean local, int[] offsets)
/*      */   {
/*   89 */     this.tz.getOffset(date, local, offsets);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets)
/*      */   {
/*   99 */     this.tz.getOffsetFromLocal(date, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRawOffset()
/*      */   {
/*  107 */     return this.tz.getRawOffset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean inDaylightTime(Date date)
/*      */   {
/*  115 */     return this.tz.inDaylightTime(date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRawOffset(int offsetMillis)
/*      */   {
/*  123 */     this.tz.setRawOffset(offsetMillis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean useDaylightTime()
/*      */   {
/*  131 */     return this.tz.useDaylightTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasSameRules(TimeZone other)
/*      */   {
/*  139 */     return this.tz.hasSameRules(other);
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
/*      */   public String getTZURL()
/*      */   {
/*  152 */     return this.tzurl;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTZURL(String url)
/*      */   {
/*  163 */     this.tzurl = url;
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
/*      */   public Date getLastModified()
/*      */   {
/*  176 */     return this.lastmod;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLastModified(Date date)
/*      */   {
/*  187 */     this.lastmod = date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void write(Writer writer)
/*      */     throws IOException
/*      */   {
/*  199 */     BufferedWriter bw = new BufferedWriter(writer);
/*  200 */     if (this.vtzlines != null) {
/*  201 */       for (String line : this.vtzlines) {
/*  202 */         if (line.startsWith("TZURL:")) {
/*  203 */           if (this.tzurl != null) {
/*  204 */             bw.write("TZURL");
/*  205 */             bw.write(":");
/*  206 */             bw.write(this.tzurl);
/*  207 */             bw.write("\r\n");
/*      */           }
/*  209 */         } else if (line.startsWith("LAST-MODIFIED:")) {
/*  210 */           if (this.lastmod != null) {
/*  211 */             bw.write("LAST-MODIFIED");
/*  212 */             bw.write(":");
/*  213 */             bw.write(getUTCDateTimeString(this.lastmod.getTime()));
/*  214 */             bw.write("\r\n");
/*      */           }
/*      */         } else {
/*  217 */           bw.write(line);
/*  218 */           bw.write("\r\n");
/*      */         }
/*      */       }
/*  221 */       bw.flush();
/*      */     } else {
/*  223 */       String[] customProperties = null;
/*  224 */       if ((this.olsonzid != null) && (ICU_TZVERSION != null)) {
/*  225 */         customProperties = new String[1];
/*  226 */         customProperties[0] = ("X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "]");
/*      */       }
/*  228 */       writeZone(writer, this.tz, customProperties);
/*      */     }
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
/*      */   public void write(Writer writer, long start)
/*      */     throws IOException
/*      */   {
/*  245 */     TimeZoneRule[] rules = this.tz.getTimeZoneRules(start);
/*      */     
/*      */ 
/*  248 */     RuleBasedTimeZone rbtz = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)rules[0]);
/*  249 */     for (int i = 1; i < rules.length; i++) {
/*  250 */       rbtz.addTransitionRule(rules[i]);
/*      */     }
/*  252 */     String[] customProperties = null;
/*  253 */     if ((this.olsonzid != null) && (ICU_TZVERSION != null)) {
/*  254 */       customProperties = new String[1];
/*  255 */       customProperties[0] = ("X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Partial@" + start + "]");
/*      */     }
/*      */     
/*  258 */     writeZone(writer, rbtz, customProperties);
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
/*      */   public void writeSimple(Writer writer, long time)
/*      */     throws IOException
/*      */   {
/*  279 */     TimeZoneRule[] rules = this.tz.getSimpleTimeZoneRulesNear(time);
/*      */     
/*      */ 
/*  282 */     RuleBasedTimeZone rbtz = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)rules[0]);
/*  283 */     for (int i = 1; i < rules.length; i++) {
/*  284 */       rbtz.addTransitionRule(rules[i]);
/*      */     }
/*  286 */     String[] customProperties = null;
/*  287 */     if ((this.olsonzid != null) && (ICU_TZVERSION != null)) {
/*  288 */       customProperties = new String[1];
/*  289 */       customProperties[0] = ("X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Simple@" + time + "]");
/*      */     }
/*      */     
/*  292 */     writeZone(writer, rbtz, customProperties);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getNextTransition(long base, boolean inclusive)
/*      */   {
/*  302 */     return this.tz.getNextTransition(base, inclusive);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getPreviousTransition(long base, boolean inclusive)
/*      */   {
/*  310 */     return this.tz.getPreviousTransition(base, inclusive);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasEquivalentTransitions(TimeZone other, long start, long end)
/*      */   {
/*  318 */     return this.tz.hasEquivalentTransitions(other, start, end);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneRule[] getTimeZoneRules()
/*      */   {
/*  326 */     return this.tz.getTimeZoneRules();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneRule[] getTimeZoneRules(long start)
/*      */   {
/*  334 */     return this.tz.getTimeZoneRules(start);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  342 */     VTimeZone other = (VTimeZone)super.clone();
/*  343 */     other.tz = ((BasicTimeZone)this.tz.clone());
/*  344 */     return other;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  351 */   private String olsonzid = null;
/*  352 */   private String tzurl = null;
/*  353 */   private Date lastmod = null;
/*      */   
/*      */   private static String ICU_TZVERSION;
/*      */   
/*      */   private static final String ICU_TZINFO_PROP = "X-TZINFO";
/*      */   
/*      */   private static final int DEF_DSTSAVINGS = 3600000;
/*      */   
/*      */   private static final long DEF_TZSTARTTIME = 0L;
/*      */   
/*      */   private static final long MIN_TIME = Long.MIN_VALUE;
/*      */   
/*      */   private static final long MAX_TIME = Long.MAX_VALUE;
/*      */   
/*      */   private static final String COLON = ":";
/*      */   
/*      */   private static final String SEMICOLON = ";";
/*      */   
/*      */   private static final String EQUALS_SIGN = "=";
/*      */   
/*      */   private static final String COMMA = ",";
/*      */   
/*      */   private static final String NEWLINE = "\r\n";
/*      */   
/*      */   private static final String ICAL_BEGIN_VTIMEZONE = "BEGIN:VTIMEZONE";
/*      */   
/*      */   private static final String ICAL_END_VTIMEZONE = "END:VTIMEZONE";
/*      */   private static final String ICAL_BEGIN = "BEGIN";
/*      */   private static final String ICAL_END = "END";
/*      */   private static final String ICAL_VTIMEZONE = "VTIMEZONE";
/*      */   private static final String ICAL_TZID = "TZID";
/*      */   private static final String ICAL_STANDARD = "STANDARD";
/*      */   private static final String ICAL_DAYLIGHT = "DAYLIGHT";
/*      */   private static final String ICAL_DTSTART = "DTSTART";
/*      */   private static final String ICAL_TZOFFSETFROM = "TZOFFSETFROM";
/*      */   private static final String ICAL_TZOFFSETTO = "TZOFFSETTO";
/*      */   private static final String ICAL_RDATE = "RDATE";
/*      */   private static final String ICAL_RRULE = "RRULE";
/*      */   private static final String ICAL_TZNAME = "TZNAME";
/*      */   private static final String ICAL_TZURL = "TZURL";
/*      */   private static final String ICAL_LASTMOD = "LAST-MODIFIED";
/*      */   private static final String ICAL_FREQ = "FREQ";
/*      */   private static final String ICAL_UNTIL = "UNTIL";
/*      */   private static final String ICAL_YEARLY = "YEARLY";
/*      */   private static final String ICAL_BYMONTH = "BYMONTH";
/*      */   private static final String ICAL_BYDAY = "BYDAY";
/*      */   private static final String ICAL_BYMONTHDAY = "BYMONTHDAY";
/*  400 */   private static final String[] ICAL_DOW_NAMES = { "SU", "MO", "TU", "WE", "TH", "FR", "SA" };
/*      */   
/*      */ 
/*      */ 
/*  404 */   private static final int[] MONTHLENGTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */   private static final int INI = 0;
/*      */   private static final int VTZ = 1;
/*      */   
/*      */   static {
/*  409 */     try { ICU_TZVERSION = TimeZone.getTZDataVersion();
/*      */     }
/*      */     catch (MissingResourceException e) {
/*  412 */       ICU_TZVERSION = null;
/*      */     }
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
/*      */   private boolean load(Reader reader)
/*      */   {
/*      */     try
/*      */     {
/*  434 */       this.vtzlines = new LinkedList();
/*  435 */       boolean eol = false;
/*  436 */       boolean start = false;
/*  437 */       boolean success = false;
/*  438 */       StringBuilder line = new StringBuilder();
/*      */       for (;;) {
/*  440 */         int ch = reader.read();
/*  441 */         if (ch == -1)
/*      */         {
/*  443 */           if ((!start) || (!line.toString().startsWith("END:VTIMEZONE"))) break;
/*  444 */           this.vtzlines.add(line.toString());
/*  445 */           success = true; break;
/*      */         }
/*      */         
/*      */ 
/*  449 */         if (ch != 13)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  454 */           if (eol) {
/*  455 */             if ((ch != 9) && (ch != 32))
/*      */             {
/*  457 */               if ((start) && 
/*  458 */                 (line.length() > 0)) {
/*  459 */                 this.vtzlines.add(line.toString());
/*      */               }
/*      */               
/*  462 */               line.setLength(0);
/*  463 */               if (ch != 10) {
/*  464 */                 line.append((char)ch);
/*      */               }
/*      */             }
/*  467 */             eol = false;
/*      */           }
/*  469 */           else if (ch == 10)
/*      */           {
/*  471 */             eol = true;
/*  472 */             if (start) {
/*  473 */               if (line.toString().startsWith("END:VTIMEZONE")) {
/*  474 */                 this.vtzlines.add(line.toString());
/*  475 */                 success = true;
/*  476 */                 break;
/*      */               }
/*      */             }
/*  479 */             else if (line.toString().startsWith("BEGIN:VTIMEZONE")) {
/*  480 */               this.vtzlines.add(line.toString());
/*  481 */               line.setLength(0);
/*  482 */               start = true;
/*  483 */               eol = false;
/*      */             }
/*      */           }
/*      */           else {
/*  487 */             line.append((char)ch);
/*      */           }
/*      */         }
/*      */       }
/*  491 */       if (!success) {
/*  492 */         return false;
/*      */       }
/*      */     }
/*      */     catch (IOException ioe) {
/*  496 */       return false;
/*      */     }
/*      */     
/*  499 */     return parse();
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
/*      */   private boolean parse()
/*      */   {
/*  513 */     if ((this.vtzlines == null) || (this.vtzlines.size() == 0)) {
/*  514 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  519 */     String tzid = null;
/*      */     
/*  521 */     int state = 0;
/*  522 */     boolean dst = false;
/*  523 */     String from = null;
/*  524 */     String to = null;
/*  525 */     String tzname = null;
/*  526 */     String dtstart = null;
/*  527 */     boolean isRRULE = false;
/*  528 */     List<String> dates = null;
/*  529 */     List<TimeZoneRule> rules = new ArrayList();
/*  530 */     int initialRawOffset = 0;
/*  531 */     int initialDSTSavings = 0;
/*  532 */     long firstStart = Long.MAX_VALUE;
/*      */     
/*  534 */     for (String line : this.vtzlines) {
/*  535 */       int valueSep = line.indexOf(":");
/*  536 */       if (valueSep >= 0)
/*      */       {
/*      */ 
/*  539 */         String name = line.substring(0, valueSep);
/*  540 */         String value = line.substring(valueSep + 1);
/*      */         
/*  542 */         switch (state) {
/*      */         case 0: 
/*  544 */           if ((name.equals("BEGIN")) && (value.equals("VTIMEZONE"))) {
/*  545 */             state = 1;
/*      */           }
/*      */           break;
/*      */         case 1: 
/*  549 */           if (name.equals("TZID")) {
/*  550 */             tzid = value;
/*  551 */           } else if (name.equals("TZURL")) {
/*  552 */             this.tzurl = value;
/*  553 */           } else if (name.equals("LAST-MODIFIED"))
/*      */           {
/*      */ 
/*  556 */             this.lastmod = new Date(parseDateTimeString(value, 0));
/*  557 */           } else if (name.equals("BEGIN")) {
/*  558 */             boolean isDST = value.equals("DAYLIGHT");
/*  559 */             if ((value.equals("STANDARD")) || (isDST))
/*      */             {
/*  561 */               if (tzid == null) {
/*  562 */                 state = 3;
/*      */               }
/*      */               else
/*      */               {
/*  566 */                 dates = null;
/*  567 */                 isRRULE = false;
/*  568 */                 from = null;
/*  569 */                 to = null;
/*  570 */                 tzname = null;
/*  571 */                 dst = isDST;
/*  572 */                 state = 2;
/*      */               }
/*      */             }
/*      */             else {
/*  576 */               state = 3;
/*  577 */               break;
/*      */             }
/*  579 */           } else { if (!name.equals("END")) break; }
/*  580 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */         case 2: 
/*  585 */           if (name.equals("DTSTART")) {
/*  586 */             dtstart = value;
/*  587 */           } else if (name.equals("TZNAME")) {
/*  588 */             tzname = value;
/*  589 */           } else if (name.equals("TZOFFSETFROM")) {
/*  590 */             from = value;
/*  591 */           } else if (name.equals("TZOFFSETTO")) {
/*  592 */             to = value;
/*  593 */           } else if (name.equals("RDATE"))
/*      */           {
/*  595 */             if (isRRULE) {
/*  596 */               state = 3;
/*      */             }
/*      */             else {
/*  599 */               if (dates == null) {
/*  600 */                 dates = new LinkedList();
/*      */               }
/*      */               
/*      */ 
/*  604 */               StringTokenizer st = new StringTokenizer(value, ",");
/*  605 */               while (st.hasMoreTokens()) {
/*  606 */                 String date = st.nextToken();
/*  607 */                 dates.add(date);
/*      */               }
/*  609 */             } } else if (name.equals("RRULE"))
/*      */           {
/*  611 */             if ((!isRRULE) && (dates != null)) {
/*  612 */               state = 3;
/*      */             } else {
/*  614 */               if (dates == null) {
/*  615 */                 dates = new LinkedList();
/*      */               }
/*  617 */               isRRULE = true;
/*  618 */               dates.add(value);
/*  619 */             } } else if (name.equals("END"))
/*      */           {
/*  621 */             if ((dtstart == null) || (from == null) || (to == null)) {
/*  622 */               state = 3;
/*      */             }
/*      */             else
/*      */             {
/*  626 */               if (tzname == null) {
/*  627 */                 tzname = getDefaultTZName(tzid, dst);
/*      */               }
/*      */               
/*      */ 
/*  631 */               TimeZoneRule rule = null;
/*  632 */               int fromOffset = 0;
/*  633 */               int toOffset = 0;
/*  634 */               int rawOffset = 0;
/*  635 */               int dstSavings = 0;
/*  636 */               long start = 0L;
/*      */               try
/*      */               {
/*  639 */                 fromOffset = offsetStrToMillis(from);
/*  640 */                 toOffset = offsetStrToMillis(to);
/*      */                 
/*  642 */                 if (dst)
/*      */                 {
/*  644 */                   if (toOffset - fromOffset > 0) {
/*  645 */                     rawOffset = fromOffset;
/*  646 */                     dstSavings = toOffset - fromOffset;
/*      */                   }
/*      */                   else {
/*  649 */                     rawOffset = toOffset - 3600000;
/*  650 */                     dstSavings = 3600000;
/*      */                   }
/*      */                 } else {
/*  653 */                   rawOffset = toOffset;
/*  654 */                   dstSavings = 0;
/*      */                 }
/*      */                 
/*      */ 
/*  658 */                 start = parseDateTimeString(dtstart, fromOffset);
/*      */                 
/*      */ 
/*  661 */                 Date actualStart = null;
/*  662 */                 if (isRRULE) {
/*  663 */                   rule = createRuleByRRULE(tzname, rawOffset, dstSavings, start, dates, fromOffset);
/*      */                 } else {
/*  665 */                   rule = createRuleByRDATE(tzname, rawOffset, dstSavings, start, dates, fromOffset);
/*      */                 }
/*  667 */                 if (rule != null) {
/*  668 */                   actualStart = rule.getFirstStart(fromOffset, 0);
/*  669 */                   if (actualStart.getTime() < firstStart)
/*      */                   {
/*  671 */                     firstStart = actualStart.getTime();
/*      */                     
/*      */ 
/*      */ 
/*  675 */                     if (dstSavings > 0) {
/*  676 */                       initialRawOffset = fromOffset;
/*  677 */                       initialDSTSavings = 0;
/*      */                     }
/*  679 */                     else if (fromOffset - toOffset == 3600000) {
/*  680 */                       initialRawOffset = fromOffset - 3600000;
/*  681 */                       initialDSTSavings = 3600000;
/*      */                     } else {
/*  683 */                       initialRawOffset = fromOffset;
/*  684 */                       initialDSTSavings = 0;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */               catch (IllegalArgumentException iae) {}
/*      */               
/*      */ 
/*      */ 
/*  693 */               if (rule == null) {
/*  694 */                 state = 3;
/*      */               }
/*      */               else {
/*  697 */                 rules.add(rule);
/*  698 */                 state = 1;
/*      */               }
/*      */             } }
/*      */           break;
/*      */         }
/*  703 */         if (state == 3) {
/*  704 */           this.vtzlines = null;
/*  705 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  710 */     if (rules.size() == 0) {
/*  711 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  715 */     InitialTimeZoneRule initialRule = new InitialTimeZoneRule(getDefaultTZName(tzid, false), initialRawOffset, initialDSTSavings);
/*      */     
/*      */ 
/*      */ 
/*  719 */     RuleBasedTimeZone rbtz = new RuleBasedTimeZone(tzid, initialRule);
/*      */     
/*  721 */     int finalRuleIdx = -1;
/*  722 */     int finalRuleCount = 0;
/*  723 */     for (int i = 0; i < rules.size(); i++) {
/*  724 */       TimeZoneRule r = (TimeZoneRule)rules.get(i);
/*  725 */       if (((r instanceof AnnualTimeZoneRule)) && 
/*  726 */         (((AnnualTimeZoneRule)r).getEndYear() == Integer.MAX_VALUE)) {
/*  727 */         finalRuleCount++;
/*  728 */         finalRuleIdx = i;
/*      */       }
/*      */     }
/*      */     
/*  732 */     if (finalRuleCount > 2)
/*      */     {
/*  734 */       return false;
/*      */     }
/*      */     
/*  737 */     if (finalRuleCount == 1) {
/*  738 */       if (rules.size() == 1)
/*      */       {
/*      */ 
/*      */ 
/*  742 */         rules.clear();
/*      */       }
/*      */       else {
/*  745 */         AnnualTimeZoneRule finalRule = (AnnualTimeZoneRule)rules.get(finalRuleIdx);
/*  746 */         int tmpRaw = finalRule.getRawOffset();
/*  747 */         int tmpDST = finalRule.getDSTSavings();
/*      */         
/*      */ 
/*  750 */         Date finalStart = finalRule.getFirstStart(initialRawOffset, initialDSTSavings);
/*  751 */         Date start = finalStart;
/*  752 */         for (int i = 0; i < rules.size(); i++) {
/*  753 */           if (finalRuleIdx != i)
/*      */           {
/*      */ 
/*  756 */             TimeZoneRule r = (TimeZoneRule)rules.get(i);
/*  757 */             Date lastStart = r.getFinalStart(tmpRaw, tmpDST);
/*  758 */             if (lastStart.after(start)) {
/*  759 */               start = finalRule.getNextStart(lastStart.getTime(), r.getRawOffset(), r.getDSTSavings(), false);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */         TimeZoneRule newRule;
/*      */         TimeZoneRule newRule;
/*  766 */         if (start == finalStart)
/*      */         {
/*  768 */           newRule = new TimeArrayTimeZoneRule(finalRule.getName(), finalRule.getRawOffset(), finalRule.getDSTSavings(), new long[] { finalStart.getTime() }, 2);
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*  776 */           int[] fields = Grego.timeToFields(start.getTime(), null);
/*  777 */           newRule = new AnnualTimeZoneRule(finalRule.getName(), finalRule.getRawOffset(), finalRule.getDSTSavings(), finalRule.getRule(), finalRule.getStartYear(), fields[0]);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  785 */         rules.set(finalRuleIdx, newRule);
/*      */       }
/*      */     }
/*      */     
/*  789 */     for (TimeZoneRule r : rules) {
/*  790 */       rbtz.addTransitionRule(r);
/*      */     }
/*      */     
/*  793 */     this.tz = rbtz;
/*  794 */     setID(tzid);
/*  795 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String getDefaultTZName(String tzid, boolean isDST)
/*      */   {
/*  802 */     if (isDST) {
/*  803 */       return tzid + "(DST)";
/*      */     }
/*  805 */     return tzid + "(STD)";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static TimeZoneRule createRuleByRRULE(String tzname, int rawOffset, int dstSavings, long start, List<String> dates, int fromOffset)
/*      */   {
/*  813 */     if ((dates == null) || (dates.size() == 0)) {
/*  814 */       return null;
/*      */     }
/*      */     
/*  817 */     String rrule = (String)dates.get(0);
/*      */     
/*  819 */     long[] until = new long[1];
/*  820 */     int[] ruleFields = parseRRULE(rrule, until);
/*  821 */     if (ruleFields == null)
/*      */     {
/*  823 */       return null;
/*      */     }
/*      */     
/*  826 */     int month = ruleFields[0];
/*  827 */     int dayOfWeek = ruleFields[1];
/*  828 */     int nthDayOfWeek = ruleFields[2];
/*  829 */     int dayOfMonth = ruleFields[3];
/*      */     
/*  831 */     if (dates.size() == 1)
/*      */     {
/*  833 */       if (ruleFields.length > 4)
/*      */       {
/*      */ 
/*  836 */         if ((ruleFields.length != 10) || (month == -1) || (dayOfWeek == 0))
/*      */         {
/*      */ 
/*  839 */           return null;
/*      */         }
/*  841 */         int firstDay = 31;
/*  842 */         int[] days = new int[7];
/*  843 */         for (int i = 0; i < 7; i++) {
/*  844 */           days[i] = ruleFields[(3 + i)];
/*      */           
/*      */ 
/*      */ 
/*  848 */           days[i] = (days[i] > 0 ? days[i] : MONTHLENGTH[month] + days[i] + 1);
/*  849 */           firstDay = days[i] < firstDay ? days[i] : firstDay;
/*      */         }
/*      */         
/*  852 */         for (int i = 1; i < 7; i++) {
/*  853 */           boolean found = false;
/*  854 */           for (int j = 0; j < 7; j++) {
/*  855 */             if (days[j] == firstDay + i) {
/*  856 */               found = true;
/*  857 */               break;
/*      */             }
/*      */           }
/*  860 */           if (!found)
/*      */           {
/*  862 */             return null;
/*      */           }
/*      */         }
/*      */         
/*  866 */         dayOfMonth = firstDay;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  871 */       if ((month == -1) || (dayOfWeek == 0) || (dayOfMonth == 0))
/*      */       {
/*  873 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  877 */       if (dates.size() > 7) {
/*  878 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  885 */       int earliestMonth = month;
/*  886 */       int daysCount = ruleFields.length - 3;
/*  887 */       int earliestDay = 31;
/*  888 */       for (int i = 0; i < daysCount; i++) {
/*  889 */         int dom = ruleFields[(3 + i)];
/*  890 */         dom = dom > 0 ? dom : MONTHLENGTH[month] + dom + 1;
/*  891 */         earliestDay = dom < earliestDay ? dom : earliestDay;
/*      */       }
/*      */       
/*  894 */       int anotherMonth = -1;
/*  895 */       for (int i = 1; i < dates.size(); i++) {
/*  896 */         rrule = (String)dates.get(i);
/*  897 */         long[] unt = new long[1];
/*  898 */         int[] fields = parseRRULE(rrule, unt);
/*      */         
/*      */ 
/*  901 */         if (unt[0] > until[0]) {
/*  902 */           until = unt;
/*      */         }
/*      */         
/*      */ 
/*  906 */         if ((fields[0] == -1) || (fields[1] == 0) || (fields[3] == 0)) {
/*  907 */           return null;
/*      */         }
/*      */         
/*  910 */         int count = fields.length - 3;
/*  911 */         if (daysCount + count > 7)
/*      */         {
/*  913 */           return null;
/*      */         }
/*      */         
/*      */ 
/*  917 */         if (fields[1] != dayOfWeek) {
/*  918 */           return null;
/*      */         }
/*      */         
/*  921 */         if (fields[0] != month) {
/*  922 */           if (anotherMonth == -1) {
/*  923 */             int diff = fields[0] - month;
/*  924 */             if ((diff == -11) || (diff == -1))
/*      */             {
/*  926 */               anotherMonth = fields[0];
/*  927 */               earliestMonth = anotherMonth;
/*      */               
/*  929 */               earliestDay = 31;
/*  930 */             } else if ((diff == 11) || (diff == 1))
/*      */             {
/*  932 */               anotherMonth = fields[0];
/*      */             }
/*      */             else {
/*  935 */               return null;
/*      */             }
/*  937 */           } else if ((fields[0] != month) && (fields[0] != anotherMonth))
/*      */           {
/*  939 */             return null;
/*      */           }
/*      */         }
/*      */         
/*  943 */         if (fields[0] == earliestMonth) {
/*  944 */           for (int j = 0; j < count; j++) {
/*  945 */             int dom = fields[(3 + j)];
/*  946 */             dom = dom > 0 ? dom : MONTHLENGTH[fields[0]] + dom + 1;
/*  947 */             earliestDay = dom < earliestDay ? dom : earliestDay;
/*      */           }
/*      */         }
/*  950 */         daysCount += count;
/*      */       }
/*  952 */       if (daysCount != 7)
/*      */       {
/*  954 */         return null;
/*      */       }
/*  956 */       month = earliestMonth;
/*  957 */       dayOfMonth = earliestDay;
/*      */     }
/*      */     
/*      */ 
/*  961 */     int[] dfields = Grego.timeToFields(start + fromOffset, null);
/*  962 */     int startYear = dfields[0];
/*  963 */     if (month == -1)
/*      */     {
/*  965 */       month = dfields[1];
/*      */     }
/*  967 */     if ((dayOfWeek == 0) && (nthDayOfWeek == 0) && (dayOfMonth == 0))
/*      */     {
/*  969 */       dayOfMonth = dfields[2];
/*      */     }
/*  971 */     int timeInDay = dfields[5];
/*      */     
/*  973 */     int endYear = Integer.MAX_VALUE;
/*  974 */     if (until[0] != Long.MIN_VALUE) {
/*  975 */       Grego.timeToFields(until[0], dfields);
/*  976 */       endYear = dfields[0];
/*      */     }
/*      */     
/*      */ 
/*  980 */     DateTimeRule adtr = null;
/*  981 */     if ((dayOfWeek == 0) && (nthDayOfWeek == 0) && (dayOfMonth != 0))
/*      */     {
/*  983 */       adtr = new DateTimeRule(month, dayOfMonth, timeInDay, 0);
/*  984 */     } else if ((dayOfWeek != 0) && (nthDayOfWeek != 0) && (dayOfMonth == 0))
/*      */     {
/*  986 */       adtr = new DateTimeRule(month, nthDayOfWeek, dayOfWeek, timeInDay, 0);
/*  987 */     } else if ((dayOfWeek != 0) && (nthDayOfWeek == 0) && (dayOfMonth != 0))
/*      */     {
/*      */ 
/*  990 */       adtr = new DateTimeRule(month, dayOfMonth, dayOfWeek, true, timeInDay, 0);
/*      */     }
/*      */     else {
/*  993 */       return null;
/*      */     }
/*      */     
/*  996 */     return new AnnualTimeZoneRule(tzname, rawOffset, dstSavings, adtr, startYear, endYear);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int TZI = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int ERR = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int[] parseRRULE(String rrule, long[] until)
/*      */   {
/* 1017 */     int month = -1;
/* 1018 */     int dayOfWeek = 0;
/* 1019 */     int nthDayOfWeek = 0;
/* 1020 */     int[] dayOfMonth = null;
/*      */     
/* 1022 */     long untilTime = Long.MIN_VALUE;
/* 1023 */     boolean yearly = false;
/* 1024 */     boolean parseError = false;
/* 1025 */     StringTokenizer st = new StringTokenizer(rrule, ";");
/*      */     
/* 1027 */     while (st.hasMoreTokens())
/*      */     {
/* 1029 */       String prop = st.nextToken();
/* 1030 */       int sep = prop.indexOf("=");
/* 1031 */       String value; if (sep != -1) {
/* 1032 */         String attr = prop.substring(0, sep);
/* 1033 */         value = prop.substring(sep + 1);
/*      */       } else {
/* 1035 */         parseError = true;
/* 1036 */         break; }
/*      */       String value;
/*      */       String attr;
/* 1039 */       if (attr.equals("FREQ"))
/*      */       {
/* 1041 */         if (value.equals("YEARLY")) {
/* 1042 */           yearly = true;
/*      */         } else {
/* 1044 */           parseError = true;
/* 1045 */           break;
/*      */         }
/* 1047 */       } else if (attr.equals("UNTIL"))
/*      */       {
/*      */         try {
/* 1050 */           untilTime = parseDateTimeString(value, 0);
/*      */         } catch (IllegalArgumentException iae) {
/* 1052 */           parseError = true;
/* 1053 */           break;
/*      */         }
/* 1055 */       } else { if (attr.equals("BYMONTH"))
/*      */         {
/*      */ 
/* 1058 */           if (value.length() > 2) {
/* 1059 */             parseError = true;
/* 1060 */             break;
/*      */           }
/*      */           try {
/* 1063 */             month = Integer.parseInt(value) - 1;
/* 1064 */             if ((month < 0) || (month >= 12)) {
/* 1065 */               parseError = true;
/* 1066 */               break;
/*      */             }
/*      */           } catch (NumberFormatException nfe) {
/* 1069 */             parseError = true;
/* 1070 */             break;
/*      */           } }
/* 1072 */         if (attr.equals("BYDAY"))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1078 */           int length = value.length();
/* 1079 */           if ((length < 2) || (length > 4)) {
/* 1080 */             parseError = true;
/* 1081 */             break;
/*      */           }
/* 1083 */           if (length > 2)
/*      */           {
/* 1085 */             int sign = 1;
/* 1086 */             if (value.charAt(0) == '+') {
/* 1087 */               sign = 1;
/* 1088 */             } else if (value.charAt(0) == '-') {
/* 1089 */               sign = -1;
/* 1090 */             } else if (length == 4) {
/* 1091 */               parseError = true;
/* 1092 */               break;
/*      */             }
/*      */             try {
/* 1095 */               int n = Integer.parseInt(value.substring(length - 3, length - 2));
/* 1096 */               if ((n == 0) || (n > 4)) {
/* 1097 */                 parseError = true;
/* 1098 */                 break;
/*      */               }
/* 1100 */               nthDayOfWeek = n * sign;
/*      */             } catch (NumberFormatException nfe) {
/* 1102 */               parseError = true;
/* 1103 */               break;
/*      */             }
/* 1105 */             value = value.substring(length - 2);
/*      */           }
/*      */           
/* 1108 */           for (int wday = 0; wday < ICAL_DOW_NAMES.length; wday++) {
/* 1109 */             if (value.equals(ICAL_DOW_NAMES[wday])) {
/*      */               break;
/*      */             }
/*      */           }
/* 1113 */           if (wday < ICAL_DOW_NAMES.length)
/*      */           {
/* 1115 */             dayOfWeek = wday + 1;
/*      */           } else {
/* 1117 */             parseError = true;
/* 1118 */             break;
/*      */           }
/* 1120 */         } else if (attr.equals("BYMONTHDAY"))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1125 */           StringTokenizer days = new StringTokenizer(value, ",");
/* 1126 */           int count = days.countTokens();
/* 1127 */           dayOfMonth = new int[count];
/* 1128 */           int index = 0;
/* 1129 */           for (;;) { if (days.hasMoreTokens()) {
/*      */               try {
/* 1131 */                 dayOfMonth[(index++)] = Integer.parseInt(days.nextToken());
/*      */               } catch (NumberFormatException nfe) {
/* 1133 */                 parseError = true;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1140 */     if (parseError) {
/* 1141 */       return null;
/*      */     }
/* 1143 */     if (!yearly)
/*      */     {
/* 1145 */       return null;
/*      */     }
/*      */     
/* 1148 */     until[0] = untilTime;
/*      */     
/*      */     int[] results;
/* 1151 */     if (dayOfMonth == null) {
/* 1152 */       int[] results = new int[4];
/* 1153 */       results[3] = 0;
/*      */     } else {
/* 1155 */       results = new int[3 + dayOfMonth.length];
/* 1156 */       for (int i = 0; i < dayOfMonth.length; i++) {
/* 1157 */         results[(3 + i)] = dayOfMonth[i];
/*      */       }
/*      */     }
/* 1160 */     results[0] = month;
/* 1161 */     results[1] = dayOfWeek;
/* 1162 */     results[2] = nthDayOfWeek;
/* 1163 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static TimeZoneRule createRuleByRDATE(String tzname, int rawOffset, int dstSavings, long start, List<String> dates, int fromOffset)
/*      */   {
/*      */     long[] times;
/*      */     
/*      */ 
/* 1173 */     if ((dates == null) || (dates.size() == 0))
/*      */     {
/*      */ 
/* 1176 */       long[] times = new long[1];
/* 1177 */       times[0] = start;
/*      */     } else {
/* 1179 */       times = new long[dates.size()];
/* 1180 */       int idx = 0;
/*      */       try {
/* 1182 */         for (String date : dates) {
/* 1183 */           times[(idx++)] = parseDateTimeString(date, fromOffset);
/*      */         }
/*      */       } catch (IllegalArgumentException iae) {
/* 1186 */         return null;
/*      */       }
/*      */     }
/* 1189 */     return new TimeArrayTimeZoneRule(tzname, rawOffset, dstSavings, times, 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void writeZone(Writer w, BasicTimeZone basictz, String[] customProperties)
/*      */     throws IOException
/*      */   {
/* 1197 */     writeHeader(w);
/*      */     
/* 1199 */     if ((customProperties != null) && (customProperties.length > 0)) {
/* 1200 */       for (int i = 0; i < customProperties.length; i++) {
/* 1201 */         if (customProperties[i] != null) {
/* 1202 */           w.write(customProperties[i]);
/* 1203 */           w.write("\r\n");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1208 */     long t = Long.MIN_VALUE;
/* 1209 */     String dstName = null;
/* 1210 */     int dstFromOffset = 0;
/* 1211 */     int dstFromDSTSavings = 0;
/* 1212 */     int dstToOffset = 0;
/* 1213 */     int dstStartYear = 0;
/* 1214 */     int dstMonth = 0;
/* 1215 */     int dstDayOfWeek = 0;
/* 1216 */     int dstWeekInMonth = 0;
/* 1217 */     int dstMillisInDay = 0;
/* 1218 */     long dstStartTime = 0L;
/* 1219 */     long dstUntilTime = 0L;
/* 1220 */     int dstCount = 0;
/* 1221 */     AnnualTimeZoneRule finalDstRule = null;
/*      */     
/* 1223 */     String stdName = null;
/* 1224 */     int stdFromOffset = 0;
/* 1225 */     int stdFromDSTSavings = 0;
/* 1226 */     int stdToOffset = 0;
/* 1227 */     int stdStartYear = 0;
/* 1228 */     int stdMonth = 0;
/* 1229 */     int stdDayOfWeek = 0;
/* 1230 */     int stdWeekInMonth = 0;
/* 1231 */     int stdMillisInDay = 0;
/* 1232 */     long stdStartTime = 0L;
/* 1233 */     long stdUntilTime = 0L;
/* 1234 */     int stdCount = 0;
/* 1235 */     AnnualTimeZoneRule finalStdRule = null;
/*      */     
/* 1237 */     int[] dtfields = new int[6];
/* 1238 */     boolean hasTransitions = false;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1242 */       TimeZoneTransition tzt = basictz.getNextTransition(t, false);
/* 1243 */       if (tzt == null) {
/*      */         break;
/*      */       }
/* 1246 */       hasTransitions = true;
/* 1247 */       t = tzt.getTime();
/* 1248 */       String name = tzt.getTo().getName();
/* 1249 */       boolean isDst = tzt.getTo().getDSTSavings() != 0;
/* 1250 */       int fromOffset = tzt.getFrom().getRawOffset() + tzt.getFrom().getDSTSavings();
/* 1251 */       int fromDSTSavings = tzt.getFrom().getDSTSavings();
/* 1252 */       int toOffset = tzt.getTo().getRawOffset() + tzt.getTo().getDSTSavings();
/* 1253 */       Grego.timeToFields(tzt.getTime() + fromOffset, dtfields);
/* 1254 */       int weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
/* 1255 */       int year = dtfields[0];
/* 1256 */       boolean sameRule = false;
/* 1257 */       if (isDst) {
/* 1258 */         if ((finalDstRule == null) && ((tzt.getTo() instanceof AnnualTimeZoneRule)) && 
/* 1259 */           (((AnnualTimeZoneRule)tzt.getTo()).getEndYear() == Integer.MAX_VALUE)) {
/* 1260 */           finalDstRule = (AnnualTimeZoneRule)tzt.getTo();
/*      */         }
/*      */         
/* 1263 */         if (dstCount > 0) {
/* 1264 */           if ((year == dstStartYear + dstCount) && (name.equals(dstName)) && (dstFromOffset == fromOffset) && (dstToOffset == toOffset) && (dstMonth == dtfields[1]) && (dstDayOfWeek == dtfields[3]) && (dstWeekInMonth == weekInMonth) && (dstMillisInDay == dtfields[5]))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1273 */             dstUntilTime = t;
/* 1274 */             dstCount++;
/* 1275 */             sameRule = true;
/*      */           }
/* 1277 */           if (!sameRule) {
/* 1278 */             if (dstCount == 1) {
/* 1279 */               writeZonePropsByTime(w, true, dstName, dstFromOffset, dstToOffset, dstStartTime, true);
/*      */             }
/*      */             else {
/* 1282 */               writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1287 */         if (!sameRule)
/*      */         {
/* 1289 */           dstName = name;
/* 1290 */           dstFromOffset = fromOffset;
/* 1291 */           dstFromDSTSavings = fromDSTSavings;
/* 1292 */           dstToOffset = toOffset;
/* 1293 */           dstStartYear = year;
/* 1294 */           dstMonth = dtfields[1];
/* 1295 */           dstDayOfWeek = dtfields[3];
/* 1296 */           dstWeekInMonth = weekInMonth;
/* 1297 */           dstMillisInDay = dtfields[5];
/* 1298 */           dstStartTime = dstUntilTime = t;
/* 1299 */           dstCount = 1;
/*      */         }
/* 1301 */         if ((finalStdRule != null) && (finalDstRule != null)) {
/*      */           break;
/*      */         }
/*      */       } else {
/* 1305 */         if ((finalStdRule == null) && ((tzt.getTo() instanceof AnnualTimeZoneRule)) && 
/* 1306 */           (((AnnualTimeZoneRule)tzt.getTo()).getEndYear() == Integer.MAX_VALUE)) {
/* 1307 */           finalStdRule = (AnnualTimeZoneRule)tzt.getTo();
/*      */         }
/*      */         
/* 1310 */         if (stdCount > 0) {
/* 1311 */           if ((year == stdStartYear + stdCount) && (name.equals(stdName)) && (stdFromOffset == fromOffset) && (stdToOffset == toOffset) && (stdMonth == dtfields[1]) && (stdDayOfWeek == dtfields[3]) && (stdWeekInMonth == weekInMonth) && (stdMillisInDay == dtfields[5]))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1320 */             stdUntilTime = t;
/* 1321 */             stdCount++;
/* 1322 */             sameRule = true;
/*      */           }
/* 1324 */           if (!sameRule) {
/* 1325 */             if (stdCount == 1) {
/* 1326 */               writeZonePropsByTime(w, false, stdName, stdFromOffset, stdToOffset, stdStartTime, true);
/*      */             }
/*      */             else {
/* 1329 */               writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1334 */         if (!sameRule)
/*      */         {
/* 1336 */           stdName = name;
/* 1337 */           stdFromOffset = fromOffset;
/* 1338 */           stdFromDSTSavings = fromDSTSavings;
/* 1339 */           stdToOffset = toOffset;
/* 1340 */           stdStartYear = year;
/* 1341 */           stdMonth = dtfields[1];
/* 1342 */           stdDayOfWeek = dtfields[3];
/* 1343 */           stdWeekInMonth = weekInMonth;
/* 1344 */           stdMillisInDay = dtfields[5];
/* 1345 */           stdStartTime = stdUntilTime = t;
/* 1346 */           stdCount = 1;
/*      */         }
/* 1348 */         if ((finalStdRule != null) && (finalDstRule != null)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1353 */     if (!hasTransitions)
/*      */     {
/* 1355 */       int offset = basictz.getOffset(0L);
/* 1356 */       boolean isDst = offset != basictz.getRawOffset();
/* 1357 */       writeZonePropsByTime(w, isDst, getDefaultTZName(basictz.getID(), isDst), offset, offset, 0L - offset, false);
/*      */     }
/*      */     else {
/* 1360 */       if (dstCount > 0) {
/* 1361 */         if (finalDstRule == null) {
/* 1362 */           if (dstCount == 1) {
/* 1363 */             writeZonePropsByTime(w, true, dstName, dstFromOffset, dstToOffset, dstStartTime, true);
/*      */           }
/*      */           else {
/* 1366 */             writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
/*      */           }
/*      */           
/*      */         }
/* 1370 */         else if (dstCount == 1) {
/* 1371 */           writeFinalRule(w, true, finalDstRule, dstFromOffset - dstFromDSTSavings, dstFromDSTSavings, dstStartTime);
/*      */ 
/*      */ 
/*      */         }
/* 1375 */         else if (isEquivalentDateRule(dstMonth, dstWeekInMonth, dstDayOfWeek, finalDstRule.getRule())) {
/* 1376 */           writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, Long.MAX_VALUE);
/*      */         }
/*      */         else
/*      */         {
/* 1380 */           writeZonePropsByDOW(w, true, dstName, dstFromOffset, dstToOffset, dstMonth, dstWeekInMonth, dstDayOfWeek, dstStartTime, dstUntilTime);
/*      */           
/* 1382 */           writeFinalRule(w, true, finalDstRule, dstFromOffset - dstFromDSTSavings, dstFromDSTSavings, dstStartTime);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1388 */       if (stdCount > 0) {
/* 1389 */         if (finalStdRule == null) {
/* 1390 */           if (stdCount == 1) {
/* 1391 */             writeZonePropsByTime(w, false, stdName, stdFromOffset, stdToOffset, stdStartTime, true);
/*      */           }
/*      */           else {
/* 1394 */             writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
/*      */           }
/*      */           
/*      */         }
/* 1398 */         else if (stdCount == 1) {
/* 1399 */           writeFinalRule(w, false, finalStdRule, stdFromOffset - stdFromDSTSavings, stdFromDSTSavings, stdStartTime);
/*      */ 
/*      */ 
/*      */         }
/* 1403 */         else if (isEquivalentDateRule(stdMonth, stdWeekInMonth, stdDayOfWeek, finalStdRule.getRule())) {
/* 1404 */           writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, Long.MAX_VALUE);
/*      */         }
/*      */         else
/*      */         {
/* 1408 */           writeZonePropsByDOW(w, false, stdName, stdFromOffset, stdToOffset, stdMonth, stdWeekInMonth, stdDayOfWeek, stdStartTime, stdUntilTime);
/*      */           
/* 1410 */           writeFinalRule(w, false, finalStdRule, stdFromOffset - stdFromDSTSavings, stdFromDSTSavings, stdStartTime);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1417 */     writeFooter(w);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isEquivalentDateRule(int month, int weekInMonth, int dayOfWeek, DateTimeRule dtrule)
/*      */   {
/* 1425 */     if ((month != dtrule.getRuleMonth()) || (dayOfWeek != dtrule.getRuleDayOfWeek())) {
/* 1426 */       return false;
/*      */     }
/* 1428 */     if (dtrule.getTimeRuleType() != 0)
/*      */     {
/* 1430 */       return false;
/*      */     }
/* 1432 */     if ((dtrule.getDateRuleType() == 1) && (dtrule.getRuleWeekInMonth() == weekInMonth))
/*      */     {
/* 1434 */       return true;
/*      */     }
/* 1436 */     int ruleDOM = dtrule.getRuleDayOfMonth();
/* 1437 */     if (dtrule.getDateRuleType() == 2) {
/* 1438 */       if ((ruleDOM % 7 == 1) && ((ruleDOM + 6) / 7 == weekInMonth)) {
/* 1439 */         return true;
/*      */       }
/* 1441 */       if ((month != 1) && ((MONTHLENGTH[month] - ruleDOM) % 7 == 6) && (weekInMonth == -1 * ((MONTHLENGTH[month] - ruleDOM + 1) / 7)))
/*      */       {
/* 1443 */         return true;
/*      */       }
/*      */     }
/* 1446 */     if (dtrule.getDateRuleType() == 3) {
/* 1447 */       if ((ruleDOM % 7 == 0) && (ruleDOM / 7 == weekInMonth)) {
/* 1448 */         return true;
/*      */       }
/* 1450 */       if ((month != 1) && ((MONTHLENGTH[month] - ruleDOM) % 7 == 0) && (weekInMonth == -1 * ((MONTHLENGTH[month] - ruleDOM) / 7 + 1)))
/*      */       {
/* 1452 */         return true;
/*      */       }
/*      */     }
/* 1455 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByTime(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, long time, boolean withRDATE)
/*      */     throws IOException
/*      */   {
/* 1463 */     beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, time);
/* 1464 */     if (withRDATE) {
/* 1465 */       writer.write("RDATE");
/* 1466 */       writer.write(":");
/* 1467 */       writer.write(getDateTimeString(time + fromOffset));
/* 1468 */       writer.write("\r\n");
/*      */     }
/* 1470 */     endZoneProps(writer, isDst);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByDOM(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, int month, int dayOfMonth, long startTime, long untilTime)
/*      */     throws IOException
/*      */   {
/* 1478 */     beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
/*      */     
/* 1480 */     beginRRULE(writer, month);
/* 1481 */     writer.write("BYMONTHDAY");
/* 1482 */     writer.write("=");
/* 1483 */     writer.write(Integer.toString(dayOfMonth));
/*      */     
/* 1485 */     if (untilTime != Long.MAX_VALUE) {
/* 1486 */       appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
/*      */     }
/* 1488 */     writer.write("\r\n");
/*      */     
/* 1490 */     endZoneProps(writer, isDst);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByDOW(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, int month, int weekInMonth, int dayOfWeek, long startTime, long untilTime)
/*      */     throws IOException
/*      */   {
/* 1498 */     beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
/*      */     
/* 1500 */     beginRRULE(writer, month);
/* 1501 */     writer.write("BYDAY");
/* 1502 */     writer.write("=");
/* 1503 */     writer.write(Integer.toString(weekInMonth));
/* 1504 */     writer.write(ICAL_DOW_NAMES[(dayOfWeek - 1)]);
/*      */     
/* 1506 */     if (untilTime != Long.MAX_VALUE) {
/* 1507 */       appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
/*      */     }
/* 1509 */     writer.write("\r\n");
/*      */     
/* 1511 */     endZoneProps(writer, isDst);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByDOW_GEQ_DOM(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, int month, int dayOfMonth, int dayOfWeek, long startTime, long untilTime)
/*      */     throws IOException
/*      */   {
/* 1520 */     if (dayOfMonth % 7 == 1)
/*      */     {
/* 1522 */       writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, (dayOfMonth + 6) / 7, dayOfWeek, startTime, untilTime);
/*      */     }
/* 1524 */     else if ((month != 1) && ((MONTHLENGTH[month] - dayOfMonth) % 7 == 6))
/*      */     {
/* 1526 */       writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, -1 * ((MONTHLENGTH[month] - dayOfMonth + 1) / 7), dayOfWeek, startTime, untilTime);
/*      */     }
/*      */     else
/*      */     {
/* 1530 */       beginZoneProps(writer, isDst, tzname, fromOffset, toOffset, startTime);
/*      */       
/*      */ 
/* 1533 */       int startDay = dayOfMonth;
/* 1534 */       int currentMonthDays = 7;
/*      */       
/* 1536 */       if (dayOfMonth <= 0)
/*      */       {
/* 1538 */         int prevMonthDays = 1 - dayOfMonth;
/* 1539 */         currentMonthDays -= prevMonthDays;
/*      */         
/* 1541 */         int prevMonth = month - 1 < 0 ? 11 : month - 1;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1546 */         writeZonePropsByDOW_GEQ_DOM_sub(writer, prevMonth, -prevMonthDays, dayOfWeek, prevMonthDays, Long.MAX_VALUE, fromOffset);
/*      */         
/*      */ 
/* 1549 */         startDay = 1;
/* 1550 */       } else if (dayOfMonth + 6 > MONTHLENGTH[month])
/*      */       {
/*      */ 
/* 1553 */         int nextMonthDays = dayOfMonth + 6 - MONTHLENGTH[month];
/* 1554 */         currentMonthDays -= nextMonthDays;
/*      */         
/* 1556 */         int nextMonth = month + 1 > 11 ? 0 : month + 1;
/*      */         
/* 1558 */         writeZonePropsByDOW_GEQ_DOM_sub(writer, nextMonth, 1, dayOfWeek, nextMonthDays, Long.MAX_VALUE, fromOffset);
/*      */       }
/* 1560 */       writeZonePropsByDOW_GEQ_DOM_sub(writer, month, startDay, dayOfWeek, currentMonthDays, untilTime, fromOffset);
/* 1561 */       endZoneProps(writer, isDst);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByDOW_GEQ_DOM_sub(Writer writer, int month, int dayOfMonth, int dayOfWeek, int numDays, long untilTime, int fromOffset)
/*      */     throws IOException
/*      */   {
/* 1571 */     int startDayNum = dayOfMonth;
/* 1572 */     boolean isFeb = month == 1;
/* 1573 */     if ((dayOfMonth < 0) && (!isFeb))
/*      */     {
/* 1575 */       startDayNum = MONTHLENGTH[month] + dayOfMonth + 1;
/*      */     }
/* 1577 */     beginRRULE(writer, month);
/* 1578 */     writer.write("BYDAY");
/* 1579 */     writer.write("=");
/* 1580 */     writer.write(ICAL_DOW_NAMES[(dayOfWeek - 1)]);
/* 1581 */     writer.write(";");
/* 1582 */     writer.write("BYMONTHDAY");
/* 1583 */     writer.write("=");
/*      */     
/* 1585 */     writer.write(Integer.toString(startDayNum));
/* 1586 */     for (int i = 1; i < numDays; i++) {
/* 1587 */       writer.write(",");
/* 1588 */       writer.write(Integer.toString(startDayNum + i));
/*      */     }
/*      */     
/* 1591 */     if (untilTime != Long.MAX_VALUE) {
/* 1592 */       appendUNTIL(writer, getDateTimeString(untilTime + fromOffset));
/*      */     }
/* 1594 */     writer.write("\r\n");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void writeZonePropsByDOW_LEQ_DOM(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, int month, int dayOfMonth, int dayOfWeek, long startTime, long untilTime)
/*      */     throws IOException
/*      */   {
/* 1603 */     if (dayOfMonth % 7 == 0)
/*      */     {
/* 1605 */       writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, dayOfMonth / 7, dayOfWeek, startTime, untilTime);
/*      */     }
/* 1607 */     else if ((month != 1) && ((MONTHLENGTH[month] - dayOfMonth) % 7 == 0))
/*      */     {
/* 1609 */       writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, month, -1 * ((MONTHLENGTH[month] - dayOfMonth) / 7 + 1), dayOfWeek, startTime, untilTime);
/*      */     }
/* 1611 */     else if ((month == 1) && (dayOfMonth == 29))
/*      */     {
/* 1613 */       writeZonePropsByDOW(writer, isDst, tzname, fromOffset, toOffset, 1, -1, dayOfWeek, startTime, untilTime);
/*      */     }
/*      */     else
/*      */     {
/* 1617 */       writeZonePropsByDOW_GEQ_DOM(writer, isDst, tzname, fromOffset, toOffset, month, dayOfMonth - 6, dayOfWeek, startTime, untilTime);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void writeFinalRule(Writer writer, boolean isDst, AnnualTimeZoneRule rule, int fromRawOffset, int fromDSTSavings, long startTime)
/*      */     throws IOException
/*      */   {
/* 1627 */     DateTimeRule dtrule = toWallTimeRule(rule.getRule(), fromRawOffset, fromDSTSavings);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1633 */     int timeInDay = dtrule.getRuleMillisInDay();
/* 1634 */     if (timeInDay < 0) {
/* 1635 */       startTime += 0 - timeInDay;
/* 1636 */     } else if (timeInDay >= 86400000) {
/* 1637 */       startTime -= timeInDay - 86399999;
/*      */     }
/*      */     
/* 1640 */     int toOffset = rule.getRawOffset() + rule.getDSTSavings();
/* 1641 */     switch (dtrule.getDateRuleType()) {
/*      */     case 0: 
/* 1643 */       writeZonePropsByDOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), startTime, Long.MAX_VALUE);
/*      */       
/* 1645 */       break;
/*      */     case 1: 
/* 1647 */       writeZonePropsByDOW(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleWeekInMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
/*      */       
/* 1649 */       break;
/*      */     case 2: 
/* 1651 */       writeZonePropsByDOW_GEQ_DOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
/*      */       
/* 1653 */       break;
/*      */     case 3: 
/* 1655 */       writeZonePropsByDOW_LEQ_DOM(writer, isDst, rule.getName(), fromRawOffset + fromDSTSavings, toOffset, dtrule.getRuleMonth(), dtrule.getRuleDayOfMonth(), dtrule.getRuleDayOfWeek(), startTime, Long.MAX_VALUE);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static DateTimeRule toWallTimeRule(DateTimeRule rule, int rawOffset, int dstSavings)
/*      */   {
/* 1665 */     if (rule.getTimeRuleType() == 0) {
/* 1666 */       return rule;
/*      */     }
/* 1668 */     int wallt = rule.getRuleMillisInDay();
/* 1669 */     if (rule.getTimeRuleType() == 2) {
/* 1670 */       wallt += rawOffset + dstSavings;
/* 1671 */     } else if (rule.getTimeRuleType() == 1) {
/* 1672 */       wallt += dstSavings;
/*      */     }
/*      */     
/* 1675 */     int month = -1;int dom = 0;int dow = 0;int dtype = -1;
/* 1676 */     int dshift = 0;
/* 1677 */     if (wallt < 0) {
/* 1678 */       dshift = -1;
/* 1679 */       wallt += 86400000;
/* 1680 */     } else if (wallt >= 86400000) {
/* 1681 */       dshift = 1;
/* 1682 */       wallt -= 86400000;
/*      */     }
/*      */     
/* 1685 */     month = rule.getRuleMonth();
/* 1686 */     dom = rule.getRuleDayOfMonth();
/* 1687 */     dow = rule.getRuleDayOfWeek();
/* 1688 */     dtype = rule.getDateRuleType();
/*      */     
/* 1690 */     if (dshift != 0) {
/* 1691 */       if (dtype == 1)
/*      */       {
/* 1693 */         int wim = rule.getRuleWeekInMonth();
/* 1694 */         if (wim > 0) {
/* 1695 */           dtype = 2;
/* 1696 */           dom = 7 * (wim - 1) + 1;
/*      */         } else {
/* 1698 */           dtype = 3;
/* 1699 */           dom = MONTHLENGTH[month] + 7 * (wim + 1);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1704 */       dom += dshift;
/* 1705 */       if (dom == 0) {
/* 1706 */         month--;
/* 1707 */         month = month < 0 ? 11 : month;
/* 1708 */         dom = MONTHLENGTH[month];
/* 1709 */       } else if (dom > MONTHLENGTH[month]) {
/* 1710 */         month++;
/* 1711 */         month = month > 11 ? 0 : month;
/* 1712 */         dom = 1;
/*      */       }
/* 1714 */       if (dtype != 0)
/*      */       {
/* 1716 */         dow += dshift;
/* 1717 */         if (dow < 1) {
/* 1718 */           dow = 7;
/* 1719 */         } else if (dow > 7) {
/* 1720 */           dow = 1;
/*      */         }
/*      */       }
/*      */     }
/*      */     DateTimeRule modifiedRule;
/*      */     DateTimeRule modifiedRule;
/* 1726 */     if (dtype == 0) {
/* 1727 */       modifiedRule = new DateTimeRule(month, dom, wallt, 0);
/*      */     } else {
/* 1729 */       modifiedRule = new DateTimeRule(month, dom, dow, dtype == 2, wallt, 0);
/*      */     }
/*      */     
/* 1732 */     return modifiedRule;
/*      */   }
/*      */   
/*      */ 
/*      */   private static void beginZoneProps(Writer writer, boolean isDst, String tzname, int fromOffset, int toOffset, long startTime)
/*      */     throws IOException
/*      */   {
/* 1739 */     writer.write("BEGIN");
/* 1740 */     writer.write(":");
/* 1741 */     if (isDst) {
/* 1742 */       writer.write("DAYLIGHT");
/*      */     } else {
/* 1744 */       writer.write("STANDARD");
/*      */     }
/* 1746 */     writer.write("\r\n");
/*      */     
/*      */ 
/* 1749 */     writer.write("TZOFFSETTO");
/* 1750 */     writer.write(":");
/* 1751 */     writer.write(millisToOffset(toOffset));
/* 1752 */     writer.write("\r\n");
/*      */     
/*      */ 
/* 1755 */     writer.write("TZOFFSETFROM");
/* 1756 */     writer.write(":");
/* 1757 */     writer.write(millisToOffset(fromOffset));
/* 1758 */     writer.write("\r\n");
/*      */     
/*      */ 
/* 1761 */     writer.write("TZNAME");
/* 1762 */     writer.write(":");
/* 1763 */     writer.write(tzname);
/* 1764 */     writer.write("\r\n");
/*      */     
/*      */ 
/* 1767 */     writer.write("DTSTART");
/* 1768 */     writer.write(":");
/* 1769 */     writer.write(getDateTimeString(startTime + fromOffset));
/* 1770 */     writer.write("\r\n");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void endZoneProps(Writer writer, boolean isDst)
/*      */     throws IOException
/*      */   {
/* 1778 */     writer.write("END");
/* 1779 */     writer.write(":");
/* 1780 */     if (isDst) {
/* 1781 */       writer.write("DAYLIGHT");
/*      */     } else {
/* 1783 */       writer.write("STANDARD");
/*      */     }
/* 1785 */     writer.write("\r\n");
/*      */   }
/*      */   
/*      */ 
/*      */   private static void beginRRULE(Writer writer, int month)
/*      */     throws IOException
/*      */   {
/* 1792 */     writer.write("RRULE");
/* 1793 */     writer.write(":");
/* 1794 */     writer.write("FREQ");
/* 1795 */     writer.write("=");
/* 1796 */     writer.write("YEARLY");
/* 1797 */     writer.write(";");
/* 1798 */     writer.write("BYMONTH");
/* 1799 */     writer.write("=");
/* 1800 */     writer.write(Integer.toString(month + 1));
/* 1801 */     writer.write(";");
/*      */   }
/*      */   
/*      */ 
/*      */   private static void appendUNTIL(Writer writer, String until)
/*      */     throws IOException
/*      */   {
/* 1808 */     if (until != null) {
/* 1809 */       writer.write(";");
/* 1810 */       writer.write("UNTIL");
/* 1811 */       writer.write("=");
/* 1812 */       writer.write(until);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void writeHeader(Writer writer)
/*      */     throws IOException
/*      */   {
/* 1820 */     writer.write("BEGIN");
/* 1821 */     writer.write(":");
/* 1822 */     writer.write("VTIMEZONE");
/* 1823 */     writer.write("\r\n");
/* 1824 */     writer.write("TZID");
/* 1825 */     writer.write(":");
/* 1826 */     writer.write(this.tz.getID());
/* 1827 */     writer.write("\r\n");
/* 1828 */     if (this.tzurl != null) {
/* 1829 */       writer.write("TZURL");
/* 1830 */       writer.write(":");
/* 1831 */       writer.write(this.tzurl);
/* 1832 */       writer.write("\r\n");
/*      */     }
/* 1834 */     if (this.lastmod != null) {
/* 1835 */       writer.write("LAST-MODIFIED");
/* 1836 */       writer.write(":");
/* 1837 */       writer.write(getUTCDateTimeString(this.lastmod.getTime()));
/* 1838 */       writer.write("\r\n");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void writeFooter(Writer writer)
/*      */     throws IOException
/*      */   {
/* 1846 */     writer.write("END");
/* 1847 */     writer.write(":");
/* 1848 */     writer.write("VTIMEZONE");
/* 1849 */     writer.write("\r\n");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String getDateTimeString(long time)
/*      */   {
/* 1856 */     int[] fields = Grego.timeToFields(time, null);
/* 1857 */     StringBuilder sb = new StringBuilder(15);
/* 1858 */     sb.append(numToString(fields[0], 4));
/* 1859 */     sb.append(numToString(fields[1] + 1, 2));
/* 1860 */     sb.append(numToString(fields[2], 2));
/* 1861 */     sb.append('T');
/*      */     
/* 1863 */     int t = fields[5];
/* 1864 */     int hour = t / 3600000;
/* 1865 */     t %= 3600000;
/* 1866 */     int min = t / 60000;
/* 1867 */     t %= 60000;
/* 1868 */     int sec = t / 1000;
/*      */     
/* 1870 */     sb.append(numToString(hour, 2));
/* 1871 */     sb.append(numToString(min, 2));
/* 1872 */     sb.append(numToString(sec, 2));
/* 1873 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String getUTCDateTimeString(long time)
/*      */   {
/* 1880 */     return getDateTimeString(time) + "Z";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long parseDateTimeString(String str, int offset)
/*      */   {
/* 1888 */     int year = 0;int month = 0;int day = 0;int hour = 0;int min = 0;int sec = 0;
/* 1889 */     boolean isUTC = false;
/* 1890 */     boolean isValid = false;
/*      */     
/* 1892 */     if (str != null)
/*      */     {
/*      */ 
/*      */ 
/* 1896 */       int length = str.length();
/* 1897 */       if ((length == 15) || (length == 16))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1902 */         if (str.charAt(8) == 'T')
/*      */         {
/*      */ 
/*      */ 
/* 1906 */           if (length == 16) {
/* 1907 */             if (str.charAt(15) == 'Z')
/*      */             {
/*      */ 
/*      */ 
/* 1911 */               isUTC = true;
/*      */             }
/*      */           } else {
/*      */             try {
/* 1915 */               year = Integer.parseInt(str.substring(0, 4));
/* 1916 */               month = Integer.parseInt(str.substring(4, 6)) - 1;
/* 1917 */               day = Integer.parseInt(str.substring(6, 8));
/* 1918 */               hour = Integer.parseInt(str.substring(9, 11));
/* 1919 */               min = Integer.parseInt(str.substring(11, 13));
/* 1920 */               sec = Integer.parseInt(str.substring(13, 15));
/*      */             }
/*      */             catch (NumberFormatException nfe)
/*      */             {
/*      */               break label249;
/*      */             }
/* 1926 */             int maxDayOfMonth = Grego.monthLength(year, month);
/* 1927 */             if ((year >= 0) && (month >= 0) && (month <= 11) && (day >= 1) && (day <= maxDayOfMonth) && (hour >= 0) && (hour < 24) && (min >= 0) && (min < 60) && (sec >= 0) && (sec < 60))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 1932 */               isValid = true; }
/*      */           } } } }
/*      */     label249:
/* 1935 */     if (!isValid) {
/* 1936 */       throw new IllegalArgumentException("Invalid date time string format");
/*      */     }
/*      */     
/* 1939 */     long time = Grego.fieldsToDay(year, month, day) * 86400000L;
/* 1940 */     time += hour * 3600000 + min * 60000 + sec * 1000;
/* 1941 */     if (!isUTC) {
/* 1942 */       time -= offset;
/*      */     }
/* 1944 */     return time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int offsetStrToMillis(String str)
/*      */   {
/* 1951 */     boolean isValid = false;
/* 1952 */     int sign = 0;int hour = 0;int min = 0;int sec = 0;
/*      */     
/*      */ 
/* 1955 */     if (str != null)
/*      */     {
/*      */ 
/* 1958 */       int length = str.length();
/* 1959 */       if ((length == 5) || (length == 7))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1964 */         char s = str.charAt(0);
/* 1965 */         if (s == '+') {
/* 1966 */           sign = 1;
/* 1967 */         } else { if (s != '-') break label119;
/* 1968 */           sign = -1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1975 */           hour = Integer.parseInt(str.substring(1, 3));
/* 1976 */           min = Integer.parseInt(str.substring(3, 5));
/* 1977 */           if (length == 7) {
/* 1978 */             sec = Integer.parseInt(str.substring(5, 7));
/*      */           }
/*      */         } catch (NumberFormatException nfe) {
/*      */           break label119;
/*      */         }
/* 1983 */         isValid = true;
/*      */       } }
/*      */     label119:
/* 1986 */     if (!isValid) {
/* 1987 */       throw new IllegalArgumentException("Bad offset string");
/*      */     }
/* 1989 */     int millis = sign * ((hour * 60 + min) * 60 + sec) * 1000;
/* 1990 */     return millis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String millisToOffset(int millis)
/*      */   {
/* 1997 */     StringBuilder sb = new StringBuilder(7);
/* 1998 */     if (millis >= 0) {
/* 1999 */       sb.append('+');
/*      */     } else {
/* 2001 */       sb.append('-');
/* 2002 */       millis = -millis;
/*      */     }
/*      */     
/* 2005 */     int t = millis / 1000;
/*      */     
/* 2007 */     int sec = t % 60;
/* 2008 */     t = (t - sec) / 60;
/* 2009 */     int min = t % 60;
/* 2010 */     int hour = t / 60;
/*      */     
/* 2012 */     sb.append(numToString(hour, 2));
/* 2013 */     sb.append(numToString(min, 2));
/* 2014 */     sb.append(numToString(sec, 2));
/*      */     
/* 2016 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String numToString(int num, int width)
/*      */   {
/* 2023 */     String str = Integer.toString(num);
/* 2024 */     int len = str.length();
/* 2025 */     if (len >= width) {
/* 2026 */       return str.substring(len - width, len);
/*      */     }
/* 2028 */     StringBuilder sb = new StringBuilder(width);
/* 2029 */     for (int i = len; i < width; i++) {
/* 2030 */       sb.append('0');
/*      */     }
/* 2032 */     sb.append(str);
/* 2033 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\VTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */