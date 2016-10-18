package org.elise.test.framework.transaction;

import org.apache.http.client.methods.*;
import org.elise.test.exception.InvalidRequestException;
import org.elise.test.exception.NullRequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huxuehan on 2016/10/18.
 */
public class HttpTransaction extends Transaction {

    private HashMap<String,String> headers = new HashMap<String,String>();
    private HashMap<String,String> parameters = new HashMap<String,String>();
    private HttpTransactionManager manager;
    private String transactionName;
    private String domainName;
    private Integer remotePort;
    private String remotePath;
    private String method;
    public HttpTransaction(String method,String domainName,Integer remotePort,String remotePath,String transactionName,HttpTransactionManager manager)
    {
           this.method = method;
           this.domainName = domainName;
           this.remotePort = remotePort;
           this.remotePath = remotePath;
           this.manager = manager;
           this.transactionName = transactionName;
    }

    public void setHeaders(String key,String value){
         headers.put(key,value);
    }

    public void setParameters(String key,String value){
        parameters.put(key,value);
    }

    private void doRequest() throws NullRequestException, InvalidRequestException {
        String uri = null;
        if(domainName == null) {
            throw new InvalidRequestException("domainName is null");
        }
        else {
            uri = uri+domainName;
        }
        //如果端口不存在，默认端口为80
        if(remotePort != null) {
            uri = uri+":"+remotePort;
        }
        //如果remotePath为空就在域名后面加上"/"
        if(remotePath != null){
            uri = uri + remotePath;
        }else{
            uri = uri+"/";
        }
        if(parameters.size()>0){
            uri=uri+"?";
            for(Map.Entry<String,String> entry:parameters.entrySet()){
                uri = uri + entry.getKey()+"="+entry.getValue()+"&";
            }
        }
        HttpRequestBase request = null;
        //根据method新建对应的http请求
        switch(method){
            case "GET":
                 request = new HttpGet(uri);
                 break;
            case "POST":
                 request = new HttpPost(uri);
                 break;
            case "PUT":
                 request = new HttpPut(uri);
                 break;
            case "DELETE":
                request = new HttpDelete(uri);
                 break;
            default:throw new NullRequestException("Unsupported method");
        }
        if(headers.size() != 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        manager.sendHttpRequest(request,transactionName);
    }
    @Override
    public void sendRequest() throws NullRequestException, InvalidRequestException {
        doRequest();
    }
}
