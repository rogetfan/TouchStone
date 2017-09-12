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

public class TestHttp {

    public static void main(String args[]) throws InterruptedException, UnsupportedEncodingException, URISyntaxException, LoadConfigException {
        ConfigLoader.getInstance().loadProperties("setting.properties", FrameworkConfig.getInstance(), TracerConfig.getInstance());
        HttpClient client = new HttpClient();
        for(int i = 0;i<100;i++) {
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.HOST, "www.baidu.com");
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        client.invokeGet("www.baidu.com",80, "https://www.baidu.com/?tn=47018152_dg",headers,  new HttpResultCallBack() {
            private long sequence;

            @Override
            public void success(String responseMessage, Object object) {
                System.out.println("321");
            }

            @Override
            public void redirect(String responseMessage, Integer statusCode, Object object) {

            }

            @Override
            public void error(String responseMessage, Integer statusCode, Object object) {

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void unreachable() {

            }

            @Override
            public long getSequence() {
                return sequence;
            }

            @Override
            public void setSequence(long sequence) {
               this.sequence = sequence;
            }
        });
        }
        for(int i = 0;i<10;i++) {
            DefaultHttpHeaders admin5Headers = new DefaultHttpHeaders();
            admin5Headers.set(HttpHeaderNames.HOST, "www.admin5.com");
            admin5Headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            client.invokeGet("www.admin5.com", 80, "http://www.admin5.com/article/20120323/417416.shtml", admin5Headers, new HttpResultCallBack() {
                private long sequence;

                @Override
                public void success(String responseMessage, Object object) {
                    System.out.println("123");
                }

                @Override
                public void redirect(String responseMessage, Integer statusCode, Object object) {

                }

                @Override
                public void error(String responseMessage, Integer statusCode, Object object) {

                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void unreachable() {

                }

                @Override
                public long getSequence() {
                    return sequence;
                }

                @Override
                public void setSequence(long sequence) {
                    this.sequence = sequence;
                }
            });
        }
//        String postBody = "mobileNo=18620523707&passWord=MTIzNDU2&clientType=ios&versionCode=100";
//        DefaultHttpHeaders postHeader = new DefaultHttpHeaders();
//        postHeader.set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
//        postHeader.set(HttpHeaderNames.HOST, "177.77.77.186");
//        postHeader.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        client.invokePost("177.77.77.186",
//                8082,
//                "http://177.77.77.186:8082/api/loginV1_2",
//                 postHeader,
//                 postBody.getBytes("UTF-8"),
//                 new HttpResultCallBack() {
//                        @Override
//                        public void success(String responseMessage, Object object) {
//                            System.out.println("999");
//                        }
//
//                        @Override
//                        public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                        }
//
//                        @Override
//                        public void error(String responseMessage, Integer statusCode, Object object) {
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
//        });
//        client.invokePost("177.77.77.186",
//                8082,
//                "http://177.77.77.186:8082/api/loginV1_2",
//                postHeader,
//                postBody.getBytes("UTF-8"),
//                new HttpResultCallBack() {
//                    @Override
//                    public void success(String responseMessage, Object object) {
//                        System.out.println("888");
//                    }
//
//                    @Override
//                    public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void error(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void unreachable() {
//
//                    }
//                });
//
//        client.invokePost("177.77.77.192",
//                8082,
//                "http://177.77.77.192:8082/api/loginV1_2",
//                postHeader,
//                postBody.getBytes("UTF-8"),
//                new HttpResultCallBack() {
//                    @Override
//                    public void success(String responseMessage, Object object) {
//                        System.out.println("888");
//                    }
//
//                    @Override
//                    public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void error(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void unreachable() {
//
//                    }
//                });
//
//        client.invokePost("177.77.77.192",
//                8082,
//                "http://177.77.77.192:8082/api/loginV1_2",
//                postHeader,
//                postBody.getBytes("UTF-8"),
//                new HttpResultCallBack() {
//                    @Override
//                    public void success(String responseMessage, Object object) {
//                        System.out.println("888");
//                    }
//
//                    @Override
//                    public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void error(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void unreachable() {
//
//                    }
//                });
//        client.invokePost("177.77.77.192",
//                8082,
//                "http://177.77.77.186:192/api/loginV1_2",
//                postHeader,
//                postBody.getBytes("UTF-8"),
//                new HttpResultCallBack() {
//                    @Override
//                    public void success(String responseMessage, Object object) {
//                        System.out.println("888");
//                    }
//
//                    @Override
//                    public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void error(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void unreachable() {
//
//                    }
//                });
//
//        client.invokePost("177.77.77.186",
//                8082,
//                "http://177.77.77.186:8082/api/loginV1_2",
//                postHeader,
//                postBody.getBytes("UTF-8"),
//                new HttpResultCallBack() {
//                    @Override
//                    public void success(String responseMessage, Object object) {
//                        System.out.println("888");
//                    }
//
//                    @Override
//                    public void redirect(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void error(String responseMessage, Integer statusCode, Object object) {
//
//                    }
//
//                    @Override
//                    public void failed(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void unreachable() {
//
//                    }
//                });

        Thread.sleep(50000);
        client.close();

    }
}
