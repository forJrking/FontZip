/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ public class ICULogger
/*     */   extends Logger
/*     */ {
/*     */   private static final String GLOBAL_FLAG_TURN_ON_LOGGING = "all";
/*     */   private static final String SYSTEM_PROP_LOGGER = "icu4j.debug.logging";
/*     */   private LOGGER_STATUS currentStatus;
/*     */   
/*     */   private static enum LOGGER_STATUS
/*     */   {
/*  61 */     ON,  OFF,  NULL;
/*     */     
/*     */ 
/*     */ 
/*     */     private LOGGER_STATUS() {}
/*     */   }
/*     */   
/*     */ 
/*     */   private ICULogger(String name, String resourceBundleName)
/*     */   {
/*  71 */     super(name, resourceBundleName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void setStatus(LOGGER_STATUS newStatus)
/*     */   {
/*  78 */     if (this.currentStatus != newStatus)
/*     */     {
/*  80 */       if ((this.currentStatus == LOGGER_STATUS.OFF) && (newStatus == LOGGER_STATUS.ON)) {
/*  81 */         setLevel(Level.INFO);
/*     */       }
/*     */       
/*  84 */       this.currentStatus = newStatus;
/*     */       
/*  86 */       if (this.currentStatus == LOGGER_STATUS.OFF) {
/*  87 */         setLevel(Level.OFF);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static LOGGER_STATUS checkGlobalLoggingFlag()
/*     */   {
/*     */     try
/*     */     {
/*  98 */       String prop = System.getProperty("icu4j.debug.logging");
/*     */       
/* 100 */       if (prop != null) {
/* 101 */         if (prop.equals("all")) {
/* 102 */           return LOGGER_STATUS.ON;
/*     */         }
/* 104 */         return LOGGER_STATUS.OFF;
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}
/*     */     
/*     */ 
/* 110 */     return LOGGER_STATUS.NULL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ICULogger getICULogger(String name)
/*     */   {
/* 122 */     return getICULogger(name, null);
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
/*     */   public static ICULogger getICULogger(String name, String resourceBundleName)
/*     */   {
/* 136 */     LOGGER_STATUS flag = checkGlobalLoggingFlag();
/* 137 */     if (flag != LOGGER_STATUS.NULL) {
/* 138 */       ICULogger logger = new ICULogger(name, resourceBundleName);
/*     */       
/*     */ 
/* 141 */       logger.addHandler(new ConsoleHandler());
/*     */       
/*     */ 
/* 144 */       if (flag == LOGGER_STATUS.ON) {
/* 145 */         logger.turnOnLogging();
/*     */       } else {
/* 147 */         logger.turnOffLogging();
/*     */       }
/*     */       
/* 150 */       return logger;
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLoggingOn()
/*     */   {
/* 163 */     if (this.currentStatus == LOGGER_STATUS.ON) {
/* 164 */       return true;
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void turnOnLogging()
/*     */   {
/* 177 */     setStatus(LOGGER_STATUS.ON);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void turnOffLogging()
/*     */   {
/* 187 */     setStatus(LOGGER_STATUS.OFF);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICULogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */