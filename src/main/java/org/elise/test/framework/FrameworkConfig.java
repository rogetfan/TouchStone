package org.elise.test.framework;

import java.util.Properties;
import org.elise.test.config.Configuration;

public class FrameworkConfig implements Configuration
{
	private static FrameworkConfig config;
	private static Integer virtualUserCount;
	private static Long maxIntervalTimeStamp;

	private FrameworkConfig() {

	}
	
    public static FrameworkConfig getInstance() {
         if(config == null)
         {
        	 config = new FrameworkConfig();
         }
         return config;
    }

	public void loadConfiguration(Properties prop) {
		virtualUserCount = Integer.parseInt(prop.getProperty("VirtualUserCount","10"));
		maxIntervalTimeStamp = Long.parseLong(prop.getProperty("MaxIntervalTimeStamp","2000"));
	}

	public Integer getVirtualUserCount() {
		return virtualUserCount;
	}

	public Long getMaxIntervalTimeStamp() {
		return maxIntervalTimeStamp;
	}
}
