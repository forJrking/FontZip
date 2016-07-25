package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public abstract interface DateFormatter
{
  public abstract String format(Date paramDate);
  
  public abstract String format(long paramLong);
  
  public abstract DateFormatter withLocale(String paramString);
  
  public abstract DateFormatter withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\DateFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */