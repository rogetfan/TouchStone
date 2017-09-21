package org.elise.test.framework.transaction.future;

import org.elise.test.framework.transaction.Response;

/**
 * Created by Glenn on  2017/9/18 0018 21:55.
 */


public interface TransactionFuture {

    void success(Response response);

    void error(Response response);

    void failed(Throwable e);

    void unreachable();
}
