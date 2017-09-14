package org.elise.test.script.http;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Test {

	public static void main(String[] args)
	{
	    final long begin = System.currentTimeMillis();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();

        final HttpGet request = new HttpGet("http://www.cnblogs.com/guogangj/p/5462594.html");

        httpclient.execute(request, new FutureCallback<HttpResponse>() {

            public void completed(final HttpResponse response) {
                try {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println(" response content is : " + content);
                    System.out.println(System.currentTimeMillis()-begin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void failed(final Exception ex) {

                System.out.println(request.getRequestLine() + "->" + ex);
            }

            public void cancelled() {
                System.out.println(request.getRequestLine() + " cancelled");
            }

        });
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            httpclient.close();
        } catch (IOException ignore) {

        }
    }

}
