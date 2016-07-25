/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.impl.CurrencyData;
/*    */ import com.ibm.icu.impl.CurrencyData.CurrencyDisplayInfoProvider;
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CurrencyDisplayNames
/*    */ {
/*    */   public static CurrencyDisplayNames getInstance(ULocale locale)
/*    */   {
/* 29 */     return CurrencyData.provider.getInstance(locale, true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean hasData()
/*    */   {
/* 39 */     return CurrencyData.provider.hasData();
/*    */   }
/*    */   
/*    */   public abstract ULocale getLocale();
/*    */   
/*    */   public abstract String getSymbol(String paramString);
/*    */   
/*    */   public abstract String getName(String paramString);
/*    */   
/*    */   public abstract String getPluralName(String paramString1, String paramString2);
/*    */   
/*    */   public abstract Map<String, String> symbolMap();
/*    */   
/*    */   public abstract Map<String, String> nameMap();
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CurrencyDisplayNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */