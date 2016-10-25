package org.elise.test.tracer;

import java.util.Properties;
import org.elise.test.config.Configuration;

public class TracerConfig implements Configuration {

	private static TracerConfig config;
	private TracerLevelEnum console;
	private TracerLevelEnum remote;
	private TracerLevelEnum file;

	private TracerConfig() {

	}

	public static TracerConfig getInstance() {
		if (config == null) {
			config = new TracerConfig();
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
