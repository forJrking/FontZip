package com.ibm.icu.impl.duration;

public abstract interface PeriodFormatter
{
  public abstract String format(Period paramPeriod);
  
  public abstract PeriodFormatter withLocale(String paramString);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\PeriodFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */