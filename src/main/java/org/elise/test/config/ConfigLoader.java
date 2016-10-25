package org.elise.test.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.elise.test.exception.LoadConfigException;
import org.elise.test.tracer.Tracer;

public class ConfigLoader {
	private static ConfigLoader loader;
	private static Tracer tracer = Tracer.getInstance(ConfigLoader.class);
	private ConfigLoader() {

	}

	public static ConfigLoader getInstance() {
		if (loader == null) {
			loader = new ConfigLoader();
		}
		return loader;
	}

	public void loadProperties(String filePath,Configuration ... configs) throws LoadConfigException {
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);
		} catch (FileNotFoundException e) {
			tracer.writeError("File not be found",e);
			throw  new LoadConfigException("");

		} catch (IOException e) {
			tracer.writeError("Read Properties From File Failed",e);
			throw  new LoadConfigException("");
		}
		for(Configuration config:configs)
             config.loadConfiguration(prop);
		tracer.writeSpecial("Load Configurations Successful");
	}
//	public static void main(String args[])
//	{
//		TracerConfig.getInstance().loadConfiguration(new Properties());
//		FrameworkConfig.getInstance().loadConfiguration(new Properties());
//		System.out.println(TracerConfig.getInstance().getConsoleLevel());
//		System.out.println(TracerConfig.getInstance().getFileLevel());
//		System.out.println(TracerConfig.getInstance().getRemoteLevel());
//		System.out.println(FrameworkConfig.getInstance().getMaxIntervalTimeStamp());
//		System.out.println(FrameworkConfig.getInstance().getVirtualUserCount());
//	}
}


