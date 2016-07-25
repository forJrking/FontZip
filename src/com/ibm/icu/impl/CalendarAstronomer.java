/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import java.util.Date;
/*      */ import java.util.TimeZone;
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
/*      */ public class CalendarAstronomer
/*      */ {
/*      */   public static final double SIDEREAL_DAY = 23.93446960027D;
/*      */   public static final double SOLAR_DAY = 24.065709816D;
/*      */   public static final double SYNODIC_MONTH = 29.530588853D;
/*      */   public static final double SIDEREAL_MONTH = 27.32166D;
/*      */   public static final double TROPICAL_YEAR = 365.242191D;
/*      */   public static final double SIDEREAL_YEAR = 365.25636D;
/*      */   public static final int SECOND_MS = 1000;
/*      */   public static final int MINUTE_MS = 60000;
/*      */   public static final int HOUR_MS = 3600000;
/*      */   public static final long DAY_MS = 86400000L;
/*      */   public static final long JULIAN_EPOCH_MS = -210866760000000L;
/*      */   static final long EPOCH_2000_MS = 946598400000L;
/*      */   private static final double PI = 3.141592653589793D;
/*      */   private static final double PI2 = 6.283185307179586D;
/*      */   private static final double RAD_HOUR = 3.819718634205488D;
/*      */   private static final double DEG_RAD = 0.017453292519943295D;
/*      */   private static final double RAD_DEG = 57.29577951308232D;
/*      */   static final double JD_EPOCH = 2447891.5D;
/*      */   static final double SUN_ETA_G = 4.87650757829735D;
/*      */   static final double SUN_OMEGA_G = 4.935239984568769D;
/*      */   static final double SUN_E = 0.016713D;
/*      */   
/*      */   public CalendarAstronomer()
/*      */   {
/*  201 */     this(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CalendarAstronomer(Date d)
/*      */   {
/*  210 */     this(d.getTime());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CalendarAstronomer(long aTime)
/*      */   {
/*  222 */     this.time = aTime;
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
/*      */   public CalendarAstronomer(double longitude, double latitude)
/*      */   {
/*  240 */     this();
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
/*      */   public void setTime(long aTime)
/*      */   {
/*  263 */     this.time = aTime;
/*  264 */     clearCache();
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
/*      */   public void setDate(Date date)
/*      */   {
/*  278 */     setTime(date.getTime());
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
/*      */   public void setJulianDay(double jdn)
/*      */   {
/*  296 */     this.time = ((jdn * 8.64E7D) + -210866760000000L);
/*  297 */     clearCache();
/*  298 */     this.julianDay = jdn;
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
/*      */   public long getTime()
/*      */   {
/*  311 */     return this.time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getDate()
/*      */   {
/*  323 */     return new Date(this.time);
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
/*      */   public double getJulianDay()
/*      */   {
/*  336 */     if (this.julianDay == Double.MIN_VALUE) {
/*  337 */       this.julianDay = ((this.time - -210866760000000L) / 8.64E7D);
/*      */     }
/*  339 */     return this.julianDay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getJulianCentury()
/*      */   {
/*  350 */     if (this.julianCentury == Double.MIN_VALUE) {
/*  351 */       this.julianCentury = ((getJulianDay() - 2415020.0D) / 36525.0D);
/*      */     }
/*  353 */     return this.julianCentury;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getGreenwichSidereal()
/*      */   {
/*  361 */     if (this.siderealTime == Double.MIN_VALUE)
/*      */     {
/*      */ 
/*      */ 
/*  365 */       double UT = normalize(this.time / 3600000.0D, 24.0D);
/*      */       
/*  367 */       this.siderealTime = normalize(getSiderealOffset() + UT * 1.002737909D, 24.0D);
/*      */     }
/*  369 */     return this.siderealTime;
/*      */   }
/*      */   
/*      */   private double getSiderealOffset() {
/*  373 */     if (this.siderealT0 == Double.MIN_VALUE) {
/*  374 */       double JD = Math.floor(getJulianDay() - 0.5D) + 0.5D;
/*  375 */       double S = JD - 2451545.0D;
/*  376 */       double T = S / 36525.0D;
/*  377 */       this.siderealT0 = normalize(6.697374558D + 2400.051336D * T + 2.5862E-5D * T * T, 24.0D);
/*      */     }
/*  379 */     return this.siderealT0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getLocalSidereal()
/*      */   {
/*  387 */     return normalize(getGreenwichSidereal() + this.fGmtOffset / 3600000.0D, 24.0D);
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
/*      */   private long lstToUT(double lst)
/*      */   {
/*  401 */     double lt = normalize((lst - getSiderealOffset()) * 0.9972695663D, 24.0D);
/*      */     
/*      */ 
/*  404 */     long base = 86400000L * ((this.time + this.fGmtOffset) / 86400000L) - this.fGmtOffset;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  409 */     return base + (lt * 3600000.0D);
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
/*      */   public final Equatorial eclipticToEquatorial(Ecliptic ecliptic)
/*      */   {
/*  426 */     return eclipticToEquatorial(ecliptic.longitude, ecliptic.latitude);
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
/*      */   public final Equatorial eclipticToEquatorial(double eclipLong, double eclipLat)
/*      */   {
/*  443 */     double obliq = eclipticObliquity();
/*  444 */     double sinE = Math.sin(obliq);
/*  445 */     double cosE = Math.cos(obliq);
/*      */     
/*  447 */     double sinL = Math.sin(eclipLong);
/*  448 */     double cosL = Math.cos(eclipLong);
/*      */     
/*  450 */     double sinB = Math.sin(eclipLat);
/*  451 */     double cosB = Math.cos(eclipLat);
/*  452 */     double tanB = Math.tan(eclipLat);
/*      */     
/*  454 */     return new Equatorial(Math.atan2(sinL * cosE - tanB * sinE, cosL), Math.asin(sinB * cosE + cosB * sinE * sinL));
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
/*      */   public final Equatorial eclipticToEquatorial(double eclipLong)
/*      */   {
/*  468 */     return eclipticToEquatorial(eclipLong, 0.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Horizon eclipticToHorizon(double eclipLong)
/*      */   {
/*  476 */     Equatorial equatorial = eclipticToEquatorial(eclipLong);
/*      */     
/*  478 */     double H = getLocalSidereal() * 3.141592653589793D / 12.0D - equatorial.ascension;
/*      */     
/*  480 */     double sinH = Math.sin(H);
/*  481 */     double cosH = Math.cos(H);
/*  482 */     double sinD = Math.sin(equatorial.declination);
/*  483 */     double cosD = Math.cos(equatorial.declination);
/*  484 */     double sinL = Math.sin(this.fLatitude);
/*  485 */     double cosL = Math.cos(this.fLatitude);
/*      */     
/*  487 */     double altitude = Math.asin(sinD * sinL + cosD * cosL * cosH);
/*  488 */     double azimuth = Math.atan2(-cosD * cosL * sinH, sinD - sinL * Math.sin(altitude));
/*      */     
/*  490 */     return new Horizon(azimuth, altitude);
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
/*      */   public double getSunLongitude()
/*      */   {
/*  571 */     if (this.sunLongitude == Double.MIN_VALUE) {
/*  572 */       double[] result = getSunLongitude(getJulianDay());
/*  573 */       this.sunLongitude = result[0];
/*  574 */       this.meanAnomalySun = result[1];
/*      */     }
/*  576 */     return this.sunLongitude;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   double[] getSunLongitude(double julian)
/*      */   {
/*  587 */     double day = julian - 2447891.5D;
/*      */     
/*      */ 
/*      */ 
/*  591 */     double epochAngle = norm2PI(0.017202791632524146D * day);
/*      */     
/*      */ 
/*      */ 
/*  595 */     double meanAnomaly = norm2PI(epochAngle + 4.87650757829735D - 4.935239984568769D);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  601 */     return new double[] { norm2PI(trueAnomaly(meanAnomaly, 0.016713D) + 4.935239984568769D), meanAnomaly };
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
/*  613 */   public Equatorial getSunPosition() { return eclipticToEquatorial(getSunLongitude(), 0.0D); }
/*      */   
/*      */   private static class SolarLongitude {
/*      */     double value;
/*      */     
/*  618 */     SolarLongitude(double val) { this.value = val; }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  627 */   public static final SolarLongitude VERNAL_EQUINOX = new SolarLongitude(0.0D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  635 */   public static final SolarLongitude SUMMER_SOLSTICE = new SolarLongitude(1.5707963267948966D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  643 */   public static final SolarLongitude AUTUMN_EQUINOX = new SolarLongitude(3.141592653589793D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  651 */   public static final SolarLongitude WINTER_SOLSTICE = new SolarLongitude(4.71238898038469D);
/*      */   
/*      */   static final double moonL0 = 5.556284436750021D;
/*      */   static final double moonP0 = 0.6342598060246725D;
/*      */   static final double moonN0 = 5.559050068029439D;
/*      */   static final double moonI = 0.08980357792017056D;
/*      */   
/*      */   public long getSunTime(double desired, boolean next)
/*      */   {
/*  660 */     timeOfAngle(new AngleFunc() { public double eval() { return CalendarAstronomer.this.getSunLongitude(); } }, desired, 365.242191D, 60000L, next);
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
/*      */   public long getSunTime(SolarLongitude desired, boolean next)
/*      */   {
/*  673 */     return getSunTime(desired.value, next);
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
/*      */   public long getSunRiseSet(boolean rise)
/*      */   {
/*  690 */     long t0 = this.time;
/*      */     
/*      */ 
/*  693 */     long noon = (this.time + this.fGmtOffset) / 86400000L * 86400000L - this.fGmtOffset + 43200000L;
/*      */     
/*  695 */     setTime(noon + (rise ? -6 : 6) * 3600000);
/*      */     
/*  697 */     long t = riseOrSet(new CoordFunc() {
/*  698 */       public CalendarAstronomer.Equatorial eval() { return CalendarAstronomer.this.getSunPosition(); } }, rise, 0.009302604913129777D, 0.009890199094634533D, 5000L);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  705 */     setTime(t0);
/*  706 */     return t;
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
/*      */   static final double moonE = 0.0549D;
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
/*      */   static final double moonA = 384401.0D;
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
/*      */   static final double moonT0 = 0.009042550854582622D;
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
/*      */   static final double moonPi = 0.016592845198710092D;
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
/*      */   public Equatorial getMoonPosition()
/*      */   {
/* 1008 */     if (this.moonPosition == null)
/*      */     {
/*      */ 
/* 1011 */       double sunLong = getSunLongitude();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1017 */       double day = getJulianDay() - 2447891.5D;
/*      */       
/*      */ 
/*      */ 
/* 1021 */       double meanLongitude = norm2PI(0.22997150421858628D * day + 5.556284436750021D);
/* 1022 */       double meanAnomalyMoon = norm2PI(meanLongitude - 0.001944368345221015D * day - 0.6342598060246725D);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1030 */       double evection = 0.022233749341155764D * Math.sin(2.0D * (meanLongitude - sunLong) - meanAnomalyMoon);
/*      */       
/* 1032 */       double annual = 0.003242821750205464D * Math.sin(this.meanAnomalySun);
/* 1033 */       double a3 = 0.00645771823237902D * Math.sin(this.meanAnomalySun);
/*      */       
/* 1035 */       meanAnomalyMoon += evection - annual - a3;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1044 */       double center = 0.10975677534091541D * Math.sin(meanAnomalyMoon);
/* 1045 */       double a4 = 0.0037350045992678655D * Math.sin(2.0D * meanAnomalyMoon);
/*      */       
/*      */ 
/* 1048 */       this.moonLongitude = (meanLongitude + evection + center - annual + a4);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1055 */       double variation = 0.011489502465878671D * Math.sin(2.0D * (this.moonLongitude - sunLong));
/*      */       
/* 1057 */       this.moonLongitude += variation;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1066 */       double nodeLongitude = norm2PI(5.559050068029439D - 9.242199067718253E-4D * day);
/*      */       
/* 1068 */       nodeLongitude -= 0.0027925268031909274D * Math.sin(this.meanAnomalySun);
/*      */       
/* 1070 */       double y = Math.sin(this.moonLongitude - nodeLongitude);
/* 1071 */       double x = Math.cos(this.moonLongitude - nodeLongitude);
/*      */       
/* 1073 */       this.moonEclipLong = (Math.atan2(y * Math.cos(0.08980357792017056D), x) + nodeLongitude);
/* 1074 */       double moonEclipLat = Math.asin(y * Math.sin(0.08980357792017056D));
/*      */       
/* 1076 */       this.moonPosition = eclipticToEquatorial(this.moonEclipLong, moonEclipLat);
/*      */     }
/* 1078 */     return this.moonPosition;
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
/*      */   public double getMoonAge()
/*      */   {
/* 1097 */     getMoonPosition();
/*      */     
/* 1099 */     return norm2PI(this.moonEclipLong - this.sunLongitude);
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
/* 1119 */   public double getMoonPhase() { return 0.5D * (1.0D - Math.cos(getMoonAge())); }
/*      */   
/*      */   private static class MoonAge {
/*      */     double value;
/*      */     
/* 1124 */     MoonAge(double val) { this.value = val; }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1132 */   public static final MoonAge NEW_MOON = new MoonAge(0.0D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1139 */   public static final MoonAge FIRST_QUARTER = new MoonAge(1.5707963267948966D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1146 */   public static final MoonAge FULL_MOON = new MoonAge(3.141592653589793D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1153 */   public static final MoonAge LAST_QUARTER = new MoonAge(4.71238898038469D);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private long time;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getMoonTime(double desired, boolean next)
/*      */   {
/* 1166 */     timeOfAngle(new AngleFunc() {
/* 1167 */       public double eval() { return CalendarAstronomer.this.getMoonAge(); } }, desired, 29.530588853D, 60000L, next);
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
/*      */   public long getMoonTime(MoonAge desired, boolean next)
/*      */   {
/* 1184 */     return getMoonTime(desired.value, next);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getMoonRiseSet(boolean rise)
/*      */   {
/* 1194 */     riseOrSet(new CoordFunc() {
/* 1195 */       public CalendarAstronomer.Equatorial eval() { return CalendarAstronomer.this.getMoonPosition(); } }, rise, 0.009302604913129777D, 0.009890199094634533D, 60000L);
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
/*      */   private long timeOfAngle(AngleFunc func, double desired, double periodDays, long epsilon, boolean next)
/*      */   {
/* 1215 */     double lastAngle = func.eval();
/*      */     
/*      */ 
/* 1218 */     double deltaAngle = norm2PI(desired - lastAngle);
/*      */     
/*      */ 
/*      */ 
/* 1222 */     double deltaT = (deltaAngle + (next ? 0.0D : -6.283185307179586D)) * (periodDays * 8.64E7D) / 6.283185307179586D;
/*      */     
/* 1224 */     double lastDeltaT = deltaT;
/* 1225 */     long startTime = this.time;
/*      */     
/* 1227 */     setTime(this.time + deltaT);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     do
/*      */     {
/* 1234 */       double angle = func.eval();
/*      */       
/*      */ 
/* 1237 */       double factor = Math.abs(deltaT / normPI(angle - lastAngle));
/*      */       
/*      */ 
/* 1240 */       deltaT = normPI(desired - angle) * factor;
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
/* 1263 */       if (Math.abs(deltaT) > Math.abs(lastDeltaT)) {
/* 1264 */         long delta = (periodDays * 8.64E7D / 8.0D);
/* 1265 */         setTime(startTime + (next ? delta : -delta));
/* 1266 */         return timeOfAngle(func, desired, periodDays, epsilon, next);
/*      */       }
/*      */       
/* 1269 */       lastDeltaT = deltaT;
/* 1270 */       lastAngle = angle;
/*      */       
/* 1272 */       setTime(this.time + deltaT);
/*      */     }
/* 1274 */     while (Math.abs(deltaT) > epsilon);
/*      */     
/* 1276 */     return this.time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private long riseOrSet(CoordFunc func, boolean rise, double diameter, double refraction, long epsilon)
/*      */   {
/* 1287 */     Equatorial pos = null;
/* 1288 */     double tanL = Math.tan(this.fLatitude);
/* 1289 */     long deltaT = Long.MAX_VALUE;
/* 1290 */     int count = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     do
/*      */     {
/* 1299 */       pos = func.eval();
/* 1300 */       double angle = Math.acos(-tanL * Math.tan(pos.declination));
/* 1301 */       double lst = ((rise ? 6.283185307179586D - angle : angle) + pos.ascension) * 24.0D / 6.283185307179586D;
/*      */       
/*      */ 
/* 1304 */       long newTime = lstToUT(lst);
/*      */       
/* 1306 */       deltaT = newTime - this.time;
/* 1307 */       setTime(newTime);
/*      */       
/* 1309 */       count++; } while ((count < 5) && (Math.abs(deltaT) > epsilon));
/*      */     
/*      */ 
/* 1312 */     double cosD = Math.cos(pos.declination);
/* 1313 */     double psi = Math.acos(Math.sin(this.fLatitude) / cosD);
/* 1314 */     double x = diameter / 2.0D + refraction;
/* 1315 */     double y = Math.asin(Math.sin(x) / Math.sin(psi));
/* 1316 */     long delta = (240.0D * y * 57.29577951308232D / cosD * 1000.0D);
/*      */     
/* 1318 */     return this.time + (rise ? -delta : delta);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final double normalize(double value, double range)
/*      */   {
/* 1330 */     return value - range * Math.floor(value / range);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final double norm2PI(double angle)
/*      */   {
/* 1339 */     return normalize(angle, 6.283185307179586D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final double normPI(double angle)
/*      */   {
/* 1346 */     return normalize(angle + 3.141592653589793D, 6.283185307179586D) - 3.141592653589793D;
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
/*      */   private double trueAnomaly(double meanAnomaly, double eccentricity)
/*      */   {
/* 1367 */     double E = meanAnomaly;
/*      */     double delta;
/* 1369 */     do { delta = E - eccentricity * Math.sin(E) - meanAnomaly;
/* 1370 */       E -= delta / (1.0D - eccentricity * Math.cos(E));
/*      */     }
/* 1372 */     while (Math.abs(delta) > 1.0E-5D);
/*      */     
/* 1374 */     return 2.0D * Math.atan(Math.tan(E / 2.0D) * Math.sqrt((1.0D + eccentricity) / (1.0D - eccentricity)));
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
/*      */   private double eclipticObliquity()
/*      */   {
/* 1387 */     if (this.eclipObliquity == Double.MIN_VALUE) {
/* 1388 */       double epoch = 2451545.0D;
/*      */       
/* 1390 */       double T = (getJulianDay() - 2451545.0D) / 36525.0D;
/*      */       
/* 1392 */       this.eclipObliquity = (23.439292D - 0.013004166666666666D * T - 1.6666666666666665E-7D * T * T + 5.027777777777778E-7D * T * T * T);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1397 */       this.eclipObliquity *= 0.017453292519943295D;
/*      */     }
/* 1399 */     return this.eclipObliquity;
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
/* 1416 */   private double fLongitude = 0.0D;
/* 1417 */   private double fLatitude = 0.0D;
/* 1418 */   private long fGmtOffset = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final double INVALID = Double.MIN_VALUE;
/*      */   
/*      */ 
/*      */ 
/* 1427 */   private transient double julianDay = Double.MIN_VALUE;
/* 1428 */   private transient double julianCentury = Double.MIN_VALUE;
/* 1429 */   private transient double sunLongitude = Double.MIN_VALUE;
/* 1430 */   private transient double meanAnomalySun = Double.MIN_VALUE;
/* 1431 */   private transient double moonLongitude = Double.MIN_VALUE;
/* 1432 */   private transient double moonEclipLong = Double.MIN_VALUE;
/*      */   
/* 1434 */   private transient double eclipObliquity = Double.MIN_VALUE;
/* 1435 */   private transient double siderealT0 = Double.MIN_VALUE;
/* 1436 */   private transient double siderealTime = Double.MIN_VALUE;
/*      */   
/* 1438 */   private transient Equatorial moonPosition = null;
/*      */   
/*      */   private void clearCache() {
/* 1441 */     this.julianDay = Double.MIN_VALUE;
/* 1442 */     this.julianCentury = Double.MIN_VALUE;
/* 1443 */     this.sunLongitude = Double.MIN_VALUE;
/* 1444 */     this.meanAnomalySun = Double.MIN_VALUE;
/* 1445 */     this.moonLongitude = Double.MIN_VALUE;
/* 1446 */     this.moonEclipLong = Double.MIN_VALUE;
/*      */     
/* 1448 */     this.eclipObliquity = Double.MIN_VALUE;
/* 1449 */     this.siderealTime = Double.MIN_VALUE;
/* 1450 */     this.siderealT0 = Double.MIN_VALUE;
/* 1451 */     this.moonPosition = null;
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
/*      */   public String local(long localMillis)
/*      */   {
/* 1470 */     return new Date(localMillis - TimeZone.getDefault().getRawOffset()).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static abstract interface AngleFunc
/*      */   {
/*      */     public abstract double eval();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static abstract interface CoordFunc
/*      */   {
/*      */     public abstract CalendarAstronomer.Equatorial eval();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final class Ecliptic
/*      */   {
/*      */     public final double latitude;
/*      */     
/*      */ 
/*      */     public final double longitude;
/*      */     
/*      */ 
/*      */ 
/*      */     public Ecliptic(double lat, double lon)
/*      */     {
/* 1500 */       this.latitude = lat;
/* 1501 */       this.longitude = lon;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1509 */       return Double.toString(this.longitude * 57.29577951308232D) + "," + this.latitude * 57.29577951308232D;
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
/*      */   public static final class Equatorial
/*      */   {
/*      */     public final double ascension;
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
/*      */     public final double declination;
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
/*      */     public Equatorial(double asc, double dec)
/*      */     {
/* 1559 */       this.ascension = asc;
/* 1560 */       this.declination = dec;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1569 */       return Double.toString(this.ascension * 57.29577951308232D) + "," + this.declination * 57.29577951308232D;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toHmsString()
/*      */     {
/* 1578 */       return CalendarAstronomer.radToHms(this.ascension) + "," + CalendarAstronomer.radToDms(this.declination);
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
/*      */   public static final class Horizon
/*      */   {
/*      */     public final double altitude;
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
/*      */     public final double azimuth;
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
/*      */     public Horizon(double alt, double azim)
/*      */     {
/* 1625 */       this.altitude = alt;
/* 1626 */       this.azimuth = azim;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1635 */       return Double.toString(this.altitude * 57.29577951308232D) + "," + this.azimuth * 57.29577951308232D;
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
/*      */   private static String radToHms(double angle)
/*      */   {
/* 1652 */     int hrs = (int)(angle * 3.819718634205488D);
/* 1653 */     int min = (int)((angle * 3.819718634205488D - hrs) * 60.0D);
/* 1654 */     int sec = (int)((angle * 3.819718634205488D - hrs - min / 60.0D) * 3600.0D);
/*      */     
/* 1656 */     return Integer.toString(hrs) + "h" + min + "m" + sec + "s";
/*      */   }
/*      */   
/*      */   private static String radToDms(double angle) {
/* 1660 */     int deg = (int)(angle * 57.29577951308232D);
/* 1661 */     int min = (int)((angle * 57.29577951308232D - deg) * 60.0D);
/* 1662 */     int sec = (int)((angle * 57.29577951308232D - deg - min / 60.0D) * 3600.0D);
/*      */     
/* 1664 */     return Integer.toString(deg) + "°" + min + "'" + sec + "\"";
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CalendarAstronomer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */