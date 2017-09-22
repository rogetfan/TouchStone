package org.elise.test.config;

import org.elise.test.exception.LoadConfigException;

import java.util.Properties;

/**
 * Created by Glenn on  2017/9/22 0022 11:24.
 */


public class TransactionConfiguration implements Configuration {

    private static TransactionConfiguration config;
    private Integer CorePoolSize;
    private Integer MaximumPoolSize;
    private Integer MaxTaskQueueCapacity;

    private TransactionConfiguration() {

    }

    public static TransactionConfiguration getInstance() {
        if (config == null) {
            config = new TransactionConfiguration();
        }
        return config;
    }


    @Override
    public void loadConfiguration(Properties prop) throws LoadConfigException {
        CorePoolSize = Integer.parseInt(prop.getProperty("CorePoolSize", "4"));
        MaximumPoolSize = Integer.parseInt(prop.getProperty("MaximumPoolSize", "128"));
        MaxTaskQueueCapacity = Integer.parseInt(prop.getProperty("MaxTaskQueueCapacity", "16384"));
    }

    public Integer getCorePoolSize() {
        return CorePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return MaximumPoolSize;
    }

    public Integer getMaxTaskQueueCapacity() {
        return MaxTaskQueueCapacity;
    }
}
