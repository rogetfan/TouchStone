package org.elise.test.framework.transaction.http;

import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.elise.test.exception.InvalidRequestException;
import org.elise.test.exception.NullRequestException;
import org.elise.test.framework.transaction.Transaction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huxuehan on 2016/10/18.
 */
public class HttpTransaction extends Transaction {

    private HashMap<String, String> headers = new HashMap<String, String>();
    private HashMap<String, String> parameters = new HashMap<String, String>();
    private HttpTransactionManager manager;
    private HttpResultCallBack callback;
    private String transactionName;
    private String domainName;
    private Integer remotePort;
    private String remotePath;
    private String method;
    private long intervalTimeStamp;
    private byte[] httpContent;
    private HttpTransaction nextTransaction;

    public HttpTransaction(String method, String domainName, Integer remotePort, String remotePath, String transactionName, HttpTransactionManager manager) {
        this.method = method;
        this.domainName = domainName;
        this.remotePort = remotePort;
        this.remotePath = remotePath;
        this.manager = manager;
        this.transactionName = transactionName;
        this.intervalTimeStamp = 10000;
        this.callback = null;
    }

    public void setTransactionCallBack(HttpResultCallBack callback) {
        this.callback = callback;
    }

    public void setHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void setParameters(String key, String value) {
        parameters.put(key, value);
    }

    public void setIntervalTimeStamp(long intervalTimeStamp) {
        this.intervalTimeStamp = intervalTimeStamp;
    }

    public void setHttpContent(byte[] httpContent) {
        this.httpContent = httpContent;
    }

    @Override
    public void setNextTransaction(Transaction nextTransaction) {
        this.nextTransaction = (HttpTransaction) nextTransaction;
    }

    @Override
    public void sendRequest() throws NullRequestException, InvalidRequestException {
        manager.sendTransaction(this);
    }

    @Override
    public Transaction getNextTransaction() {
        return nextTransaction;
    }

    public long getIntervalTimeStamp() {
        return intervalTimeStamp;
    }

    protected HttpResultCallBack getCallback() {
        return callback;
    }

    protected String getTransactionName() {
        return transactionName;
    }

    protected HttpEntityEnclosingRequestBase getRequest() throws NullRequestException, InvalidRequestException {
        String uri = "";
        if (domainName == null) {
            throw new InvalidRequestException("domainName is null");
        } else {
            uri = uri + domainName;
        }
        //if port number not exist,use 80 as default
        if (remotePort != null) {
            uri = uri + ":" + remotePort;
        }
        // if remotePath is null,add "/" in the end of it
        if (remotePath != null) {
            uri = uri + remotePath;
        } else {
            uri = uri + "/";
        }
        if (parameters.size() > 0) {
            uri = uri + "?";
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                uri = uri + entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        HttpEntityEnclosingRequestBase request = null;
        //build Http request according to it's method
        switch (method) {
            case "GET":
                request = new org.elise.test.framework.transaction.http.methods.HttpGet(uri);
                break;
            case "POST":
                request = new HttpPost(uri);
                break;
            case "PUT":
                request = new HttpPut(uri);
                break;
            case "DELETE":
                request = new org.elise.test.framework.transaction.http.methods.HttpDelete(uri);
                break;
            default:
                throw new NullRequestException("Unsupported method");
        }
        if (httpContent != null) {
            BasicHttpEntity entity = new BasicHttpEntity();
            InputStream input = new ByteArrayInputStream(httpContent);
            entity.setContent(input);
            request.setEntity(entity);
        }
        if (headers.size() != 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return request;
    }
}
