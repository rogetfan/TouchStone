package org.elise.test.framework.transaction.future;

import org.elise.test.framework.transaction.Response;

/**
 * Created by Glenn on  2017/9/18 0018 21:55.
 */


public interface TransactionFuture {

    void success(Response response,Long usedTimeStamp);

    void error(Response response,Long usedTimeStamp);

    void failed(Throwable e,Long usedTimeStamp);

    void unreachable(Long usedTimeStamp);
}
