package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public abstract interface DurationFormatter
{
  public abstract String formatDurationFromNowTo(Date paramDate);
  
  public abstract String formatDurationFromNow(long paramLong);
  
  public abstract String formatDurationFrom(long paramLong1, long paramLong2);
  
  public abstract DurationFormatter withLocale(String paramString);
  
  public abstract DurationFormatter withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\DurationFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */