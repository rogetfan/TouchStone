package org.elise.test.framework;

import java.util.Properties;
import org.elise.test.config.Configuration;


/**
 * Created by Glenn on 2016/10/25.
 */

public class FrameworkConfig implements Configuration
{
	private static FrameworkConfig config;
	private static Integer virtualUserGroup;
	private static Integer groutpUserCount;
	private static Integer tranDispatcherCount;
	private static Integer netWorkSenderCount;
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
		virtualUserGroup = Integer.parseInt(prop.getProperty("VirtualUserGroup","10"));
		groutpUserCount = Integer.parseInt(prop.getProperty("GroupUserCount","1000"));
		maxIntervalTimeStamp = Long.parseLong(prop.getProperty("MaxIntervalTimeStamp","2000"));
		tranDispatcherCount=Integer.parseInt(prop.getProperty("TranDispatcherCount","2000"));
		netWorkSenderCount = Integer.parseInt(prop.getProperty("NetWorkSenderCount","10"));
	}

	public static Integer getVirtualUserGroup() {
		return virtualUserGroup;
	}

	public static Integer getGroutpUserCount() {
		return groutpUserCount;
	}

	public static Integer getTranDispatcherCount() {
		return tranDispatcherCount;
	}

	public static Integer getNetWorkSenderCount() {
		return netWorkSenderCount;
	}

	public Long getMaxIntervalTimeStamp() {
		return maxIntervalTimeStamp;
	}

}
