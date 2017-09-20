//package org.elise.test.script.http;
//
//import org.elise.test.framework.transaction.http.HttpResultCallBack;
//import org.elise.test.framework.transaction.http.EliseHttpRequest;
//import org.elise.test.framework.transaction.http.HttpTransactionManager;
//import org.elise.test.framework.virtualuser.VirtualUser;
//import org.elise.test.tracer.Tracer;
//
//import java.util.UUID;
//
///**
// * Created by Glenn on 2016/10/25.
// */
//public class HttpUser extends VirtualUser<HttpUserInfo> {
//    private static final String ACTION_1="visit_baidu";
//    private static final String ACTION_2="visti_zhidao";
//    private static Tracer tracer = Tracer.getInstance(HttpUser.class);
//    public HttpUser() {
//        super();
//    }
//
//    @Override
//    public void action() throws Throwable {
//
//        HttpResultCallBack callback = new HttpResultCallBack() {
//
//            @Override
//            public void success(String responseMessage) {
//                //tracer.writeInfo(responseMessage);
//                tracer.writeInfo("Response length is " + responseMessage.length());
//            }
//
//            @Override
//            public void error(String responseMessage, Integer statusCode) {
//                tracer.writeInfo("Response length is " + responseMessage.length());
//                tracer.writeInfo("Response status code is " + statusCode);
//            }
//
//            @Override
//            public void failed(Exception e) {
//                System.err.println(e);
//            }
//
//            @Override
//            public void unreachable() {
//
//            }
//        };
//        HttpTransactionManager manager = (HttpTransactionManager)this.getTransactionManager();
//        EliseHttpRequest trans1 = manager.createHttpTransaction("POST", "http://192.168.50.225", 8009, "/cams/login.htm", ACTION_1);
//        //EliseHttpRequest trans1 = manager.createHttpTransaction("GET", "http://zhidao.baidu.com", 80, "/link", action1);
//        //trans1.setParameters("url", "z8PzLn5W7WrghaIw7M21wGIMRYl8R0Ufn_zaj8gQhC8JO08mZHLe_cjIyfY5UJ_cZKQSp-7tEJpCbDDlDD8CTnlb6Me8Vq8X36-xohnuALq");
//        trans1.setHttpContent("userName=passport_0&userPwd=passport_0&remember=1".getBytes());
//        trans1.setTransactionCallBack(callback);
//        trans1.setIntervalTimeStamp(200L);
//        //EliseHttpRequest trans2 = manager.createHttpTransaction("GET", "http://zhidao.baidu.com", 80, "/link", action2);
//        //trans2.setParameters("url", "z8PzLn5W7WrghaIw7M21wGIMRYl8R0Ufn_zaj8gQhC8JO08mZHLe_cjIyfY5UJ_cZKQSp-7tEJpCbDDlDD8CTnlb6Me8Vq8X36-xohnuALq");
//        EliseHttpRequest trans2 = manager.createHttpTransaction("POST", "http://192.168.50.225", 8009, "/cams/login.htm", ACTION_2);
//        trans2.setTransactionCallBack(callback);
//        trans2.setIntervalTimeStamp(200L);
//        trans2.setHttpContent("userName=passport_0&userPwd=passport_0&remember=1".getBytes());
//        trans1.setNextTransaction(null);
//        trans2.setNextTransaction(trans1);
//        trans2.sendRequest();
//    }
//
//    @Override
//    public String getUserStamp() {
//        return UUID.randomUUID().toString();
//    }
//}