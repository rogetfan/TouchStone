package org.elise.test.framework.stack.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.stack.Connection;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Glenn on  2017/9/14 0014 10:56.
 */


public class HttpConnection implements Connection {

    public static final Tracer TRACER = Tracer.getInstance(HttpConnection.class);

    private  final ConcurrentLinkedQueue<Transaction> callBackQueue = new ConcurrentLinkedQueue<>();
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

    @Override
    public void invoke(Transaction transaction) throws IOException, InterruptedException {
        Channel channel = connect();
       //
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(uri.getPath());
        uriBuilder.append("?");
        uriBuilder.append(uri.getQuery() == null ? "" : uri.getQuery());
        DefaultFullHttpRequest request;
        if (httpBody == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uriBuilder.toString());
        } else {
            ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(httpBody);
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uriBuilder.toString(), body);
        }
        headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        headers.set(HttpHeaderNames.HOST, uri.getHost());
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        headers.set(HttpHeaderNames.ACCEPT_ENCODING,HttpHeaderValues.GZIP_DEFLATE);
        headers.set(HttpHeaderNames.USER_AGENT,"IAmFuckingYourMind/6.3.2");
        request.headers().set(headers);
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
            try {
                if (channelFuture.isDone()) {
                    if (channelFuture.isSuccess()) {
                        transaction.getCallBack().setSequenceNum(HttpClient.getCounter(channelFuture.channel().id().asShortText()));
                        // Write request log
                        if (TRACER.isInfoAvailable()) {
                            writeRequestLog(
                                    channelFuture.channel().id().asShortText(),
                                    request.method().toString(),
                                    uri.toASCIIString(),
                                    request.protocolVersion().toString(),
                                    request.headers()
                            );
                        }
                        HttpClient.putCallBack(channelFuture.channel().id().asShortText(), callBack);
                    } else if (channelFuture.isCancelled()) {
                        channelFuture.channel().close().sync();
                        TRACER.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " has been canceled");
                        callBack.unreachable();
                    } else if (channelFuture.cause() != null) {
                        TRACER.writeError("Exception take place when send request to remote " + channelFuture.channel().remoteAddress());
                        channelFuture.channel().close().sync();
                        callBack.failed(channelFuture.cause());
                    }

                } else {
                    TRACER.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " failed");
                    callBack.unreachable();
                }
            } catch (Throwable t) {
                TRACER.writeError("Unknown Exception take place when send request");
                callBack.failed(t);
            }
        });
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
                TRACER.writeInfo("Channel is null and connect to "+address.toString()+" successfully");
                return channel;
            } else if (!channel.isRegistered()) {
                if (callBackQueue != null && !callBackQueue.isEmpty()) {
                    for (Transaction transaction : callBackQueue) {
                        transaction.getCallBack().failed(new Exception("Channel has been destroy"));
                    }
                }
                client.unregister(getKey());
                ChannelFuture future = client.getBootstrap().connect(address).sync();
                channel = future.channel();
                reset();
                TRACER.writeInfo("Channel is abandon and then connect to "+address.toString()+" successfully");
                client.register(getKey(),this);
                return channel;
            } else {
                TRACER.writeInfo("Channel is useful and active");
                return channel;
            }
        }
    }

    private void writeRequestLog(String method, URI uri, String protocolVersion, HttpHeaders headers,byte[] httpBody,long sequenceId) throws UnsupportedEncodingException {
        StringBuilder req = new StringBuilder();
        req.append("--------------------- ");
        req.append("Channel Id:");
        req.append(channel.id().asShortText());
        req.append(" Sequence:");
        req.append(sequenceId);
        req.append(" ---------------------");
        req.append(StringUtil.ENDLINE);
        req.append(method);
        req.append(StringUtil.SPACE);
        req.append(uri.getPath());
        if (uri.getQuery() != null) {
            req.append("?");
            req.append(uri.getQuery());
        }
        req.append(StringUtil.SPACE);
        req.append(protocolVersion);
        req.append(StringUtil.ENDLINE);
        for (Map.Entry<String, String> entry : headers.entries()) {
            req.append(entry.getKey()).append(":").append(entry.getValue());
            req.append(StringUtil.ENDLINE);
        }
        req.append(StringUtil.ENDLINE);
        req.append(StringUtil.ENDLINE);
        if (httpBody == null) {
            req.append("");
        } else if (httpBody.length > 8 * 1024) {
            req.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            req.append(new String(httpBody, "UTF-8"));
        }
        TRACER.writeInfo(req.toString());
    }
}
