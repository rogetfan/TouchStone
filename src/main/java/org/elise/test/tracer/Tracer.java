package org.elise.test.tracer;

import org.elise.test.tracer.writer.ConsoleWriter;
import org.elise.test.tracer.writer.FileWriter;
import org.elise.test.tracer.writer.RemoteWriter;


/**
 * Created by Glenn on 2016/10/25.
 */
public class Tracer
{
    private static ConsoleWriter  consoleWriter = null;
    private static FileWriter fileWriter = null;
    private static RemoteWriter remoteWriter = null;
	private String className;

    private Tracer(Class<?> clazz) {
    	this.className = clazz.getSimpleName();
        if(consoleWriter == null) {
            consoleWriter = new ConsoleWriter();
        }
        if(fileWriter == null){
            fileWriter = new FileWriter();
        }
        if(remoteWriter == null){
            remoteWriter = new RemoteWriter();
        }
    }
    
    public static Tracer getInstance(Class<?> clazz) {
    	return new Tracer(clazz);
    }
	
    // Only compared with console level
    public boolean isInfoAvailable() {
       return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.INFO);
    }
    // Only compared with console level
    public boolean isWarnAvailable() {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.WARN);
    }
    // Only compared with console level
    public boolean isErrorAvailable() {
    	return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.ERROR);
    }
    // Only compared with console level
    public boolean isSpecialAvailable() {
        return TracerConfig.getInstance().getConsoleLevel().compare(TracerLevelEnum.SPECIAL);
    }
    private void writeTracerObject(TracerObject log) {
        if(TracerConfig.getInstance().getConsoleLevel().compare(log.getLogLevel())){
            consoleWriter.write(log);
        }
        if(TracerConfig.getInstance().getFileLevel().compare(log.getLogLevel())){

        }
        if(TracerConfig.getInstance().getRemoteLevel().compare(log.getLogLevel())){

        }
    }
    public void writeInfo(String message) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.INFO,null,message);
        writeTracerObject(object);
    }
    public void writeWarn(String message) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.WARN,null,message);
        writeTracerObject(object);
    }
    public void writeWarn(String message,Throwable t) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.WARN,t,message);
        writeTracerObject(object);
    }
    public void writeError(String message) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.ERROR,null,message);
        writeTracerObject(object);
    }
    public void writeError(String message,Throwable t) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.ERROR,t,message);
        writeTracerObject(object);
    }
    public void writeSpecial(String message) {
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.SPECIAL,null,message);
        writeTracerObject(object);
    }
    public void writeSpecial(String message,Throwable t){
        TracerObject object = new TracerObject(System.currentTimeMillis(),className,Thread.currentThread().getName(),TracerLevelEnum.SPECIAL,t,message);
        writeTracerObject(object);
    }
}