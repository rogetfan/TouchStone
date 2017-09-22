package org.elise.test.framework.stack;

/**
 * Created by Glenn on  2017/9/18 0018 21:25.
 */

public interface Connection {

     void invoke(Object request,Object attachment) throws Throwable;
     void connect() throws Throwable;

}
