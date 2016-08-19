package com.linekong.platform.tracer;

public enum TracerLevelEnum 
{
    INFO("info",1),
    WARN("warn",2),
    ERROR("error",3),
    SPECIAL("specail",4);
    
    private String levelName;
    private Integer levelValue;
    private TracerLevelEnum(String levelName,Integer levelValue)
    {
    	this.levelName = levelName;
    	this.levelValue = levelValue;
    }
    
    public String getLevelName()
    {
    	return levelName;
    }
    
    public Integer getLevelVaule()
    {
    	return levelValue;
    }
}
