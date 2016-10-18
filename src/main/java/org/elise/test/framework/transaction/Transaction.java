package org.elise.test.framework.transaction;

import org.elise.test.exception.InvalidRequestException;
import org.elise.test.exception.NullRequestException;

/**
 * Created by huxuehan on 2016/10/18.
 */
public abstract class Transaction {
    Transaction nextTransaction;
    public void setNextTransaction(Transaction nextTransaction){
        this.nextTransaction = nextTransaction;
    }
    public abstract void sendRequest() throws NullRequestException, InvalidRequestException;
}
