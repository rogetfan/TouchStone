package org.elise.test.performance;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.elise.test.config.ConfigLoader;
import org.elise.test.exception.LoadConfigException;
import org.elise.test.framework.FrameworkConfig;
import org.elise.test.framework.stack.http.HttpClient;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.TracerConfig;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

public class TestHttp {

    public static void main(String args[]) throws InterruptedException, UnsupportedEncodingException, URISyntaxException, LoadConfigException {
        ConfigLoader.getInstance().loadProperties("setting.properties", FrameworkConfig.getInstance(), TracerConfig.getInstance());
        final long begin = System.currentTimeMillis();
        HttpClient.initialize();
        HttpClient client = HttpClient.getInstance();
//        String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
//        DefaultHttpHeaders postHeader = new DefaultHttpHeaders();
//        postHeader.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
//        postHeader.set(HttpHeaderNames.HOST, "177.77.77.186");
//        postHeader.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        for (int i = 0; i < 1000; i++) {
//            client.invokePost(
//                    "http://177.77.77.186:8082/api/loginV1_2",
//                    postHeader,
//                    postBody.getBytes("UTF-8"),
//                    new HttpResultCallBack() {
//                        long sequence;
//
//
//                        @Override
//                        public void success(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                        }
//
//                        @Override
//                        public void redirect(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                        }
//
//                        @Override
//                        public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                        }
//
//                        @Override
//                        public void failed(Throwable t) {
//
//                        }
//
//                        @Override
//                        public void unreachable() {
//
//                        }
//
//                        @Override
//                        public long getSequenceNum() {
//                            return sequence;
//                        }
//
//                        @Override
//                        public void setSequenceNum(long sequence) {
//                            this.sequence = sequence;
//                        }
//                    });
//        }

        DefaultHttpHeaders Header = new DefaultHttpHeaders();
        Header.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
        Header.set(HttpHeaderNames.HOST, "www.cnblogs.com");
        Header.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        client.invokeGet(
                "http://www.cnblogs.com/guogangj/p/5462594.html",
                Header,
                new HttpResultCallBack() {
                    long sequence;


                    @Override
                    public void success(Integer statusCode, Object httpContent, Map<String, String> headers) {
                        System.out.println(System.currentTimeMillis()-begin);
                    }

                    @Override
                    public void redirect(Integer statusCode, Object httpContent, Map<String, String> headers) {

                    }

                    @Override
                    public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {

                    }

                    @Override
                    public void failed(Throwable t) {

                    }

                    @Override
                    public void unreachable() {

                    }

                    @Override
                    public long getSequenceNum() {
                        return sequence;
                    }

                    @Override
                    public void setSequenceNum(long sequence) {
                        this.sequence = sequence;
                    }
                });


        Thread.sleep(60 * 1000);
        client.close();

    }
}
