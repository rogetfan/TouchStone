package org.elise.test.framework.transaction.http;

/**
 * Created by Glenn on 2016/10/19.
 */
public interface HttpResultCallBack {
    /**
     * Send request successfully,and get response which contains 2XX
     * */
     void success(String responseMessage,Object object);
    /**
     * Send request successfully,and get response which contains 3XX
     * */
     void redirect(String responseMessage,Integer statusCode,Object object);
    /**
     * Send request successfully,but not get response which contains 2xx or 3XX
     * For example 404,501
     * */
     void error(String responseMessage,Integer statusCode,Object object);
    /**
     * The request don't reach remote host which has been sent
     * or some incident take place  when send request
     * */
     void failed(Throwable e);
    /**
     *The Connections can't be established
     * */
     void unreachable();

}
