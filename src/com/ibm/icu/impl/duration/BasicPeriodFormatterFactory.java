/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
/*     */ import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ public class BasicPeriodFormatterFactory
/*     */   implements PeriodFormatterFactory
/*     */ {
/*     */   private final PeriodFormatterDataService ds;
/*     */   private PeriodFormatterData data;
/*     */   private Customizations customizations;
/*     */   private boolean customizationsInUse;
/*     */   private String localeName;
/*     */   
/*     */   BasicPeriodFormatterFactory(PeriodFormatterDataService ds)
/*     */   {
/*  64 */     this.ds = ds;
/*  65 */     this.customizations = new Customizations();
/*  66 */     this.localeName = Locale.getDefault().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicPeriodFormatterFactory getDefault()
/*     */   {
/*  75 */     return (BasicPeriodFormatterFactory)BasicPeriodFormatterService.getInstance().newPeriodFormatterFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setLocale(String localeName)
/*     */   {
/*  83 */     this.data = null;
/*  84 */     this.localeName = localeName;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setDisplayLimit(boolean display)
/*     */   {
/*  95 */     updateCustomizations().displayLimit = display;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getDisplayLimit()
/*     */   {
/* 105 */     return this.customizations.displayLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setDisplayPastFuture(boolean display)
/*     */   {
/* 115 */     updateCustomizations().displayDirection = display;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getDisplayPastFuture()
/*     */   {
/* 125 */     return this.customizations.displayDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setSeparatorVariant(int variant)
/*     */   {
/* 135 */     updateCustomizations().separatorVariant = ((byte)variant);
/* 136 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSeparatorVariant()
/*     */   {
/* 145 */     return this.customizations.separatorVariant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setUnitVariant(int variant)
/*     */   {
/* 155 */     updateCustomizations().unitVariant = ((byte)variant);
/* 156 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getUnitVariant()
/*     */   {
/* 165 */     return this.customizations.unitVariant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatterFactory setCountVariant(int variant)
/*     */   {
/* 175 */     updateCustomizations().countVariant = ((byte)variant);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCountVariant()
/*     */   {
/* 185 */     return this.customizations.countVariant;
/*     */   }
/*     */   
/*     */   public PeriodFormatter getFormatter() {
/* 189 */     this.customizationsInUse = true;
/* 190 */     return new BasicPeriodFormatter(this, this.localeName, getData(), this.customizations);
/*     */   }
/*     */   
/*     */   private Customizations updateCustomizations()
/*     */   {
/* 195 */     if (this.customizationsInUse) {
/* 196 */       this.customizations = this.customizations.copy();
/* 197 */       this.customizationsInUse = false;
/*     */     }
/* 199 */     return this.customizations;
/*     */   }
/*     */   
/*     */   PeriodFormatterData getData()
/*     */   {
/* 204 */     if (this.data == null) {
/* 205 */       this.data = this.ds.get(this.localeName);
/*     */     }
/* 207 */     return this.data;
/*     */   }
/*     */   
/*     */   PeriodFormatterData getData(String locName)
/*     */   {
/* 212 */     return this.ds.get(locName);
/*     */   }
/*     */   
/*     */   static class Customizations
/*     */   {
/* 217 */     boolean displayLimit = true;
/* 218 */     boolean displayDirection = true;
/* 219 */     byte separatorVariant = 2;
/* 220 */     byte unitVariant = 0;
/* 221 */     byte countVariant = 0;
/*     */     
/*     */     public Customizations copy() {
/* 224 */       Customizations result = new Customizations();
/* 225 */       result.displayLimit = this.displayLimit;
/* 226 */       result.displayDirection = this.displayDirection;
/* 227 */       result.separatorVariant = this.separatorVariant;
/* 228 */       result.unitVariant = this.unitVariant;
/* 229 */       result.countVariant = this.countVariant;
/* 230 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicPeriodFormatterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */