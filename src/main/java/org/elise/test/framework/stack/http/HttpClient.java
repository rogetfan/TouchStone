package org.elise.test.framework.stack.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
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
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("HttpResponseDecoder", new HttpResponseDecoder());
                p.addLast("HttpRequestEncoder", new HttpRequestEncoder());
                p.addLast("Aggregator", new HttpObjectAggregator(1024 * 1024 * 8));
                p.addLast("HttpClient", new HttpClientInboundHandler());
            }
        });
    }

    public void close() {
        workerGroup.shutdownGracefully();
    }

    public static HttpResultCallBack getCallBack(SocketAddress address) throws Exception {
        synchronized (callBackQueue) {
            ConcurrentLinkedQueue<HttpResultCallBack> queue = callBackQueue.get(address);
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

    public void invokePost(String host, Integer port, String url, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) {
        connect(host, port).addListener(new HttpConnListener(url, HttpMethod.POST, headers, callBack, httpBody));
    }

    public void invokeGet(String host, Integer port, String url, DefaultHttpHeaders headers, HttpResultCallBack callBack) {
        connect(host, port).addListener(new HttpConnListener(url, HttpMethod.GET, headers, callBack, null));
    }

    public void invokeDelete(String host, Integer port, String url, DefaultHttpHeaders headers, HttpResultCallBack callBack) {
        connect(host, port).addListener(new HttpConnListener(url, HttpMethod.DELETE, headers, callBack, null));
    }

    public void invokePut(String host, Integer port, String url, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) {
        connect(host, port).addListener(new HttpConnListener(url, HttpMethod.PUT, headers, callBack, httpBody));
    }


    public void invoke(String host, Integer port, String url, HttpMethod method, DefaultHttpHeaders headers, byte[] httpBody, HttpResultCallBack callBack) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {
        connect(host, port).addListener(new HttpConnListener(url, method, headers, callBack, httpBody));
    }

    private ChannelFuture connect(String host, Integer port) {
        SocketAddress address = new InetSocketAddress(host, port);
        synchronized (hostMap) {
            ChannelFuture future = hostMap.get(address);
            if (future == null) {
                future = b.connect(host, port);
                hostMap.put(address, future);
                callBackQueue.put(address, new ConcurrentLinkedQueue<>());
                return future;
            } else if (!future.channel().isRegistered()) {
                ConcurrentLinkedQueue<HttpResultCallBack> queue = callBackQueue.get(address);
                if (queue != null && !queue.isEmpty()) {
                    for (HttpResultCallBack callBack : queue) {
                        callBack.failed(new Exception("Channel has been destroy"));
                    }
                }
                future = b.connect(host, port);
                hostMap.put(address, future);
                callBackQueue.put(address, new ConcurrentLinkedQueue<>());
                return future;
            } else {
                return future;
            }
        }
    }

}
