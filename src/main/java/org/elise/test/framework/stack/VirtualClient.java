package org.elise.test.framework.stack;

/**
 * Created by Glenn on  2017/9/18 0018 21:26.
 */


public interface VirtualClient {

    void start() throws Throwable;
    void close() throws Throwable;
    Connection getConnection(Integer flag,Object host) throws Throwable;

}
