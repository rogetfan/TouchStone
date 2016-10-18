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
    public boolean isErrorAvailable()
    {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.ERROR);
    }
    // Only compared with console level
    public boolean isSpecialAvailable()
    {
        return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.SPECIAL);
    }
    private void writeTracerObject(TracerObject log) {

    }
    public void writeInfo(String message) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.INFO,null,message);
        writeTracerObject(object);
    }
    public void writeWarn(String message) {

    }
    public void writeWarn(String message,Exception e){

    }
    public void writeWarn(String message,Throwable t) {

    }
    public void writeError(String message) {

    }
    public void writeError(String message,Exception e){

    }
    public void writeError(String message,Throwable t) {

    }
    public void writeSpecial(String message) {

    }
    public void writeSpecial(String message,Exception e){

    }
    public void writeSpecial(String message,Throwable t){

    }
}
