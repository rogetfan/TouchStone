package org.elise.test.framework.stack.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpReqSender {

    public static final Tracer tracer = Tracer.getInstance(HttpRespHandler.class);

    private URI uri;
    private HttpMethod method;
    private DefaultHttpHeaders headers;
    private HttpResultCallBack callBack;
    private byte[] httpBody;

    public HttpReqSender(URI uri, HttpMethod method, DefaultHttpHeaders headers, HttpResultCallBack callBack, byte[] httpBody) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.callBack = callBack;
        this.httpBody = httpBody;
    }

    public void send(Channel channel) {
        DefaultFullHttpRequest request;
        if (httpBody == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.toASCIIString());
        } else {
            ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(httpBody);
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri.toASCIIString(), body);
        }
        headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        request.headers().set(headers);
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
            try {
                if (channelFuture.isDone()) {
                    if (channelFuture.isSuccess()) {
                        callBack.setSequenceNum(HttpClient.getCounter(channelFuture.channel().id().asLongText()));
                        // Write request log
                        if (tracer.isInfoAvailable()) {
                            writeRequestLog(
                                    channelFuture.channel().id().asLongText(),
                                    request.method().toString(),
                                    uri.toASCIIString(),
                                    request.protocolVersion().toString(),
                                    request.headers()
                            );
                        }
                        HttpClient.putCallBack(channelFuture.channel().id().asLongText(), callBack);
                    } else if (channelFuture.isCancelled()) {
                        channelFuture.channel().closeFuture().sync();
                        tracer.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " has been canceled");
                        callBack.unreachable();
                    } else if (channelFuture.cause() != null) {
                        channelFuture.channel().closeFuture().sync();
                        tracer.writeError("Exception take place when send request to remote " + channelFuture.channel().remoteAddress());
                        callBack.failed(channelFuture.cause());
                    }

                } else {
                    tracer.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " failed");
                    callBack.unreachable();
                }
            } catch (Throwable t) {
                    tracer.writeError("Unknown Exception take place when send request");
                    callBack.failed(t);
            }
        });
    }

    private void writeRequestLog(String id, String method, String url, String protocolVersion, HttpHeaders headers) throws URISyntaxException, UnsupportedEncodingException {
        StringBuilder req = new StringBuilder();
        req.append("--------------------- ");
        req.append("Channel Id:");
        req.append(id);
        req.append(" Sequence:");
        req.append(callBack.getSequenceNum());
        req.append(" ---------------------");
        req.append(StringUtil.ENDLINE);
        req.append(method);
        req.append(StringUtil.SPACE);
        URI uri = new URI(url);
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
        tracer.writeInfo(req.toString());
    }
}