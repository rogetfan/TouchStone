package org.elise.test.framework.stack.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Glenn on 2017/9/8.
 */
public final class HttpClient {

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(32);
    private static final Bootstrap b = new Bootstrap();

    private static final Map<SocketAddress, List<HttpConnection>> hostPool = new ConcurrentHashMap<>();
    private static final Map<String, HttpConnection> connectionMap = new ConcurrentHashMap<>();

    private static HttpClient client = null;
    private static Boolean isInitialized = false;
    private static Integer maxConnCount;

    private static final Integer DEFAULT_MAX_CONN_COUNT = 8;
    private static final Integer DEFAULT_MAX_CONTENT_LENGTH = 1024*1024*8;

    public static HttpConnection getInstance(Integer flag, URI uri) {
        synchronized (isInitialized) {
            if (!isInitialized) {
                start(DEFAULT_MAX_CONN_COUNT,DEFAULT_MAX_CONTENT_LENGTH);
            }
        }
        SocketAddress address = new InetSocketAddress(uri.getHost(), uri.getPort() == -1 ? 80 : uri.getPort());
        List<HttpConnection> connList ;
        synchronized (hostPool) {
            connList = hostPool.get(address);
            if (connList == null) {
                connList = new ArrayList<>(maxConnCount);
                for (int i = 0; i < maxConnCount; i++)
                    connList.add(new HttpConnection(client,i,address));
                hostPool.put(address,connList);
            }
        }
        return connList.get(flag % maxConnCount);
    }


    public static void start(Integer maxContentLength, Integer maxConnCount) {
        client = new HttpClient(maxContentLength,maxConnCount);
        isInitialized = true;
    }

    private HttpClient(Integer maxContentLength, Integer maxConnCount) {
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator());
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("HttpResponseDecoder", new HttpResponseDecoder());
                p.addLast("HttpRequestEncoder", new HttpRequestEncoder());
                p.addLast("Aggregator", new HttpObjectAggregator(maxContentLength));
                p.addLast("HttpClient", new HttpRespHandler());
            }
        });
        this.maxConnCount = maxConnCount;
    }

    public static void close() {
        workerGroup.shutdownGracefully();
    }

    protected void register(String channelId, HttpConnection conn){
        connectionMap.put(channelId,conn);
    }

    protected void unregister(String channelId){
        connectionMap.remove(channelId);
    }

    protected Bootstrap getBootstrap(){
        return b;
    }

    protected static long getCounter(String channelId) throws Exception {
        HttpConnection conn = connectionMap.get(channelId);
        if (conn == null) {
            throw new Exception("Http Connection is null");
        } else {
            return conn.getCounter();
        }
    }

    protected static HttpResultCallBack getCallBack(String channelId) throws Exception {
        HttpConnection conn = connectionMap.get(channelId);
        if (conn == null) {
            throw new Exception("Http Connection is null");
        } else {
            return conn.getCallBack();
        }
    }

    protected static void putCallBack(String channelId, HttpResultCallBack callBack) throws Exception {
        HttpConnection conn = connectionMap.get(channelId);
        if (conn == null) {
            throw new Exception("Http Connection is null");
        } else {
            conn.setCallBack(callBack);
        }
    }
}