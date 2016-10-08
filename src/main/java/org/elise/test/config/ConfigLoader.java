package org.elise.test.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.elise.test.exception.LoadConfigException;

public class ConfigLoader {
	private static ConfigLoader loader;

	private ConfigLoader() {

	}

	public static ConfigLoader getInstance() {
		if (loader == null) {
			loader = new ConfigLoader();
		}
		return loader;
	}

	public void loadFile(String filePath,Configuration ... configs) throws LoadConfigException {
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);
			loadProperties(prop);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Read Configuration From File Failed");
			throw  new LoadConfigException("");

		} catch (IOException e) {
			System.err.println("Read Propertities From File Failed");
			e.printStackTrace();
			throw  new LoadConfigException("");
		}
	}
	private static void loadProperties(Properties prop) 
	{
          
	}
}
