package org.elise.test.config;

import org.elise.test.exception.LoadConfigException;

import java.util.Properties;

public interface Configuration 
{
    void loadConfiguration(Properties prop) throws LoadConfigException;
}
