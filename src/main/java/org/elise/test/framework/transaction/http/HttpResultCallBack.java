package org.elise.test.framework.transaction.http;

/**
 * Created by huxuehan on 2016/10/19.
 */
public interface HttpResultCallBack {
    /**
     * 请求发送成功返回2XX应答
     * */
    public void success(String responseMessage);
    /**
     * 请求发送成功但未返回2XX应答
     * 例如返回404
     * */
    public void error(String responseMessage,Integer statusCode);
    /**
     *HTTP请求发送失败，未到达对端主机
     * */
    public void failed(Exception e);
    /**
     *HTTP请求发送，未收到应答响应
     * */
    public void unreachable();

}
