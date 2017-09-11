package org.elise.test.framework.stack.http;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import org.elise.test.framework.transaction.http.HttpResultCallBack;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class TestHttp {

    public static void main(String args[]) throws InterruptedException, UnsupportedEncodingException, URISyntaxException {
        String host = "www.baidu.com";
        String url = "https://www.baidu.com/?tn=47018152_dg";
        HttpClient client = new HttpClient();
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.HOST, host);
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        client.invoke(host,80, url, HttpMethod.GET,headers,  new HttpResultCallBack() {
            @Override
            public void success(String responseMessage, Object object) {
                System.out.println("321");
                System.out.println(responseMessage);
            }

            @Override
            public void error(String responseMessage, Integer statusCode, Object object) {

            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void unreachable() {

            }
        });
        Thread.sleep(5000);
        client.invoke(host,80, url, HttpMethod.GET,headers,  new HttpResultCallBack() {
            @Override
            public void success(String responseMessage, Object object) {
                System.out.println("123");
                System.out.println(responseMessage);
            }

            @Override
            public void error(String responseMessage, Integer statusCode, Object object) {

            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void unreachable() {

            }
        });
        Thread.sleep(50000);
        client.close();

    }
}
