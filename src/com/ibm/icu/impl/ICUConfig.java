/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.security.AccessControlException;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ICUConfig
/*    */ {
/*    */   public static final String CONFIG_PROPS_FILE = "/com/ibm/icu/ICUConfig.properties";
/* 25 */   private static final Properties CONFIG_PROPS = new Properties();
/*    */   
/* 27 */   static { try { InputStream is = ICUData.getStream("/com/ibm/icu/ICUConfig.properties");
/* 28 */       if (is != null) {
/* 29 */         CONFIG_PROPS.load(is);
/*    */       }
/*    */     }
/*    */     catch (MissingResourceException mre) {}catch (IOException ioe) {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String get(String name)
/*    */   {
/* 44 */     return get(name, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String get(String name, String def)
/*    */   {
/* 55 */     String val = null;
/* 56 */     String fname = name;
/* 57 */     if (System.getSecurityManager() != null) {
/*    */       try {
/* 59 */         val = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*    */           public String run() {
/* 61 */             return System.getProperty(this.val$fname);
/*    */           }
/*    */           
/*    */ 
/*    */         });
/*    */       }
/*    */       catch (AccessControlException e) {}
/*    */     } else {
/* 69 */       val = System.getProperty(name);
/*    */     }
/*    */     
/* 72 */     if (val == null) {
/* 73 */       val = CONFIG_PROPS.getProperty(name, def);
/*    */     }
/* 75 */     return val;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */