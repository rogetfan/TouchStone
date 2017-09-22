package org.elise.test.performance;

import org.elise.test.config.ConfigLoader;
import org.elise.test.config.HttpStackConfiguration;
import org.elise.test.config.TracerConfiguration;
import org.elise.test.config.TransactionConfiguration;
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
            ConfigLoader.getInstance().loadProperties("setting.properties",
                    HttpStackConfiguration.getInstance(),
                    TracerConfiguration.getInstance(),
                    TransactionConfiguration.getInstance());

            TransactionExecutor executor = new TransactionExecutor(EliseHttpClient.getInstance());
            EliseHttpClient.getInstance().setExecutor(executor);
            EliseHttpClient.getInstance().start();
            TransactionManager manager = new TransactionManager(EliseHttpClient.getInstance());
            System.out.println("123456");
            URI login = new URI("http://177.77.77.186:8084/inner/login");
            String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
            Map<String, String> loginHeader = new HashMap<>();
            loginHeader.put("content-type", "application/x-www-form-urlencoded");
            try {
                Runnable r = () -> {
                    for (int i = 0; ; i++) {
                        Transaction transaction = manager.create(i);
                        EliseHttpRequest request = new EliseHttpRequest(login, loginHeader,postBody);
                        request.setMethodPost();
                        transaction.setRequest(request);
                        transaction.future = new HttpTransFuture() {


                            @Override
                            public void failed(Throwable e, Long usedTimeStamp) {
                                TRACER.writeError("", e);
                            }

                            @Override
                            public void unreachable(Long usedTimeStamp) {

                            }

                            @Override
                            public void redirect(Integer statusCode, byte[] httpContent, Map<String, String> headers, Long usedTimeStamp) {
                                TRACER.writeError("123456");
                            }

                            @Override
                            public void success(Integer statusCode, byte[] httpContent, Map<String, String> headers, Long usedTimeStamp) {
                                TRACER.writeSpecial(usedTimeStamp.toString());
                            }

                            @Override
                            public void error(Integer statusCode, Object httpContent, Map<String, String> headers, Long usedTimeStamp) {
                                TRACER.writeError("321654");
                            }
                        };
                        executor.execTransaction(transaction);
                        try {
                            Thread.sleep(35L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread t1 = new Thread(r);
                Thread t2 = new Thread(r);
                Thread t3 = new Thread(r);
                Thread t4 = new Thread(r);
                t1.start();
                t2.start();
                t3.start();
                t4.start();

            } catch (Throwable t) {

            }

//            URI uri = new URI("http://zikao.hqjy.com/");
//            Map<String, String> header = new HashMap<>();
//            header.put("accept", "*/*");
//            header.put("cache-control", "no-cache");
//            try {
//                for (int i = 0; ; i++) {
//                    Transaction transaction = manager.create(i);
//                    EliseHttpRequest request = new EliseHttpRequest(uri, header);
//                    transaction.setRequest(request);
//                    transaction.future = new HttpTransFuture() {
//
//                        @Override
//                        public void redirect(Integer statusCode, byte[] httpContent, Map<String, String> headers,Long usedTimeStamp) {
//                            TRACER.writeError("123456");
//                        }
//
//                        @Override
//                        public void success(Integer statusCode, byte[] httpContent, Map<String, String> headers,Long usedTimeStamp) {
//                            TRACER.writeInfo(usedTimeStamp.toString());
//                        }
//
//                        @Override
//                        public void error(Integer statusCode, Object httpContent, Map<String, String> headers,Long usedTimeStamp) {
//                            TRACER.writeError("321654");
//                        }
//
//                        @Override
//                        public void failed(Throwable e,Long usedTimeStamp) {
//                            TRACER.writeError("", e);
//                        }
//
//                        @Override
//                        public void unreachable(Long usedTimeStamp) {
//
//                        }
//                    };
//                    executor.execTransaction(transaction);
//                    Thread.sleep(10);
//                }
//            } catch (Throwable t) {
//            }
        } finally {
            Thread.sleep(60000*1000);
            System.out.println(counter.get());
            EliseHttpClient.getInstance().close();
        }
    }
}
