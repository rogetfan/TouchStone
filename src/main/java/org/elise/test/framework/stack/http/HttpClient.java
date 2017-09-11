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
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Glenn on 2017/9/8.
 */
public final class HttpClient {

    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap b;
    private Map<SocketAddress, ChannelFuture> hostMap = new ConcurrentHashMap<>();
    private static Map<SocketAddress, ConcurrentLinkedQueue<HttpResultCallBack>> callBackQueue = new ConcurrentHashMap<>();

    public HttpClient() {

        b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("HttpResponseDecoder", new HttpResponseDecoder());
                p.addLast("HttpRequestEncoder", new HttpRequestEncoder());
                p.addLast("Aggregator", new HttpObjectAggregator(1024 * 1024));
                p.addLast("HttpClient", new HttpClientInboundHandler());
            }
        });
    }

    public void close() {
        workerGroup.shutdownGracefully();
    }

    public static HttpResultCallBack getCallBack(SocketAddress address) throws Exception {
        ConcurrentLinkedQueue<HttpResultCallBack> queue = callBackQueue.get(address);
        synchronized (queue) {
            if (queue == null || queue.isEmpty()) {
                throw new Exception("Transaction CallBack missed");
            } else {
                return callBackQueue.get(address).poll();
            }
        }

    }

    public static boolean putCallBack(SocketAddress address, HttpResultCallBack callBack) {

        synchronized (callBackQueue) {
            if (!callBackQueue.containsKey(address)) {
                ConcurrentLinkedQueue<HttpResultCallBack> queue = new ConcurrentLinkedQueue<>();
                queue.add(callBack);
                callBackQueue.put(address, queue);
                return true;
            } else {
                ConcurrentLinkedQueue<HttpResultCallBack> queue = callBackQueue.get(address);
                if (queue == null) {
                    return false;
                } else {
                    queue.add(callBack);
                    return true;
                }
            }
        }

    }

    public void invoke(String host, Integer port, String url, HttpMethod method, DefaultHttpHeaders headers, HttpResultCallBack callBack) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {
        SocketAddress address = new InetSocketAddress(host, port);
        ChannelFuture future = hostMap.get(address);
        if (future == null || !future.channel().isRegistered()) {
            future = b.connect(host, port);
            hostMap.put(address, future);
            callBackQueue.put(address, new ConcurrentLinkedQueue<>());
        }
        future.addListener(new HttpConnListener(url, method, headers, callBack));
    }
}
