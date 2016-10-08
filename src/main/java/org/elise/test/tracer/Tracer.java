package org.elise.test.tracer;

public class Tracer 
{
	private String className;
	
    private Tracer(Class<?> clazz)
    {
    	this.className = clazz.getSimpleName();
    }
    
    public static Tracer getInstance(Class<?> clazz)
    {
    	return new Tracer(clazz);
    }
	
    // Only compared with console level
    public boolean isInfoAvailable()
    {
       return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.INFO);
    }
    // Only compared with console level
    public boolean isWarnAvailable()
    {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.WARN);
    }
    // Only compared with console level
    public boolean isSpecialAvailable()
    {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.SPECIAL);
    }
    // Only compared with console level
    public boolean isErrorAvailable()
    {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.ERROR);
    }
    
    
}
