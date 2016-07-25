package com.ibm.icu.impl.duration;

import java.util.TimeZone;

public abstract interface PeriodBuilderFactory
{
  public abstract PeriodBuilderFactory setAvailableUnitRange(TimeUnit paramTimeUnit1, TimeUnit paramTimeUnit2);
  
  public abstract PeriodBuilderFactory setUnitIsAvailable(TimeUnit paramTimeUnit, boolean paramBoolean);
  
  public abstract PeriodBuilderFactory setMaxLimit(float paramFloat);
  
  public abstract PeriodBuilderFactory setMinLimit(float paramFloat);
  
  public abstract PeriodBuilderFactory setAllowZero(boolean paramBoolean);
  
  public abstract PeriodBuilderFactory setWeeksAloneOnly(boolean paramBoolean);
  
  public abstract PeriodBuilderFactory setAllowMilliseconds(boolean paramBoolean);
  
  public abstract PeriodBuilderFactory setLocale(String paramString);
  
  public abstract PeriodBuilderFactory setTimeZone(TimeZone paramTimeZone);
  
  public abstract PeriodBuilder getFixedUnitBuilder(TimeUnit paramTimeUnit);
  
  public abstract PeriodBuilder getSingleUnitBuilder();
  
  public abstract PeriodBuilder getOneOrTwoUnitBuilder();
  
  public abstract PeriodBuilder getMultiUnitBuilder(int paramInt);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\PeriodBuilderFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */