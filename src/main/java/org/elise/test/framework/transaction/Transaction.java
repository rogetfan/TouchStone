package org.elise.test.framework.transaction;

import org.elise.test.framework.stack.VirtualClient;
import org.elise.test.framework.transaction.future.TransactionFuture;

/**
 * Created by Glenn on 2016/10/18.
 */
public abstract class Transaction {

    public TransactionFuture future;
    protected Response response;
    protected Request request;
    protected Integer connToken;
    protected long sequence;
    protected VirtualClient client;
    protected long timeStampBegin;



    public  long getSequenceNum(){
        return sequence;
    }
    public  void setSequenceNum(long sequence){
        this.sequence = sequence;
    }
    public  void setResponse(Response response){
        this.response = response;
    }
    public  void setRequest(Request request){
        this.request = request;
    }
    public  long getTransBeginTime(){
        return timeStampBegin;
    }

    public abstract void sendRequest() throws Throwable;
    public abstract Long getSleepTimeStamp() throws Throwable;
    public abstract String getStrResponse() throws Throwable;
    public abstract String getStrRequest() throws Throwable;
}
