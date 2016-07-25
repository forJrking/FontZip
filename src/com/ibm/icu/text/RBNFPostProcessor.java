package com.ibm.icu.text;

abstract interface RBNFPostProcessor
{
  public abstract void init(RuleBasedNumberFormat paramRuleBasedNumberFormat, String paramString);
  
  public abstract void process(StringBuffer paramStringBuffer, NFRuleSet paramNFRuleSet);
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBNFPostProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */