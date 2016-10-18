package org.elise.test.framework.transaction;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.elise.test.lr.LrTransManager;
import org.elise.test.tracer.Tracer;

import java.io.IOException;

/**
 * Created by huxuehan on 2016/10/18.
 */
public class HttpTransactionManager {
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
    private static CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
    private static Tracer tracer = Tracer.getInstance(HttpTransactionManager.class);

    public HttpTransactionManager() {

    }

    /**
     * DomainName结尾不带"/"
     */
    public HttpTransaction createHttpTransaction(String method, String domainName, Integer remotePort, String remotePath,String transactionName) {
        return new HttpTransaction(method, domainName, remotePort, remotePath,transactionName,this);
    }

    protected void sendHttpRequest(HttpRequestBase request,final String transactionName) {
        try {
            httpclient.start();
            final long begin = System.currentTimeMillis();
            httpclient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    if(tracer.isInfoAvailable()){
                        tracer.writeInfo(httpResponse.toString());
                    }
                    if(httpResponse.getStatusLine().getStatusCode() == 200 || httpResponse.getStatusLine().getStatusCode() == 204){
                        LrTransManager.addStatus(transactionName,((double)System.currentTimeMillis()-begin)/1000.0,true);
                    }else{
                        LrTransManager.addStatus(transactionName,((double)System.currentTimeMillis()-begin)/1000.0,false);
                    }
                }

                @Override
                public void failed(Exception e) {
                    tracer.writeError("Send http request failed",e);
                    LrTransManager.addStatus(transactionName,((double)System.currentTimeMillis()-begin)/1000.0,false);
                }

                @Override
                public void cancelled() {

                }
            });
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                tracer.writeError("close socket error", e);
            }
        }
    }
}
