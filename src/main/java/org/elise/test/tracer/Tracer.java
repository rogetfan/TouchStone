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
    public boolean isInfoTrace()
    {
       return TracerConfig.getInstance().getConsoleLevel().equals(TracerLevelEnum.INFO);
       
    }
    // Only compared with console level
    public boolean isWarnTrace()
    {
    	return TracerConfig.getInstance().getConsoleLevel().equals(TracerLevelEnum.WARN);
    }
    // Only compared with console level
    public boolean isSpecialTrace()
    {
    	return TracerConfig.getInstance().getConsoleLevel().equals(TracerLevelEnum.SPECIAL);
    }
    // Only compared with console level
    public boolean isErrorTrace()
    {
    	return TracerConfig.getInstance().getConsoleLevel().equals(TracerLevelEnum.ERROR);
    }
    
    
}
