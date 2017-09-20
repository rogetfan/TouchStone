package org.elise.test.framework.transaction.http;

import org.elise.test.framework.stack.http.HttpClient;
import org.elise.test.framework.transaction.Request;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.util.GlobalId;

/**
 * Created by Glenn on  2017/9/20 0020 11:56.
 */


public class HttpTransaction extends Transaction {

    private String globalId;
    private EliseHttpResponse response;
    private EliseHttpRequest request;
    private Integer connToken;
    private long sequence;
    private HttpClient client;

    protected HttpTransaction(HttpClient client, Integer connToken){
        this.client = client;
        this.globalId = GlobalId.generate16();
        this.connToken = connToken;
    }


    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public long getSequenceNum(){
        return sequence;
    }

    @Override
    public void setSequenceNum(long sequence){
        this.sequence = sequence;
    }



}
