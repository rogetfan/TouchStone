package com.linekong.platform.tracer;

import java.util.Properties;

import com.linekong.platform.config.Configuration;

public class TracerConfig implements Configuration {

	private TracerLevelEnum console;
	private TracerLevelEnum remote;
	private TracerLevelEnum file;

	private static TracerConfig config;

	private TracerConfig() {

	}

	public static TracerConfig getInstance() {
		if (config == null) {
			config = new TracerConfig();
		}

		return config;
	}

	public void loadConfiguration(Properties prop) {
		console = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("consoleWriterLevel", "1")));
		remote = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("remoteWriterLevel", "1")));
		file = TracerLevelEnum.parseFromInt(Integer.parseInt(prop.getProperty("fileWriterLevel", "1")));

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
