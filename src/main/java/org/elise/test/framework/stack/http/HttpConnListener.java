package org.elise.test.framework.stack.http;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;

import java.net.URI;

public class HttpConnListener implements ChannelFutureListener {

    private String url;
    private HttpMethod method;
    private DefaultHttpHeaders headers;


    public HttpConnListener(String url, HttpMethod method, DefaultHttpHeaders headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isDone()) {
            if (future.isSuccess()) {
                URI uri = new URI(url);
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method,
                        uri.toASCIIString());
                headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                request.headers().set(headers);
                // 构建http请求
                future.channel().writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                    HttpClientInboundHandler handler = (HttpClientInboundHandler) channelFuture.channel().pipeline().get("HttpClient");
                    handler.setCallBack(new HttpResultCallBack() {
                        @Override
                        public void success(String responseMessage, Object object) {
                            System.out.println("321");
                            System.out.println(responseMessage);
                        }

                        @Override
                        public void error(String responseMessage, Integer statusCode, Object object) {

                        }

                        @Override
                        public void failed(Exception e) {

                        }

                        @Override
                        public void unreachable() {

                        }
                    });
                });
            } else if (future.isCancelled()) {
                future.channel().closeFuture().sync();

            } else if (future.cause() != null) {
                future.channel().closeFuture().sync();
            }
        } else {

        }

    }
}
