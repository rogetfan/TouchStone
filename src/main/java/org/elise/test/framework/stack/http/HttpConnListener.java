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
import java.util.Map;

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
                    DefaultFullHttpRequest request;
                    if (httpBody == null) {
                        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, new URI(url).toASCIIString());
                    } else {
                        ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(httpBody);
                        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, new URI(url).toASCIIString(), body);
                    }
                    headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                    request.headers().set(headers);
                    future.channel().writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                        if (channelFuture.isDone()) {
                            if (channelFuture.isSuccess()) {
                                callBack.setSequence(HttpClient.getCounter(channelFuture.channel().remoteAddress()));
                                // Write request log
                                if (tracer.isInfoAvailable()) {
                                    StringBuilder req = new StringBuilder();
                                    req.append("--------------------- ");
                                    req.append("Channel Id:");
                                    req.append(channelFuture.channel().id());
                                    req.append(" Sequence:");
                                    req.append(callBack.getSequence());
                                    req.append(" ---------------------");
                                    req.append(StringUtil.ENDLINE);
                                    req.append(request.method());
                                    req.append(StringUtil.SPACE);
                                    URI uri = new URI(request.uri());
                                    req.append(uri.getPath());
                                    req.append("?");
                                    req.append(uri.getQuery());
                                    req.append(StringUtil.SPACE);
                                    req.append(request.protocolVersion());
                                    req.append(StringUtil.ENDLINE);
                                    for (Map.Entry<String, String> entry : request.headers().entries()) {
                                        req.append(entry.getKey()).append(":").append(entry.getValue());
                                        req.append(StringUtil.ENDLINE);
                                    }
                                    req.append(StringUtil.ENDLINE);
                                    req.append(StringUtil.ENDLINE);
                                    if(request.content().readableBytes() > 8*1024){
                                        req.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
                                    }else{
                                        req.append(request.content().toString(io.netty.util.CharsetUtil.UTF_8));
                                    }
                                    tracer.writeInfo(req.toString());
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