package org.elise.test.framework.stack.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.elise.test.framework.transaction.http.HttpResultCallBack;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by huxuehan on 2017/9/8.
 */
public final class  HttpClient {

    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap b;
    private HashMap<InetSocketAddress,ChannelFuture> hostMap = new HashMap<>();
    public HttpClient() {

        b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY,true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("HttpResponseDecoder",new HttpResponseDecoder());
                p.addLast("HttpRequestEncoder",new HttpRequestEncoder());
                p.addLast("Aggregator", new HttpObjectAggregator(1024*1024));
                p.addLast("HttpClient",new HttpClientInboundHandler());
            }
        });
    }

    public void close(){
        workerGroup.shutdownGracefully();
    }

    public void invoke(String host, Integer port, String url , HttpMethod method, DefaultHttpHeaders headers,HttpResultCallBack callBack) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {
        InetSocketAddress address = new InetSocketAddress(host,port);
        ChannelFuture future = hostMap.get(address);
        if( future == null || ! future.channel().isRegistered()) {
             future = b.connect(host, port);
            hostMap.put(address, future);

        }
        future.addListener(new HttpConnListener(url,method,headers,callBack));
    }
}
