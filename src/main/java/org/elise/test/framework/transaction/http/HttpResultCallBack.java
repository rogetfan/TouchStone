package org.elise.test.framework.transaction.http;

/**
 * Created by huxuehan on 2016/10/19.
 */
public interface HttpResultCallBack {
    /**
     * Send request successfully,and get response which contains 2XX
     * */
     void success(String responseMessage,Object object);
    /**
     * Send request successfully,but not get response which contains 2xx
     * For example 404
     * */
     void error(String responseMessage,Integer statusCode,Object object);
    /**
     * The request which has been sent don't reach remote host.
     * */
     void failed(Exception e);
    /**
     *The request has been sent but no response returned
     * */
     void unreachable();

}
