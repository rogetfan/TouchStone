package org.elise.test.framework.stack.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by huxuehan on 2017/9/8.
 */
public final class  HttpClient {

    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap b;
    public HttpClient() {

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY,true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                ch.pipeline().addLast(new HttpResponseDecoder());
                // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                ch.pipeline().addLast(new HttpRequestEncoder());
                ch.pipeline().addLast(new HttpClientInboundHandler());
            }
        });
    }
    public void invoke(String host,Integer port,HttpMethod method) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {
        // Start the client.
        ChannelFuture f = b.connect(host, port);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.channel().writeAndFlush()
                URI uri = new URI("http://127.0.0.1:8844");
                String msg = "Are you ok?";
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method,
                        uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

                // 构建http请求
                request.headers().set(HttpHeaderNames.HOST, host);
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                // 发送http请求
                channelFuture.channel().write(request);
                channelFuture.channel().flush();
                channelFuture.channel().closeFuture().sync();
                channelFuture.channel().
            }
        });


    }


}
