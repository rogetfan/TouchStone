package org.elise.test.framework.transaction;

/**
 * Created by Glenn on 2016/10/18.
 */
public abstract class Transaction {

    public TransactionCallback callback;

    public abstract long getSequenceNum();

    public abstract  void setSequenceNum(long sequence);

    public abstract void sendRequest(Request request);

}
