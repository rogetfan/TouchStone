package org.elise.test.framework;

import java.util.Properties;
import org.elise.test.config.Configuration;

public class FrameworkConfig implements Configuration
{
	private static FrameworkConfig config;
	private static Integer VirtualUserGroup;
	private static Integer GroupCount;
	private static Integer TranDispatcherCount;

	private static Long maxIntervalTimeStamp;

	private FrameworkConfig() {

	}
	
    public static FrameworkConfig getInstance() {
         if(config == null) {
        	 config = new FrameworkConfig();
         }
         return config;
    }

	public void loadConfiguration(Properties prop) {
		VirtualUserGroup = Integer.parseInt(prop.getProperty("VirtualUserGroup","10"));
		GroupCount = Integer.parseInt(prop.getProperty("GroupCount","1000"));
		maxIntervalTimeStamp = Long.parseLong(prop.getProperty("MaxIntervalTimeStamp","2000"));
		TranDispatcherCount=Integer.parseInt(prop.getProperty("TranDispatcherCount","2000"));
	}

	public static Integer getVirtualUserGroup() {
		return VirtualUserGroup;
	}

	public static Integer getGroupCount() {
		return GroupCount;
	}
	public static Integer getTranDispatcherCount() {
		return TranDispatcherCount;
	}
	public Long getMaxIntervalTimeStamp() {
		return maxIntervalTimeStamp;
	}
}
