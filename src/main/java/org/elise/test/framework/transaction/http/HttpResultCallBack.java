package org.elise.test.framework.transaction.http;

/**
 * Created by huxuehan on 2016/10/19.
 */
public interface HttpResultCallBack {
    /**
     * Send request successfully,and get response which contains 2XX
     * */
    public void success(String responseMessage);
    /**
     * Send request successfully,but not get response which contains 2xx
     * For example 404
     * */
    public void error(String responseMessage,Integer statusCode);
    /**
     * The request which has been sent don't reach remote host.
     * */
    public void failed(Exception e);
    /**
     *The request has been sent but no response returned
     * */
    public void unreachable();

}
