package org.elise.test.config;

import java.util.Properties;

import org.elise.test.tracer.TracerLevelEnum;


/**
 * Created by Glenn on 2016/10/25.
 */
public class TracerConfiguration implements Configuration {

	private static TracerConfiguration config;
	private TracerLevelEnum console;
	private TracerLevelEnum remote;
	private TracerLevelEnum file;

	private TracerConfiguration() {

	}

	public static TracerConfiguration getInstance() {
		if (config == null) {
			config = new TracerConfiguration();
		}
		return config;
	}

	public void loadConfiguration(Properties prop) {
		console = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("ConsoleWriterLevel", "1")));
		remote = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("RemoteWriterLevel", "1")));
		file = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("FileWriterLevel", "1")));

	}


	public TracerLevelEnum getConsoleLevel() {
		return console;
	}

	public TracerLevelEnum getRemoteLevel() {
		return remote;
	}

	public TracerLevelEnum getFileLevel() {
		return file;
	}
}
