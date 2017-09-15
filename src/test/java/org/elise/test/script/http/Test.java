package org.elise.test.script.http;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class Test {
    public static final AtomicLong counter = new AtomicLong();

	public static void main(String[] args)
	{
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(1024);
        cm.setDefaultMaxPerRoute(128);
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();

        final HttpGet request = new HttpGet("http://www.cnblogs.com/likaitai/p/5431246.html");
        for(int i=0;i<12000;i++) {
            httpclient.execute(request, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    counter.incrementAndGet();
                }

                public void failed(final Exception ex) {

                    System.out.println(request.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    System.out.println(request.getRequestLine() + " cancelled");
                }

            });
        }
        try {
            Thread.sleep(60*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter.get());
        try {
            httpclient.close();
        } catch (IOException ignore) {

        }
    }

}
