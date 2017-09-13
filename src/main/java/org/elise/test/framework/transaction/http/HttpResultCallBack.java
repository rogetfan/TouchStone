package org.elise.test.framework.transaction.http;

import java.util.Map;

/**
 * Created by Glenn on 2016/10/19.
 */
public interface HttpResultCallBack {
    /**
     * Send request successfully,and get response which contains 2XX
     * */
     void success(Integer statusCode,Object httpContent,Map<String,String> headers);
    /**
     * Send request successfully,and get response which contains 3XX
     * */
     void redirect(Integer statusCode,Object httpContent,Map<String,String> headers);
    /**
     * Send request successfully,but not get response which contains 2xx or 3XX
     * For example 404,501
     * */
     void error(Integer statusCode,Object httpContent,Map<String,String> headers);
    /**
     * The request don't reach remote host which has been sent
     * or some incident take place  when send request
     * */
     void failed(Throwable e);
    /**
     *The Connections can't be established
     * */
     void unreachable();

     long getSequenceNum();

     void setSequenceNum(long sequence);

}
