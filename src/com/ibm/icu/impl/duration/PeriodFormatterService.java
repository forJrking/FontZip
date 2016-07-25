package com.ibm.icu.impl.duration;

import java.util.Collection;

public abstract interface PeriodFormatterService
{
  public abstract DurationFormatterFactory newDurationFormatterFactory();
  
  public abstract PeriodFormatterFactory newPeriodFormatterFactory();
  
  public abstract PeriodBuilderFactory newPeriodBuilderFactory();
  
  public abstract Collection<String> getAvailableLocaleNames();
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\PeriodFormatterService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */