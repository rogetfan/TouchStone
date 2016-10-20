package org.elise.test.performance.http;

import org.elise.test.exception.InvalidRequestException;
import org.elise.test.exception.NullRequestException;
import org.elise.test.framework.transaction.TransactionManager;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.framework.transaction.http.HttpTransaction;
import org.elise.test.framework.transaction.http.HttpTransactionManager;
import org.elise.test.framework.user.VirtualUser;
import org.elise.test.tracer.TracerConfig;

import java.util.Properties;

/**
 * Created by huxuehan on 2016/10/19.
 */
public class HttpUser extends VirtualUser<HttpUserInfo> {
    private static final String action1="visit_baidu";
    private static final String action2="visti_zhidao";
    public HttpUser() {
        super();
    }

    @Override
    public void action() {

        HttpResultCallBack callback = new HttpResultCallBack() {

            @Override
            public void success(String responseMessage) {
                //System.out.println(responseMessage);
                //System.out.println("Response length is " + responseMessage.length());
            }

            @Override
            public void error(String responseMessage, Integer statusCode) {
                System.out.println("Response length is " + responseMessage.length());
                System.out.println("Response status code is " + statusCode);
            }

            @Override
            public void failed(Exception e) {
                System.err.println(e);
            }

            @Override
            public void unreachable() {

            }
        };
        HttpTransactionManager manager = (HttpTransactionManager)this.getTransactionManager();
        HttpTransaction trans1 = manager.createHttpTransaction("POST", "http://192.168.50.225", 8009, "/cams/login.htm", action1);
        //HttpTransaction trans1 = manager.createHttpTransaction("GET", "http://zhidao.baidu.com", 80, "/link", action1);
        //trans1.setParameters("url", "z8PzLn5W7WrghaIw7M21wGIMRYl8R0Ufn_zaj8gQhC8JO08mZHLe_cjIyfY5UJ_cZKQSp-7tEJpCbDDlDD8CTnlb6Me8Vq8X36-xohnuALq");
        trans1.setHttpContent("userName=passport_0&userPwd=passport_0&remember=1".getBytes());
        trans1.setTransactionCallBack(callback);
        trans1.setIntervalTimeStamp(200L);
        //HttpTransaction trans2 = manager.createHttpTransaction("GET", "http://zhidao.baidu.com", 80, "/link", action2);
        //trans2.setParameters("url", "z8PzLn5W7WrghaIw7M21wGIMRYl8R0Ufn_zaj8gQhC8JO08mZHLe_cjIyfY5UJ_cZKQSp-7tEJpCbDDlDD8CTnlb6Me8Vq8X36-xohnuALq");
        HttpTransaction trans2 = manager.createHttpTransaction("POST", "http://192.168.50.225", 8009, "/cams/login.htm", action2);
        trans2.setTransactionCallBack(callback);
        trans2.setIntervalTimeStamp(200L);
        trans2.setHttpContent("userName=passport_0&userPwd=passport_0&remember=1".getBytes());
        trans1.setNextTransaction(null);
        trans2.setNextTransaction(trans1);
        try {
            trans2.sendRequest();
        } catch (NullRequestException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
