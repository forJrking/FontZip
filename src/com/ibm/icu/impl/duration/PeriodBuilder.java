package com.ibm.icu.impl.duration;

import java.util.TimeZone;

public abstract interface PeriodBuilder
{
  public abstract Period create(long paramLong);
  
  public abstract Period createWithReferenceDate(long paramLong1, long paramLong2);
  
  public abstract PeriodBuilder withLocale(String paramString);
  
  public abstract PeriodBuilder withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\PeriodBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */