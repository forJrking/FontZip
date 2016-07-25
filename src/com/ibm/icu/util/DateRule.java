package com.ibm.icu.util;

import java.util.Date;

public abstract interface DateRule
{
  public abstract Date firstAfter(Date paramDate);
  
  public abstract Date firstBetween(Date paramDate1, Date paramDate2);
  
  public abstract boolean isOn(Date paramDate);
  
  public abstract boolean isBetween(Date paramDate1, Date paramDate2);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\DateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */