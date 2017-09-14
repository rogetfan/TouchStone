package org.elise.test.framework.stack.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Glenn on  2017/9/14 0014 10:56.
 */


public class HttpConnection {

    public static final Tracer TRACER = Tracer.getInstance(HttpConnection.class);

    private  final ConcurrentLinkedQueue<HttpResultCallBack> callBackQueue = new ConcurrentLinkedQueue<>();
    private  final AtomicLong counter = new AtomicLong();
    private  Channel channel;
    private  final Object connLock = new Object();
    private  HttpClient client;
    private  Integer index;
    private  SocketAddress address;

    public HttpConnection(HttpClient client,Integer index,SocketAddress address){
        this.index = index;
        this.client = client;
        this.address = address;

    }

    public Long getCounter(){
        return counter.incrementAndGet();
    }

    private void reset(){
        counter.getAndSet(0);
        callBackQueue.clear();
    }

    public HttpResultCallBack getCallBack(){
        return callBackQueue.poll();
    }

    public void setCallBack(HttpResultCallBack callBack){
        callBackQueue.add(callBack);
    }

    public void invokePost(URI uri, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) throws  Throwable {
        Channel channel = connect();
        HttpReqSender sender = new HttpReqSender(uri, HttpMethod.POST, headers, callBack, httpBody);
        sender.send(channel);
    }

    public void invokeGet(URI uri, DefaultHttpHeaders headers, HttpResultCallBack callBack) throws Throwable {
        Channel channel = connect();
        HttpReqSender sender =new HttpReqSender(uri, HttpMethod.GET, headers, callBack, null);
        sender.send(channel);
    }

    public void invokeDelete(URI uri, DefaultHttpHeaders headers, HttpResultCallBack callBack) throws Throwable {
        Channel channel = connect();
        HttpReqSender sender = new HttpReqSender(uri, HttpMethod.DELETE, headers, callBack, null);
        sender.send(channel);
    }

    public void invokePut(URI uri, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) throws Throwable {
        Channel channel = connect();
        HttpReqSender sender = new HttpReqSender(uri, HttpMethod.PUT, headers, callBack, httpBody);
        sender.send(channel);
    }


    public void invoke(URI uri, HttpMethod method, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) throws Throwable {
        Channel channel = connect();
        HttpReqSender sender =new HttpReqSender(uri, method, headers, callBack, httpBody);
        sender.send(channel);
    }



    public String getKey(){
        return channel.id().asLongText();
    }

    private Channel connect() throws IOException, InterruptedException {
        synchronized (connLock) {
            if (channel == null) {
                ChannelFuture future = client.getBootstrap().connect(address).sync();
                channel = future.channel();
                client.register(getKey(),this);
                return channel;
            } else if (!channel.isRegistered()) {
                if (callBackQueue != null && !callBackQueue.isEmpty()) {
                    for (HttpResultCallBack callBack : callBackQueue) {
                        callBack.failed(new Exception("Channel has been destroy"));
                    }
                }
                client.unregister(getKey());
                ChannelFuture future = client.getBootstrap().connect(address).sync();
                channel = future.channel();
                reset();
                client.register(getKey(),this);
                return channel;
            } else {
                return channel;
            }
        }
    }
}
