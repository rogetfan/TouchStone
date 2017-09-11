package org.elise.test.framework.stack.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpConnListener implements ChannelFutureListener {

    public static Tracer tracer = Tracer.getInstance(HttpClientInboundHandler.class);

    private String url;
    private HttpMethod method;
    private DefaultHttpHeaders headers;
    private HttpResultCallBack callBack;
    private byte[] httpBody;
    public HttpConnListener(String url, HttpMethod method, DefaultHttpHeaders headers, HttpResultCallBack callBack, byte[] httpBody) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.callBack = callBack;
        this.httpBody = httpBody;
    }

    @Override
    public void operationComplete(ChannelFuture future) {
        try {
            if (future.isDone()) {
                if (future.isSuccess()) {
                    URI uri = new URI(url);
                    DefaultFullHttpRequest request;
                    if (httpBody == null) {
                        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.toASCIIString());
                    } else {
                        ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(httpBody);
                        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.toASCIIString(), body);
                    }
                    headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                    request.headers().set(headers);
                    future.channel().writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                        if (channelFuture.isDone()) {
                            if (channelFuture.isSuccess()) {
                                //Write request log
                                if (tracer.isInfoAvailable()) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("------------- ");
                                    sb.append("Channel Id:");
                                    sb.append(channelFuture.channel().id());
                                    sb.append(" Sequence:");
                                    sb.append(" -------------");
                                    sb.append(StringUtil.ENDLINE);
                                    sb.append(request.toString());
                                    tracer.writeInfo(sb.toString());

                                }
                                HttpClient.putCallBack(channelFuture.channel().remoteAddress(), callBack);

                            } else if (channelFuture.isCancelled()) {

                            } else if (channelFuture.cause() != null) {

                            }

                        } else {

                        }
                    });
                } else if (future.isCancelled()) {
                    future.channel().closeFuture().sync();
                    tracer.writeError("Connect to remote " + future.channel().remoteAddress().toString() + " has been canceled");
                    callBack.unreachable();
                } else if (future.cause() != null) {
                    future.channel().closeFuture().sync();
                    tracer.writeError("Exception take place when connect to remote " + future.channel().remoteAddress().toString());
                    callBack.failed(future.cause());
                }
            } else {
                tracer.writeError("Connect to remote " + future.channel().remoteAddress().toString() + " failed");
                callBack.unreachable();
            }
        } catch (URISyntaxException e) {
            tracer.writeError("Url " + url + " is illegal");
            callBack.failed(e);
        } catch (InterruptedException e) {
            tracer.writeError("Interruption happened when close the future ");
            callBack.failed(e);
        }
    }
}