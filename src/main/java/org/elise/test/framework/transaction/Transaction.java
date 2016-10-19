package org.elise.test.framework.transaction;

import org.elise.test.exception.InvalidRequestException;
import org.elise.test.exception.NullRequestException;

/**
 * Created by huxuehan on 2016/10/18.
 */
public abstract class Transaction {
    public abstract void setNextTransaction(Transaction nextTransaction);
    public abstract void sendRequest() throws NullRequestException, InvalidRequestException;
    public abstract long getIntervalTimeStamp();
    public abstract Transaction getNextTransaction();
}
