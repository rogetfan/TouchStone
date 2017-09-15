package org.elise.test.config;

import org.elise.test.exception.LoadConfigException;

import java.util.Properties;


/**
 * Created by Glenn on 2016/10/25.
 */

public class HttpStackConfiguration implements Configuration {


    private Integer SocketReceiveBuffer;
    private Integer SocketSendBuffer;
    private Boolean SocketReuseAddress;
    private Boolean TcpNoDelay;
    private Boolean SocketKeepAlive;
    private Boolean TcpAutoRead;
    private Integer ConnectTimeoutMillis;
    private Integer[] ReceiveBufferAllocator = new Integer[3];
    private Integer[] WriteBufferWaterMark = new Integer[2];
    private Integer WorkerGroupThreads;
    private Integer MaxConnCountPerPort;
    private Integer MaxContentLength;
    private static HttpStackConfiguration config;

    private HttpStackConfiguration() {

    }

    public static HttpStackConfiguration getInstance() {
        if (config == null) {
            config = new HttpStackConfiguration();
        }
        return config;
    }

    public void loadConfiguration(Properties prop) throws LoadConfigException {

        SocketReceiveBuffer = Integer.parseInt(prop.getProperty("SocketReceiveBuffer", "64")) * 1024;
        SocketSendBuffer = Integer.parseInt(prop.getProperty("SocketSendBuffer", "256")) * 1024;
        SocketReuseAddress = prop.getProperty("SocketReuseAddress", "1").equals("0") ? false : true;
        TcpNoDelay = prop.getProperty("TcpNoDelay", "1").equals("0") ? false : true;
        SocketKeepAlive = prop.getProperty("SocketKeepAlive", "1").equals("0") ? false : true;
        TcpAutoRead = prop.getProperty("TcpAutoRead", "1").equals("0") ? false : true;
        ConnectTimeoutMillis = Integer.parseInt(prop.getProperty("ConnectTimeoutMillis", "3000"));
        String var1 = prop.getProperty("ReceiveBufferAllocator", "32,64,32768");
        if (var1.split(",").length != 3) {
            throw new LoadConfigException("Length of ReceiveBufferAllocator's Parameters is not 3");
        } else {
            ReceiveBufferAllocator[0] = Integer.parseInt(var1.split(",")[0]) * 1024;
            ReceiveBufferAllocator[1] = Integer.parseInt(var1.split(",")[1]) * 1024;
            ReceiveBufferAllocator[2] = Integer.parseInt(var1.split(",")[2]) * 1024;
        }
        String var2 = prop.getProperty("WriteBufferWaterMark", "32,4096");
        if (var2.split(",").length != 2) {
            throw new LoadConfigException("Length of WriteBufferWaterMark's Parameters is not 2");
        } else {
            WriteBufferWaterMark[0] = Integer.parseInt(var2.split(",")[0]) * 1024;
            WriteBufferWaterMark[1] = Integer.parseInt(var2.split(",")[1]) * 1024;
        }
        WorkerGroupThreads = Integer.parseInt(prop.getProperty("WorkerGroupThreads", "32"));
        MaxConnCountPerPort = Integer.parseInt(prop.getProperty("MaxConnCountPerPort", "128"));
        MaxContentLength = Integer.parseInt(prop.getProperty("MaxContentLength", "8192")) * 1024;

    }

    public Integer[] getWriteBufferWaterMark() {
        return WriteBufferWaterMark;
    }

    public Integer getSocketReceiveBuffer() {
        return SocketReceiveBuffer;
    }

    public Integer getSocketSendBuffer() {
        return SocketSendBuffer;
    }

    public Boolean getSocketReuseAddress() {
        return SocketReuseAddress;
    }

    public Boolean getTcpNoDelay() {
        return TcpNoDelay;
    }

    public Boolean getSocketKeepAlive() {
        return SocketKeepAlive;
    }

    public Boolean getTcpAutoRead() {
        return TcpAutoRead;
    }

    public Integer getConnectTimeoutMillis() {
        return ConnectTimeoutMillis;
    }

    public Integer[] getReceiveBufferAllocator() {
        return ReceiveBufferAllocator;
    }

    public Integer getWorkerGroupThreads() {
        return WorkerGroupThreads;
    }

    public Integer getMaxConnCountPerPort() {
        return MaxConnCountPerPort;
    }

    public Integer getMaxContentLength() {
        return MaxContentLength;
    }

    public HttpStackConfiguration getConfig() {
        return config;
    }
}
