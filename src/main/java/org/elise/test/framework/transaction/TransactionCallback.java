package org.elise.test.framework.transaction;

/**
 * Created by Glenn on  2017/9/18 0018 21:55.
 */


public interface TransactionCallback {

    void success(Response response);

    void error(Response response,String errorMsg);

    void failed(Throwable e);

    void unreachable();
}
