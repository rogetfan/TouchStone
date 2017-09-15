package org.elise.test.performance;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.elise.test.config.ConfigLoader;
import org.elise.test.config.HttpStackConfiguration;
import org.elise.test.config.TracerConfiguration;
import org.elise.test.framework.stack.http.HttpClient;
import org.elise.test.framework.stack.http.HttpConnection;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttp {

    public static final Tracer TRACER = Tracer.getInstance(TestHttp.class);

    public static final AtomicLong counter = new AtomicLong();

    public static void main(String args[]) throws Throwable {
        try {
            ConfigLoader.getInstance().loadProperties("setting.properties", HttpStackConfiguration.getInstance(), TracerConfiguration.getInstance());
            HttpClient.start();

            URI uri = new URI("http://177.77.77.186:8082/api/loginV1_2");
            String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
            DefaultHttpHeaders postHeader = new DefaultHttpHeaders();
            postHeader.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
            postHeader.set(HttpHeaderNames.HOST, "177.77.77.186");
            postHeader.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            for (int i = 0; ; i++) {
                try {
                    HttpConnection conn = HttpClient.getInstance(i, uri);
                    conn.invokePost(
                            uri,
                            postHeader,
                            postBody.getBytes("UTF-8"),
                            new HttpResultCallBack() {
                                long sequence;


                                @Override
                                public void success(Integer statusCode, Object httpContent, Map<String, String> headers) {
                                    counter.incrementAndGet();
                                }

                                @Override
                                public void redirect(Integer statusCode, Object httpContent, Map<String, String> headers) {

                                }

                                @Override
                                public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {

                                }

                                @Override
                                public void failed(Throwable t) {
                                    TRACER.writeError("",t);
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
                    Thread.sleep( 10);
                } catch (Throwable t) {
                    TRACER.writeError("", t);
                }
            }
//        URI uri = new URI("http://www.cnblogs.com/likaitai/p/5431246.html");
//        DefaultHttpHeaders postHeader = new DefaultHttpHeaders();
//        postHeader.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
//        postHeader.set(HttpHeaderNames.HOST, "www.cnblogs.com");
//        postHeader.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        for(int i=0;i<1280;i++) {
//            try {
//                HttpConnection conn = HttpClient.getInstance(i,uri);
//                conn.invokeGet(
//                        uri,
//                        postHeader,
//                        new HttpResultCallBack() {
//                            long sequence;
//
//
//                            @Override
//                            public void success(Integer statusCode, Object httpContent, Map<String, String> headers) {
//                                counter.incrementAndGet();
//                            }
//
//                            @Override
//                            public void redirect(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                            }
//
//                            @Override
//                            public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                            }
//
//                            @Override
//                            public void failed(Throwable t) {
//                                TRACER.writeError("",t);
//                            }
//
//                            @Override
//                            public void unreachable() {
//
//                            }
//
//                            @Override
//                            public long getSequenceNum() {
//                                return sequence;
//                            }
//
//                            @Override
//                            public void setSequenceNum(long sequence) {
//                                this.sequence = sequence;
//                            }
//                        });
//            }catch (Throwable t){
//                TRACER.writeError("",t);
//            }
//        }




        }finally {
            System.out.println(counter.get());
            HttpClient.close();
        }
    }
}
