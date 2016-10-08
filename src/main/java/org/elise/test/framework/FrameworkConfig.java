package org.elise.test.framework;

import java.util.Properties;

import org.elise.test.config.Configuration;

public class FrameworkConfig implements Configuration
{
	private static FrameworkConfig config;
	private FrameworkConfig()
	{
		
	}
	
    public static FrameworkConfig getInstance()
    {
         if(config == null)
         {
        	 config = new FrameworkConfig();
         }
         return config;
    }
	
	public void loadConfiguration(Properties prop) 
	{
			
	}
     	
}
