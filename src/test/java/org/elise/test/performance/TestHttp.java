package org.elise.test.performance;

import org.elise.test.config.ConfigLoader;
import org.elise.test.config.HttpStackConfiguration;
import org.elise.test.config.TracerConfiguration;
import org.elise.test.framework.stack.http.EliseHttpClient;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.framework.transaction.TransactionExecutor;
import org.elise.test.framework.transaction.http.EliseHttpRequest;
import org.elise.test.framework.transaction.http.HttpTransFuture;
import org.elise.test.framework.transaction.http.TransactionManager;
import org.elise.test.tracer.Tracer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttp {

    public static final Tracer TRACER = Tracer.getInstance(TestHttp.class);

    public static final AtomicLong counter = new AtomicLong();

    public static void main(String args[]) throws Throwable {
        try {
            ConfigLoader.getInstance().loadProperties("setting.properties", HttpStackConfiguration.getInstance(), TracerConfiguration.getInstance());
            EliseHttpClient.start();
            TransactionExecutor executor = new TransactionExecutor(4, 64, 10240);
            TransactionManager manager = new TransactionManager(EliseHttpClient.getInstance());

//            URI uri = new URI("http://177.77.77.186:8084/inner/login");
//            String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
//            DefaultHttpHeaders postHeader = new DefaultHttpHeaders();
//            postHeader.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
//            for (int i = 0; ; i++) {
//                try {
//                    EliseHttpConnection conn = EliseHttpClient.getInstance(i, uri);
//                    conn.invokePost(
//                            uri,
//                            postHeader,
//                            postBody.getBytes("UTF-8"),
//                            new HttpResultCallBack() {
//                                long sequence;
//
//
//                                @Override
//                                public void success(Integer statusCode, Object httpContent, Map<String, String> headers) {
//                                    counter.incrementAndGet();
//                                }
//
//                                @Override
//                                public void redirect(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                                }
//
//                                @Override
//                                public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {
//
//                                }
//
//                                @Override
//                                public void failed(Throwable t) {
//                                    TRACER.writeError("",t);
//                                }
//
//                                @Override
//                                public void unreachable() {
//
//                                }
//
//                                @Override
//                                public long getSequenceNum() {
//                                    return sequence;
//                                }
//
//                                @Override
//                                public void setSequenceNum(long sequence) {
//                                    this.sequence = sequence;
//                                }
//                            });
//                    Thread.sleep( 10);
//                } catch (Throwable t) {
//                    TRACER.writeError("", t);
//                }
//            }


//            URI login = new URI("http://177.77.77.186:8084/inner/login");
//            String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
//            Map<String, String> loginHeader = new HashMap<>();
//            loginHeader.put("content-type", "application/x-www-form-urlencoded");
//            try {
//                for (int i = 0; ; i++) {
//                    Transaction transaction = manager.create(i);
//                    EliseHttpRequest request = new EliseHttpRequest(login, loginHeader,postBody);
//                    request.setMethodPost();
//                    transaction.setRequest(request);
//                    transaction.future = new HttpTransFuture() {
//
//                        @Override
//                        public void redirect(Integer statusCode, byte[] httpContent, Map<String, String> headers) {
//                            TRACER.writeInfo("123456");
//                        }
//
//                        @Override
//                        public void success(Integer statusCode, byte[] httpContent, Map<String, String> headers) {
//                            TRACER.writeInfo("654321");
//                        }
//
//                        @Override
//                        public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {
//                            TRACER.writeError("321654");
//                        }
//
//                        @Override
//                        public void failed(Throwable e) {
//                            TRACER.writeError("", e);
//                        }
//
//                        @Override
//                        public void unreachable() {
//
//                        }
//                    };
//                    executor.exec(transaction);
//                    Thread.sleep(10);
//                }
//            } catch (Throwable t) {
//            }

            URI uri = new URI("http://177.77.77.186:8082/api/captcha-image");
            Map<String, String> header = new HashMap<>();
            header.put("accept", "*/*");
            header.put("cache-control", "no-cache");
            try {
                for (int i = 0; ; i++) {
                    Transaction transaction = manager.create(i);
                    EliseHttpRequest request = new EliseHttpRequest(uri, header);
                    transaction.setRequest(request);
                    transaction.future = new HttpTransFuture() {

                        @Override
                        public void redirect(Integer statusCode, byte[] httpContent, Map<String, String> headers) {
                            TRACER.writeError("123456");
                        }

                        @Override
                        public void success(Integer statusCode, byte[] httpContent, Map<String, String> headers) {
                            TRACER.writeInfo("654321");
                        }

                        @Override
                        public void error(Integer statusCode, Object httpContent, Map<String, String> headers) {
                            TRACER.writeError("321654");
                        }

                        @Override
                        public void failed(Throwable e) {
                            TRACER.writeError("", e);
                        }

                        @Override
                        public void unreachable() {

                        }
                    };
                    executor.exec(transaction);
                    Thread.sleep(10);
                }
            } catch (Throwable t) {
            }
        } finally {
            Thread.sleep(60000);
            System.out.println(counter.get());
            EliseHttpClient.close();
        }
    }
}
