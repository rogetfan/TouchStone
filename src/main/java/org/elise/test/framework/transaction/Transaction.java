package org.elise.test.framework.transaction;


import org.elise.test.framework.usergroup.VirtualUser;

/**
 * Created by Glenn on 2016/10/18.
 */
public abstract class Transaction implements Runnable{

    public Transaction(){



    }

    public abstract VirtualUser getUser() throws Throwable;

    public abstract void sendRequest() throws Throwable;

    public abstract long getIntervalTimeStamp() throws Throwable;

    public abstract Transaction getNextTransaction() throws Throwable;

    public abstract void setNextTransaction(Transaction nextTransaction) throws Throwable;
}
